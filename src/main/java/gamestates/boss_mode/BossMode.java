package gamestates.boss_mode;

import static utils.HelpMethods.ParseCutscenes;
import static utils.Constants.Exploring.Cutscenes.BOSS;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.cutsceneManagers.CutsceneManagerBoss;
import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import entities.bossmode.PlayerBoss;
import entities.bossmode.rudinger1.Rudinger1;
import entities.bossmode.AnimatedComponentFactory;
import entities.bossmode.IBoss;
import entities.flying.enemies.EnemyManager;
import entities.flying.pickupItems.PickupItem;
import game_events.*;
import gamestates.Gamestate;
import gamestates.State;
import gamestates.flying.Flying;
import main_classes.Game;
import main_classes.Testing;
import projectiles.ProjectileHandler;
import projectiles.ProjectileHandler2;
import ui.GameoverOverlay2;
import ui.PauseBoss;
import ui.TextboxManager;
import utils.Constants.Audio;
import utils.ResourceLoader;

public class BossMode extends State {
   private PlayerBoss player;
   private ProjectileHandler2 projectileHandler;
   private AnimatedComponentFactory animationFactory;
   public ArrayList<PickupItem> pickupItems;
   private AudioPlayer audioPlayer;
   private CutsceneManagerBoss cutsceneManager;
   private IBoss boss;
   private int bossNr;

   private PauseBoss pauseOverlay;
   private GameoverOverlay2 gameoverOverlay;
   public boolean ambienceEnabled = false;
   public boolean musicEnabled = false;
   public boolean pause = false;
   public boolean gameOver = false;

   public BossMode(Game game) {
      super(game);
      audioPlayer = game.getAudioPlayer();
      initClasses();
   }

   private void initClasses() {
      Rectangle2D.Float playerHitbox = new Rectangle2D.Float(500f, 600f, 50f, 50f);
      player = new PlayerBoss(game, playerHitbox);

      pickupItems = new ArrayList<>();
      projectileHandler = new ProjectileHandler2(
            game,
            game.getAudioPlayer(),
            player,
            new EnemyManager(null, null));
      animationFactory = new AnimatedComponentFactory();
      pauseOverlay = new PauseBoss(game, this, game.getOptionsMenu());
      gameoverOverlay = new GameoverOverlay2(game, this);

      EventHandler eventHandler = new EventHandler();
      eventHandler.addEventListener(this::doReaction);
      TextboxManager textboxManager = game.getTextboxManager();

      cutsceneManager = new CutsceneManagerBoss(game, eventHandler, textboxManager, Gamestate.BOSS_MODE);
   }

   public void doReaction(GeneralEvent event) {
      if (event instanceof TextBoxEvent tbEvt) {
         cutsceneManager.activateTextbox(tbEvt);
      } else if (event instanceof StartSongEvent evt) {
         musicEnabled = true;
         audioPlayer.startSong(evt.index(), 0, true);
      } else if (event instanceof StartAmbienceEvent evt) {
         ambienceEnabled = true;
         audioPlayer.startAmbienceLoop(evt.index());
      } else if (event instanceof StopLoopsEvent) {
         ambienceEnabled = false;
         musicEnabled = false;
         audioPlayer.stopAllLoops();
      } else if (event instanceof FadeOutLoopEvent) {
         ambienceEnabled = false;
         musicEnabled = false;
         audioPlayer.fadeOutAllLoops();
      } else if (event instanceof PlaySFXEvent evt) {
         audioPlayer.playSFX(evt.SFXIndex());
      } else if (event instanceof GoToFlyingEvent) {
         goToFlying();
      } else if (event instanceof SetBossVisibleEvent evt) {
         boss.setVisible(evt.visible());
      } else if (event instanceof ObjectMoveEvent evt) {
         cutsceneManager.moveObject(evt);
      } else if (event instanceof ClearObjectsEvent) {
         cutsceneManager.clearObjects();
      } else if (event instanceof SetVisibleEvent evt) {
         player.setVisible(evt.visible());
      } else {
         cutsceneManager.activateEffect(event);
      }
   }

   /** Should be called from the boss-defeated-cutscene. */
   private void goToFlying() {
      game.getFlying().setBombsWhenBossIsFinished(projectileHandler.getBombsAtEndOfLevel());

      // Render stuff
      game.getView().getRenderCutscene().setCutsceneManager(game.getFlying().getCutsceneManager());

      player.reset();
      pause = false;
      gameOver = false;

      Gamestate.state = Gamestate.FLYING;
   }

   /**
    * Loads the boss and prepares all objects in BossMode.
    * Also loads the renders for the boss.
    * Finally it starts the opening cutscene.
    */
   public void loadNewBoss(int bossNr) {
      this.bossNr = bossNr;
      loadBoss(bossNr);
      player.onLevelStart();
      setPlayerBossParts();
      game.getView().getRenderBossMode().loadBoss(bossNr);
      loadCutscenes(bossNr);
      projectileHandler.setBoss(bossNr, boss);
      startCutscene(0);
   }

   /** Wrapper method which strips away unecessary arguments */
   private void startCutscene(int i) {
      cutsceneManager.startCutscene(BOSS, i);
   }

   private void setPlayerBossParts() {
      player.setBoss(boss.getBossParts());
   }

   private void loadCutscenes(int bossNr) {
      cutsceneManager.clear();
      List<String> cutsceneData = ResourceLoader.getBossCutsceneData(bossNr);
      cutsceneManager.addCutscenes(ParseCutscenes(cutsceneData));
   }

   private void loadBoss(int bossNr) {
      switch (bossNr) {
         case 1:
            boss = new Rudinger1(
                  game, player, projectileHandler,
                  animationFactory, pickupItems);
            return;
         default:
            throw new IllegalArgumentException("No boss available for bossNr: " + bossNr);
      }
   }

   public void update() {
      if (game.isFading())
         return;
      checkKeyboardInputs();
      if (pause) {
         pauseOverlay.update();
      } else if (gameOver) {
         gameoverOverlay.update();
      } else if (cutsceneManager.isActive()) {
         player.updateOnlyFlame();
         cutsceneManager.handleKeyBoardInputs();
         cutsceneManager.update();
      } else {
         player.update(0, 0);
         Flying.updatePickupItems(pickupItems, 0f, player, projectileHandler, audioPlayer);
         boss.update();
         projectileHandler.update(boss.getXPos(), boss.getYPos(), 0);
         checkBossDeath();
      }
   }

   private void checkKeyboardInputs() {
      if (game.pauseIsPressed) {
         game.pauseIsPressed = false;
         flipPause();
      }
   }

   public void flipPause() {
      if (pause == false) {
         pause = true;
         game.getAudioPlayer().pauseAllLoops();
      } else {
         pause = false;
         if (musicEnabled) {
            audioPlayer.continueCurrentSong();
         }
         if (ambienceEnabled) {
            audioPlayer.continueCurrentAmbience();
         }
      }
   }

   private void checkBossDeath() {
      if (boss.isDead()) {
         killBoss(); // Yup. Kill the dead boss.
      }
   }

   /** Resets the player, current boss and projectileHandler. */
   public void resetBossMode() {
      pickupItems.clear();
      projectileHandler.reset();
      projectileHandler.resetBombs(false);
      cutsceneManager.reset();
      player.reset();
      boss.reset();
      gameoverOverlay.reset();
      pause = false;
      gameOver = false;
      ambienceEnabled = false;
      musicEnabled = false;
   }

   /** Is called from the gameOverOverlay */
   public void restartBossSong() {
      int songNr = switch (bossNr) {
         case 1 -> Audio.SONG_BOSS1;
         default -> throw new IllegalArgumentException("No song available for boss nr: " + bossNr);
      };
      musicEnabled = true;
      ambienceEnabled = true;
      game.getAudioPlayer().startSong(songNr, 0f, true);
      game.getAudioPlayer().startAmbienceLoop(Audio.AMBIENCE_ROCKET_ENGINE);
   }

   /** Is called from the pauseOverlay */
   public void skipBossMode() {
      resetBossMode();
      goToFlying();
   }

   public void killPlayer() {
      gameOver = true;
      player.setVisible(false);
      gameoverOverlay.setPlayerPos(player.getHitbox().x, player.getHitbox().y);
      game.getAudioPlayer().stopAllLoops();
      game.getAudioPlayer().playSFX(Audio.SFX_DEATH);
   }

   /**
    * Stops all sound loops, and starts the second cutscene in the cutscene sheet.
    * (This also inactivates the boss). Death animation and death-sfx for the boss
    * should be in that cutscene.
    */
   public void killBoss() {
      game.getAudioPlayer().stopAllLoops();
      ambienceEnabled = false;
      musicEnabled = false;
      projectileHandler.reset();
      pickupItems.clear();
      startCutscene(1);
   }

   public DefaultCutsceneManager getCutsceneManager() {
      return cutsceneManager;
   }

   public PlayerBoss getPlayer() {
      return player;
   }

   public IBoss getBoss() {
      return boss;
   }

   public ProjectileHandler getProjectileHandler() {
      return projectileHandler;
   }

   public PauseBoss getPauseOverlay() {
      return pauseOverlay;
   }

   public GameoverOverlay2 getGameOverOverlay() {
      return gameoverOverlay;
   }

   /** Currently only supports skipping the intro cutscene */
   public void skipIntroCutscene() {
      cutsceneManager.reset();
      player.setVisible(true);
      boss.setVisible(true);
      ambienceEnabled = true;
      musicEnabled = true;
      restartBossSong();
      audioPlayer.startAmbienceLoop(Audio.AMBIENCE_ROCKET_ENGINE);

   }

}
