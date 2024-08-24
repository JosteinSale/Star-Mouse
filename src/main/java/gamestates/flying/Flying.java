package gamestates.flying;

import static utils.Constants.Exploring.Cutscenes.AUTOMATIC;
import static utils.Constants.Flying.REPAIR_HEALTH;
import static entities.flying.EntityFactory.TypeConstants.DRONE;
import static entities.flying.EntityFactory.TypeConstants.BOMB;
import static entities.flying.EntityFactory.TypeConstants.REPAIR;
import static entities.flying.EntityFactory.TypeConstants.POWERUP;
import static utils.HelpMethods.GetAutomaticTrigger;
import static utils.HelpMethods.GetCutscenes;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.Cutscene;
import cutscenes.cutsceneManagers.CutsceneManagerFly;
import data_storage.ProgressValues;
import entities.exploring.AutomaticTrigger;
import entities.flying.EntityFactory;
import entities.flying.PlayerFly;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import entities.flying.pickupItems.PickupItem;
import game_events.*;
import gamestates.Gamestate;
import gamestates.State;
import gamestates.Statemethods;
import main_classes.Game;
import projectiles.ProjectileHandler;
import ui.GameoverOverlay;
import ui.LevelFinishedOverlay;
import ui.OptionsMenu;
import ui.PauseFlying;
import utils.Constants.Audio;
import utils.ResourceLoader;

public class Flying extends State implements Statemethods {

   public AudioPlayer audioPlayer;
   private PauseFlying pauseOverlay;
   private LevelFinishedOverlay levelFinishedOverlay;
   private GameoverOverlay gameoverOverlay;
   private MapManager2 mapManager;
   private FlyLevelInfo flyLevelInfo;
   private EntityFactory entityFactory;
   private EnemyManager enemyManager;
   private ProjectileHandler projectileHandler;
   private PlayerFly player;
   private CutsceneManagerFly cutsceneManager;
   private EventHandler eventHandler;
   private ArrayList<AutomaticTrigger> automaticTriggers;
   private ArrayList<PickupItem> pickupItems;

   private Integer level;
   private int song;
   private boolean shouldSongLoop;
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
      initClasses(game.getOptionsMenu());
      loadEventReactions();
   }

   private void initClasses(OptionsMenu optionsMenu) {
      Rectangle2D.Float playerHitbox = new Rectangle2D.Float(500f, 400f, 50f, 50f);
      this.player = new PlayerFly(game, playerHitbox);
      this.entityFactory = new EntityFactory(player);
      this.enemyManager = new EnemyManager(player, entityFactory, audioPlayer);
      this.projectileHandler = new ProjectileHandler(game, audioPlayer, player, enemyManager);
      this.eventHandler = new EventHandler();
      this.cutsceneManager = new CutsceneManagerFly(Gamestate.FLYING, game, eventHandler, game.getTextboxManager());
      this.pauseOverlay = new PauseFlying(this, optionsMenu);
      this.levelFinishedOverlay = new LevelFinishedOverlay(game, this);
      this.gameoverOverlay = new GameoverOverlay(this);
      this.flyLevelInfo = new FlyLevelInfo();
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
      // startAt(-20000); // For testing purposes
   }

   private void loadPickupItems(Integer level) {
      pickupItems.clear();
      automaticTriggers.clear();
      List<String> levelData = ResourceLoader.getFlyLevelData(level);
      for (String line : levelData) {
         String[] lineData = line.split(";");
         String entryName = lineData[0];
         if (entryName.equals("automaticTrigger")) {
            automaticTriggers.add(GetAutomaticTrigger(lineData));
         } else if (entityFactory.isPickupItemRegistered(entryName)) {
            pickupItems.add(entityFactory.getNewPickupItem(lineData));
         }
      }
   }

   private void loadCutscenes(Integer level) {
      this.cutsceneManager.clear();
      List<String> cutsceneData = ResourceLoader.getFlyCutsceneData(level);
      ArrayList<ArrayList<Cutscene>> cutscenes = GetCutscenes(cutsceneData);
      for (ArrayList<Cutscene> cutscenesForTrigger : cutscenes) {
         cutsceneManager.addCutscene(cutscenesForTrigger);
      }
   }

   private void loadEventReactions() {
      this.eventHandler.addEventListener(this::doReaction);
   }

   public void doReaction(GeneralEvent event) {
      if (event instanceof TextBoxEvent tbEvt) {
         this.cutsceneManager.activateTextbox(tbEvt);
      } else if (event instanceof SetGameplayEvent evt) {
         this.gamePlayActive = evt.active();
      } else if (event instanceof LevelFinishedEvent) {
         this.levelFinished = true;
         this.enemyManager.levelFinished();
         this.levelFinishedOverlay.setLevelStats(enemyManager.getFinalKilledEnemies());
      } else if (event instanceof StartSongEvent evt) {
         this.shouldSongLoop = evt.shouldLoop();
         this.game.getAudioPlayer().startSong(evt.index(), 0, shouldSongLoop);
      } else if (event instanceof StartAmbienceEvent evt) {
         audioPlayer.startAmbienceLoop(evt.index());
      } else if (event instanceof FadeOutLoopEvent) {
         audioPlayer.fadeOutAllLoops();
      } else if (event instanceof StopLoopsEvent) {
         this.audioPlayer.stopAllLoops();
      } else if (event instanceof AddProjectileEvent evt) {
         this.projectileHandler.addCustomProjectile(evt);
      } else if (event instanceof GoToBossEvent evt) {
         transferBombsToProgValues();
         game.getBossMode().loadNewBoss(evt.bossNr());
         Gamestate.state = Gamestate.BOSS_MODE;
         // We do not need to do anything else, as we will return to Flying after
         // the boss, and thus exit Flying properly then.
      } else {
         this.cutsceneManager.activateEffect(event);
      }
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
      // System.out.println(mapManager.yProgess);
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
      }
   }

   public void flipPause() {
      // 1. If we are entering pause:
      if (!pause) {
         this.pause = true;
         this.audioPlayer.stopAllLoops();
      }
      // 2. If we are exiting pause:
      else {
         this.pause = false;
         this.audioPlayer.continueCurrentAmbience();
         this.audioPlayer.continueCurrentSong();
      }
   }

   public void resetPause() {
      this.pause = false;
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
               this.cutsceneManager.startCutscene(index, AUTOMATIC, automaticTriggers.get(index).getStartCutscene());
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
      if (this.level != 0) {
         transferBombsToProgValues();
      }
      this.checkPointReached = false;
      this.resetFlying();
      if (this.level == 0) {
         Gamestate.state = Gamestate.EXPLORING;
      } else {
         game.getLevelSelect().reset();
         game.getLevelSelect().updateUnlockedLevels(level, enemyManager.getFinalKilledEnemies().size());
         game.saveDataToDisc();
         Gamestate.state = Gamestate.LEVEL_SELECT;
      }
   }

   private void transferBombsToProgValues() {
      game.getExploring().getProgressValues().setBombs(projectileHandler.getBombsAtEndOfLevel());
      game.getExploring().updatePauseInventory();
   }

   /**
    * Is called from bossMode right before
    * exitFinishedLevel and transferBombsToProgValues
    */
   public void setBombsWhenBossIsFinished(int nrOfBombs) {
      this.projectileHandler.setBombs(nrOfBombs);
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
      audioPlayer.playSFX(Audio.SFX_DEATH);
   }

   /**
    * Not to be confused with the resetFlying()-method. This method should be
    * called
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
      audioPlayer.startSong(song, songResetPos, shouldSongLoop);
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
      this.cutsceneManager.reset();
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
