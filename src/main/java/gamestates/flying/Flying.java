package gamestates.flying;

import static utils.Constants.Audio;
import static utils.Constants.Flying.TypeConstants.BOMB;
import static utils.Constants.Flying.TypeConstants.DRONE;
import static utils.Constants.Flying.TypeConstants.POWERUP;
import static utils.Constants.Flying.TypeConstants.REPAIR;
import static utils.Constants.Flying.REPAIR_HEALTH;
import static utils.HelpMethods.GetAutomaticTrigger;
import static utils.HelpMethods.GetCutscenes;
import static utils.HelpMethods2.GetPickupItem;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.Cutscene;
import cutscenes.CutsceneManager2;
import entities.exploring.AutomaticTrigger;
import entities.flying.PlayerFly;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import entities.flying.pickupItems.PickupItem;
import game_events.*;
import gamestates.Gamestate;
import gamestates.State;
import gamestates.Statemethods;
import main_classes.Game;
import misc.ProgressValues;
import projectiles.ProjectileHandler;
import ui.GameoverOverlay;
import ui.LevelFinishedOverlay;
import ui.OptionsMenu;
import ui.PauseFlying;
import ui.TextboxManager2;
import utils.LoadSave;

public class Flying extends State implements Statemethods {

   public AudioPlayer audioPlayer;
   private PauseFlying pauseOverlay;
   private LevelFinishedOverlay levelFinishedOverlay;
   private GameoverOverlay gameoverOverlay;
   private MapManager2 mapManager;
   private FlyLevelInfo flyLevelInfo;
   private EnemyManager enemyManager;
   private ProjectileHandler projectileHandler;
   private PlayerFly player;
   private CutsceneManager2 cutsceneManager;
   private EventHandler eventHandler;
   private ArrayList<AutomaticTrigger> automaticTriggers;
   private ArrayList<PickupItem> pickupItems;

   private Integer level;
   private int song;
   private boolean pause = false;
   private boolean gamePlayActive = true;
   private boolean levelFinished = false;
   public boolean checkPointReached = false;
   private boolean gameOver = false;
   private float fgNormalSpeed = 2f;
   private float bgNormalSpeed = 0.7f;
   private float fgCurSpeed = fgNormalSpeed;
   private float bgCurSpeed = bgNormalSpeed;

   public Flying(Game game) {
      super(game);
      this.audioPlayer = game.getAudioPlayer();
      this.automaticTriggers = new ArrayList<>();
      this.pickupItems = new ArrayList<>();
      initClasses(game.getOptionsMenu(), game.getExploring().getProgressValues());
      loadEventReactions();
   }

   public void loadLevel(int level) {
      this.level = level;
      this.song = Audio.GetFlyLevelSong(level);
      mapManager = new MapManager2(level, flyLevelInfo.getBgImgHeight(level));
      player.setClImg(mapManager.clImg);
      projectileHandler.setClImg(mapManager.clImg);
      projectileHandler.setBombs(game.getExploring().getProgressValues().getBombs());
      enemyManager.loadEnemiesForLvl(level);
      loadPickupItems(level);
      loadCutscenes(level);
      player.setKilledEnemies(0);
      // startAt(-22000); // For testing purposes

   }

   private void initClasses(OptionsMenu optionsMenu, ProgressValues progValues) {
      Rectangle2D.Float playerHitbox = new Rectangle2D.Float(500f, 400f, 50f, 50f);
      this.player = new PlayerFly(game, playerHitbox);
      this.enemyManager = new EnemyManager(player, audioPlayer);
      this.projectileHandler = new ProjectileHandler(game, audioPlayer, player, enemyManager);
      this.eventHandler = new EventHandler();
      TextboxManager2 textboxManager = new TextboxManager2();
      this.cutsceneManager = new CutsceneManager2(eventHandler, textboxManager);
      this.pauseOverlay = new PauseFlying(this, optionsMenu);
      this.levelFinishedOverlay = new LevelFinishedOverlay(this, progValues);
      this.gameoverOverlay = new GameoverOverlay(this);
      this.flyLevelInfo = new FlyLevelInfo();
   }

   private void loadPickupItems(Integer level) {
      pickupItems.clear();
      automaticTriggers.clear();
      List<String> levelData = LoadSave.getFlyLevelData(level);
      for (String line : levelData) {
         String[] lineData = line.split(";");
         if (lineData[0].equals("automaticTrigger")) {
            automaticTriggers.add(GetAutomaticTrigger(lineData));
         } else if (lineData[0].equals("powerup")) {
            int width = 30;
            int height = 50;
            pickupItems.add(GetPickupItem(lineData, width, height, POWERUP));
         } else if (lineData[0].equals("repair")) {
            int width = 60;
            int height = 60;
            pickupItems.add(GetPickupItem(lineData, width, height, REPAIR));
         } else if (lineData[0].equals("bomb")) {
            int width = 45;
            int height = 45;
            pickupItems.add(GetPickupItem(lineData, width, height, BOMB));
         }
      }
   }

   private void loadCutscenes(Integer level) {
      this.cutsceneManager.clear();
      List<String> cutsceneData = LoadSave.getFlyCutsceneData(level);
      ArrayList<ArrayList<Cutscene>> cutscenes = GetCutscenes(cutsceneData);
      for (ArrayList<Cutscene> cutscenesForTrigger : cutscenes) {
         cutsceneManager.addCutscene(cutscenesForTrigger);
      }
   }

   private void loadEventReactions() {
      this.eventHandler.addEventListener(this::doReaction);
   }

   public void doReaction(GeneralEvent event) {
      if (event instanceof DialogueEvent evt) {
         this.cutsceneManager.startDialogue(evt.name(), evt.speed(), evt.text(), evt.portraitIndex());
      } else if (event instanceof FadeInEvent evt) {
         this.cutsceneManager.startFadeIn(evt.speed());
      } else if (event instanceof FadeOutEvent evt) {
         this.cutsceneManager.startFadeOut(evt.speed());
      } else if (event instanceof SetVisibleEvent evt) {
         this.player.setVisible(evt.visible());
      } else if (event instanceof WaitEvent evt) {
         this.cutsceneManager.waitFrames(evt.waitFrames());
      } else if (event instanceof SetOverlayImageEvent evt) {
         this.cutsceneManager.setOverlayImage(evt.fileName());
      } else if (event instanceof SetOverlayActiveEvent evt) {
         this.cutsceneManager.setOverlayActive(evt.active());
      } else if (event instanceof BlackScreenEvent evt) {
         this.cutsceneManager.setBlackScreen(evt.active());
      } else if (event instanceof SetGameplayEvent evt) {
         this.gamePlayActive = evt.active();
      } else if (event instanceof FadeHeaderEvent evt) {
         cutsceneManager.fadeHeader(evt.inOut(), evt.yPos(), evt.fadeSpeed(), evt.header());
      } else if (event instanceof LevelFinishedEvent evt) {
         this.levelFinished = true;
         this.enemyManager.levelFinished();
         this.levelFinishedOverlay.setLevelStats(enemyManager.getFinalKilledEnemies());
      } else if (event instanceof StartSongEvent evt) {
         this.game.getAudioPlayer().startSongLoop(evt.index(), 0);
      } else if (event instanceof StartAmbienceEvent evt) {
         audioPlayer.startAmbienceLoop(evt.index());
      } else if (event instanceof FadeOutLoopEvent evt) {
         audioPlayer.fadeOutAllLoops();
      } else if (event instanceof FellowShipEvent evt) {
         cutsceneManager.startFellowShips(evt.xPos(), evt.yPos(), evt.takeOffTimer());
      } else if (event instanceof GoToBossEvent evt) {
         transferBombsToProgValues();
         game.getBossMode().loadNewBoss(evt.bossNr());
         Gamestate.state = Gamestate.BOSS_MODE;
         // We do not need to do anything else, as we will return to Flying after
         // the boss, and thus exit Flying properly then.
      }
      /*
       * else if (event instanceof SetStartingCutsceneEvent2 evt) {
       * this.setNewStartingCutscene(evt.triggerObject(), evt.cutsceneIndex());
       * }
       */
   }

   /** Flips the pause boolean */
   public void flipPause() {
      this.pause = !pause;
   }

   @Override
   public void update() {
      if (gameOver) {
         gameoverOverlay.update();
      } else if (pause) {
         checkPause();
         pauseOverlay.update();
      } else if (levelFinished) {
         levelFinishedOverlay.update();
      } else if (gamePlayActive) {
         updateGameplay();
      }
      if (!pause) {
         cutsceneManager.update();
      }
   }

   private void updateGameplay() {
      if (!cutsceneManager.isActive()) {
         checkCutsceneTriggers();
      }
      updateChartingY();
      checkPause();
      moveMaps();
      checkCheckPoint();
      moveCutscenes();
      player.update(mapManager.clYOffset, mapManager.clXOffset);
      updatePickupItems();
      enemyManager.update(fgCurSpeed);
      projectileHandler.update(
         mapManager.clYOffset, mapManager.clXOffset, fgCurSpeed);
   }

   private void checkPause() {
      if (game.pauseIsPressed) {
         game.pauseIsPressed = false;
         this.flipPause();
         audioPlayer.flipAudioOnOff();
      }
   }

   private void checkCheckPoint() {
      if (checkPointReached) {
         return;
      } else if (mapManager.yProgess >= flyLevelInfo.getCheckPoint(level)) {
         this.checkPointReached = true;
         enemyManager.checkPointReached();
         projectileHandler.checkPointReached();
      }
   }

   private void updatePickupItems() {
      for (PickupItem p : pickupItems) {
         p.update(fgCurSpeed);
         if (p.isActive() && (p.getHitbox().intersects(player.getHitbox()))) {
            p.setActive(false);
            if (p.getType() == POWERUP) {
               projectileHandler.setPowerup(true);
               audioPlayer.playSFX(Audio.SFX_POWERUP);
            } else if (p.getType() == REPAIR) {
               player.increaseHealth(REPAIR_HEALTH);
               audioPlayer.playSFX(Audio.SFX_REPAIR);
            } else if (p.getType() == BOMB) {
               projectileHandler.addBombToInventory();
               audioPlayer.playSFX(Audio.SFX_BOMB_PICKUP);
            }
         }
      }
   }

   private void moveCutscenes() {
      for (AutomaticTrigger trigger : automaticTriggers) {
         trigger.getHitbox().y += fgCurSpeed;
      }
   }

   private void checkCutsceneTriggers() {
      int index = 0;
      for (AutomaticTrigger trigger : automaticTriggers) {
         if ((trigger.getHitbox().y > 0) && (trigger.getHitbox().y < 10)) {
            if (!trigger.hasPlayed()) {
               this.cutsceneManager.startCutscene(index, automaticTriggers.get(index).getStartCutscene());
               trigger.setPlayed(true);
            }
         }
         index += 1;
      }
   }

   private void moveMaps() {
      if (player.takesCollisionDmg()) {
         fgCurSpeed = fgNormalSpeed / 2;
         bgCurSpeed = bgNormalSpeed / 2;
      } else {
         fgCurSpeed = fgNormalSpeed;
         bgCurSpeed = bgNormalSpeed;
      }
      mapManager.clYOffset += fgCurSpeed;
      mapManager.bgYOffset += bgCurSpeed;
   }

   @Override
   public void draw(Graphics g) {
      if (!levelFinished) {
         mapManager.drawMaps(g);
         drawPickupItems(g);
         this.player.draw(g);
         this.enemyManager.draw(g);
         this.projectileHandler.draw(g);
      }
      if (!gameOver) {
         this.cutsceneManager.draw(g);
      }
      if (gameOver) {
         gameoverOverlay.draw(g);
      } else if (pause) {
         pauseOverlay.draw(g);
      } else if (levelFinished) {
         levelFinishedOverlay.draw(g);
      }
   }

   private void drawPickupItems(Graphics g) {
      for (PickupItem p : pickupItems) {
         p.draw(g);
      }
   }

   public void exitFinishedLevel() {
      // Credits are updated in LevelFinishedOverlay.
      transferBombsToProgValues();
      this.checkPointReached = false;
      this.resetFlying();
      if (this.level == 0) {
         Gamestate.state = Gamestate.EXPLORING;
      } else {
         game.getLevelSelect().reset();
         game.getLevelSelect().updateUnlockedLevels(level, enemyManager.getFinalKilledEnemies().size());
         Gamestate.state = Gamestate.LEVEL_SELECT;
      }
   }

   private void transferBombsToProgValues() {
      game.getExploring().getProgressValues().setBombs(projectileHandler.getBombsAtEndOfLevel());
      game.getExploring().updatePauseInventory();
   }

   /** Used only for testing porposes */
   private void startAt(int y) {
      mapManager.yProgess = -y;
      mapManager.clYOffset -= y;
      mapManager.bgYOffset -= y * (bgCurSpeed / fgCurSpeed);
      for (PickupItem p : pickupItems) {
         p.getHitbox().y -= y;
      }
      for (AutomaticTrigger trigger : automaticTriggers) {
         trigger.getHitbox().y -= y;
      }
      enemyManager.moveAllEnemies(y);
   }

   /**
    * Resets all non-level-specific values in flying-mode.
    * Should be called both when loading a new level, and when resetting the
    * current level.
    * (Level-specific values are set in the 'loadLevel'-method and
    * 'resetLevel'-method)
    */
   public void resetFlying() {
      pause = false;
      gamePlayActive = true;
      levelFinished = false;
      gameOver = false;
      fgCurSpeed = fgNormalSpeed;
      bgCurSpeed = bgNormalSpeed;
      player.reset();
      gameoverOverlay.reset();
      projectileHandler.reset();
      cutsceneManager.reset();
   }

   public void killPlayer() {
      gameOver = true;
      gamePlayActive = false;
      player.setVisible(false);
      gameoverOverlay.setPlayerPos(player.getHitbox().x, player.getHitbox().y);
      audioPlayer.stopAllLoops();
      audioPlayer.startAmbienceLoop(Audio.AMBIENCE_SILENCE);
      audioPlayer.playSFX(Audio.SFX_DEATH);
   }

   /**
    * Not to be confused with the resetFlying()-method. This method should be called
    * when the player has died, and resets the level.
    * It should reset (not reload) all level-specific variables,
    * like enemies, pickup-items and map-offsets.
    */
   public void resetLevel(boolean toCheckPoint) {
      // Determine resetpoints:
      float resetYPos = flyLevelInfo.getResetPoint(level);
      float songResetPos = flyLevelInfo.getSongResetPoint(level);
      if (toCheckPoint) {
         resetYPos = flyLevelInfo.getCheckPoint(level) + 100f; // +100 to avoid cutscene and get faster into action
         songResetPos = flyLevelInfo.getSongCheckPoint(level);
      }
      // Reset:
      resetLevelValues(toCheckPoint);
      resetObjects(toCheckPoint, resetYPos);

      // Restart audio
      audioPlayer.startSongLoop(song, songResetPos);
      audioPlayer.startAmbienceLoop(Audio.AMBIENCE_ROCKET_ENGINE);
   }

   private void resetLevelValues(boolean toCheckPoint) {
      // If the player has reached checkPoint, but decides to reset level to start:
      if (!toCheckPoint) {
         this.checkPointReached = false;
      }  
   }

   private void resetObjects(boolean toCheckPoint, float resetYPos) {
      for (PickupItem p : pickupItems) {
         p.resetTo(resetYPos);
      }
      for (AutomaticTrigger trigger : automaticTriggers) {
         trigger.resetTo(resetYPos);
      }
      mapManager.resetOffsetsTo(resetYPos, (bgCurSpeed / fgCurSpeed));
      resetChartingY(toCheckPoint);
      projectileHandler.resetBombs(toCheckPoint);
      enemyManager.resetEnemiesTo(resetYPos, toCheckPoint);
      if (toCheckPoint) {
         player.setKilledEnemies(enemyManager.getEnemiesKilledAtCheckpoint().size());
      }
   }

   private void resetChartingY(boolean toCheckPoint) {
      if (toCheckPoint) {
         mapManager.yProgess = flyLevelInfo.getCheckPoint(level);
      } else {
         mapManager.yProgess = 0;
      }
   }

   /**
    * Changes the levelOffset to the end of the level, and moves the automatic
    * triggers accorgingly.
    * Doesn't move enemies or pickup-items.
    */
   public void skipLevel() {
      this.cutsceneManager.skipCutscene();
      float skipYPos = flyLevelInfo.getSkipLevelPoint(level);
      mapManager.resetOffsetsTo(skipYPos, (bgCurSpeed / fgCurSpeed));
      for (AutomaticTrigger trigger : automaticTriggers) {
         trigger.resetTo(skipYPos);
      }
   }

   /**
    * Can be used to artificially increase the number of killed enemies by 10.
    * Use for testing purposes.
    */
   public void plus10KilledEnemies() {
      for (int i = 0; i < 10; i++) {
         this.enemyManager.increaseKilledEnemies(DRONE);
      }
   }

   /**
    * Can be used to artificially decrease the number of killed enemies by 10.
    * Use for testing purposes.
    */
   public void minus10KilledEnemies() {
      for (int i = 0; i < 10; i++) {
         this.enemyManager.decreaseKilledEnemies(DRONE);
      }
   }

   /**
    * Is called from Player. Is needed to check teleport collision with big
    * enemies.
    */
   public ArrayList<Enemy> getBigEnemies() {
      return this.enemyManager.getBigEnemies();
   }

   /** Is called from Player. Is needed for big enemies */
   public void checkIfDead(Enemy enemy) {
      this.enemyManager.checkIfDead(enemy);
   }

   private void updateChartingY() {
      mapManager.yProgess += fgCurSpeed;
   }
}
