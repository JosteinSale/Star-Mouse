package gamestates.flying;

import static utils.Constants.Flying.REPAIR_HEALTH;
import static entities.flying.EnemyFactory.TypeConstants.DRONE;
import static entities.flying.pickupItems.PickupItemFactory.TypeConstants.*;
import static utils.HelpMethods.GetAutomaticTrigger;
import static utils.HelpMethods.ParseCutscenes;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.cutsceneManagers.CutsceneManagerFly;
import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import entities.exploring.AutomaticTrigger;
import entities.flying.EnemyFactory;
import entities.flying.PlayerFly;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import entities.flying.pickupItems.PickupItem;
import entities.flying.pickupItems.PickupItemFactory;
import game_events.*;
import gamestates.Gamestate;
import gamestates.State;
import main_classes.Game;
import projectiles.ProjectileHandler;
import ui.GameoverOverlay;
import ui.LevelFinishedOverlay;
import ui.OptionsMenu;
import ui.PauseFlying;
import utils.Constants.Audio;
import utils.ResourceLoader;

public class Flying extends State {

   public AudioPlayer audioPlayer;
   private PauseFlying pauseOverlay;
   private LevelFinishedOverlay levelFinishedOverlay;
   private GameoverOverlay gameoverOverlay;
   private MapManager2 mapManager;
   private PickupItemFactory pickupFactory;
   private EnemyManager enemyManager;
   private ProjectileHandler projectileHandler;
   private PlayerFly player;
   private CutsceneManagerFly cutsceneManager;
   private EventHandler eventHandler;
   private ArrayList<AutomaticTrigger> automaticTriggers;
   public ArrayList<PickupItem> pickupItems;

   private Integer level;
   private int song;
   public boolean shouldSongLoop;
   public boolean pause = false;
   public boolean gamePlayActive = true;
   public boolean levelFinished = false;
   public boolean checkPointReached = false;
   public boolean gameOver = false;
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
      this.pickupFactory = new PickupItemFactory();
      this.enemyManager = new EnemyManager(player, audioPlayer);
      this.projectileHandler = new ProjectileHandler(game, audioPlayer, player, enemyManager);
      this.eventHandler = new EventHandler();
      this.cutsceneManager = new CutsceneManagerFly(Gamestate.FLYING, game, eventHandler, game.getTextboxManager());
      this.pauseOverlay = new PauseFlying(this, optionsMenu);
      this.levelFinishedOverlay = new LevelFinishedOverlay(game, this);
      this.gameoverOverlay = new GameoverOverlay(this);
      this.mapManager = new MapManager2();
   }

   public void loadLevel(int lvl) {
      this.level = lvl;
      this.song = Audio.GetFlyLevelSong(lvl);
      mapManager.loadNewMap(lvl, FlyLevelInfo.getBgImgHeight(lvl), game);
      player.setClImg(mapManager.clImg);
      projectileHandler.setClImg(mapManager.clImg);
      projectileHandler.setBombs(game.getProgressValues().getBombs());
      List<String> levelData = ResourceLoader.getFlyLevelData(lvl);
      enemyManager.loadEnemiesForLvl(levelData);
      loadLevelData(levelData);
      loadCutscenes(lvl);
      player.onLevelStart();
      this.setRenders(lvl, FlyLevelInfo.getBgImgHeight(lvl));
      // startAt(7000); // For testing purposes
   }

   /**
    * Sets all Flying-renders for this level, as well as sets the
    * cutsceneManager for RenderCutscene.
    */
   private void setRenders(int lvl, int bgImgHeight) {
      game.getView().getRenderFlying().loadLevel(lvl, bgImgHeight);
      game.getView().getRenderCutscene().setCutsceneManager(cutsceneManager);
   }

   private void loadLevelData(List<String> levelData) {
      pickupItems.clear();
      automaticTriggers.clear();
      for (String line : levelData) {
         String[] lineData = line.split(";");
         String entryName = lineData[0];
         if (entryName.equals("automaticTrigger")) {
            automaticTriggers.add(GetAutomaticTrigger(lineData));
         } else if (pickupFactory.isPickupItemRegistered(entryName)) {
            pickupItems.add(pickupFactory.getPickupItemFromLineData(lineData));
         }
      }
   }

   private void loadCutscenes(Integer level) {
      this.cutsceneManager.clear();
      List<String> cutsceneData = ResourceLoader.getFlyCutsceneData(level);
      cutsceneManager.addCutscenes(ParseCutscenes(cutsceneData));
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
         goToBossMode(evt.bossNr());
      } else {
         this.cutsceneManager.activateEffect(event);
      }
   }

   public void update() {
      if (game.isFading()) {
         return;
      }
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
      moveCutsceneTriggers();
      player.update(mapManager.clYOffset, mapManager.clXOffset, fgCurSpeed);
      updatePickupItems(pickupItems, fgCurSpeed, player, projectileHandler, audioPlayer);
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
         this.audioPlayer.pauseAllLoops();
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
      } else if (mapManager.yProgess >= FlyLevelInfo.getCheckPoint(level)) {
         this.checkPointReached = true;
         enemyManager.checkPointReached();
         projectileHandler.checkPointReached();
      }
   }

   public static void updatePickupItems(
         ArrayList<PickupItem> pickupItems, float fgCurSpeed,
         PlayerFly player, ProjectileHandler projectileHandler, AudioPlayer audioPlayer) {
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

   private void moveCutsceneTriggers() {
      for (AutomaticTrigger trigger : automaticTriggers) {
         trigger.getHitbox().y += fgCurSpeed;
      }
   }

   private void checkCutsceneTriggers() {
      for (AutomaticTrigger trigger : automaticTriggers) {
         if ((trigger.getHitbox().y > 0) && (trigger.getHitbox().y < 10)) {
            if (!trigger.hasPlayed()) {
               String entityName = trigger.getName();
               this.cutsceneManager.startCutscene(entityName, trigger.getStartCutscene());
               trigger.setPlayed(true);
            }
         }
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

   /** Is called from the levelFinishedOverlay */
   public void exitFinishedLevel() {
      if (this.level != 0) {
         transferBombsToProgValues();
      }
      this.resetValuesOnExit();
      if (this.level == 0) {
         Gamestate.state = Gamestate.EXPLORING;
         game.getView().getRenderCutscene().setCutsceneManager(game.getExploring().getCurrentCutsceneManager());
      } else {
         game.getLevelSelect().updateUnlockedLevels(level, enemyManager.getFinalKilledEnemies().size());
         game.saveDataToDisc();
         Gamestate.state = Gamestate.LEVEL_SELECT;
         game.getLevelSelect().returnToLevelSelect();
      }
   }

   /**
    * Call if the player has chosen 'Main Menu' in the PauseOverlay /
    * GameOverOverlay
    */
   public void exitToMainMenu() {
      game.returnToMainMenu(() -> resetValuesOnExit());
   }

   private void transferBombsToProgValues() {
      game.getProgressValues().setBombs(projectileHandler.getBombsAtEndOfLevel());
      game.getExploring().updatePauseInventory();
   }

   private void goToBossMode(int bossNr) {
      transferBombsToProgValues();
      game.getBossMode().loadNewBoss(bossNr);
      Gamestate.state = Gamestate.BOSS_MODE;
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
      mapManager.yProgess = y;
      mapManager.clYOffset += y;
      mapManager.bgYOffset += y * (bgCurSpeed / fgCurSpeed);
      for (PickupItem p : pickupItems) {
         p.getHitbox().y += y;
      }
      for (AutomaticTrigger trigger : automaticTriggers) {
         trigger.getHitbox().y += y;
      }
      enemyManager.moveAllEnemies(-y);
   }

   /**
    * Resets values that needs to be cleared when exiting flying-mode.
    * (Other values are reset when loading a new level)
    */
   public void resetValuesOnExit() {
      resetGeneralValues();
      checkPointReached = false;
   }

   /**
    * Resets the most general values in flying-mode. Does not reset things that
    * require specific logic, such as enemy positions, song position, etc.
    */
   private void resetGeneralValues() {
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
    * This method is called when the player has died, and resets the level.
    * The player may choose to reset to either the start of the level,
    * or to the last checkpoint reached.
    */
   public void restartLevel(boolean toCheckPoint) {
      // 1. Reset general values
      resetGeneralValues();

      // 2. Reset checkpointReached if needed
      if (!toCheckPoint) {
         checkPointReached = false;
      }

      // 3. Determine resetpoints:
      float resetYPos = FlyLevelInfo.getResetPoint(level);
      float songResetPos = FlyLevelInfo.getSongResetPoint(level);
      if (toCheckPoint) {
         resetYPos = FlyLevelInfo.getCheckPoint(level) + 100f; // +100 to avoid cutscene and get faster into action
         songResetPos = FlyLevelInfo.getSongCheckPoint(level);
      }
      // 4. Reset objects:
      resetObjects(toCheckPoint, resetYPos);

      // 5. Restart audio
      audioPlayer.startSong(song, songResetPos, shouldSongLoop);
      audioPlayer.startAmbienceLoop(Audio.AMBIENCE_ROCKET_ENGINE);
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
         mapManager.yProgess = FlyLevelInfo.getCheckPoint(level);
      } else {
         mapManager.yProgess = FlyLevelInfo.getResetPoint(level);
      }
   }

   /**
    * Changes the levelOffset to the end of the level, and moves the automatic
    * triggers accorgingly.
    * Doesn't move enemies or pickup-items.
    */
   public void skipLevel() {
      this.cutsceneManager.reset();
      float skipYPos = FlyLevelInfo.getSkipLevelPoint(level);
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
   public void checkIfDeadAndHandleDeath(Enemy enemy) {
      if (enemy.isDead()) {
         this.enemyManager.handleEnemyDeath(enemy);
      }
   }

   private void updateChartingY() {
      mapManager.yProgess += fgCurSpeed;
   }

   public EnemyFactory getEnemyFactory() {
      return this.enemyManager.getEnemyFactory();
   }

   public MapManager2 getMapManager() {
      return this.mapManager;
   }

   public PlayerFly getPlayer() {
      return this.player;
   }

   public EnemyManager getEnemyManager() {
      return this.enemyManager;
   }

   public ProjectileHandler getProjectileHandler() {
      return this.projectileHandler;
   }

   public DefaultCutsceneManager getCutsceneManager() {
      return this.cutsceneManager;
   }

   public GameoverOverlay getGameOverOverlay() {
      return this.gameoverOverlay;
   }

   public PauseFlying getPauseMenu() {
      return this.pauseOverlay;
   }

   public LevelFinishedOverlay getLevelFinishedOverlay() {
      return this.levelFinishedOverlay;
   }

   public ArrayList<PickupItem> getPickupItems() {
      return this.pickupItems;
   }
}
