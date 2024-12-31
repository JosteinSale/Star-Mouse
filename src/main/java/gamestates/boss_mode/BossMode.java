package gamestates.boss_mode;

import static utils.Constants.Exploring.Cutscenes.AUTOMATIC;
import static utils.HelpMethods.GetCutscenes;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.Cutscene;
import cutscenes.cutsceneManagers.CutsceneManagerBoss;
import entities.bossmode.PlayerBoss;
import entities.bossmode.rudinger1.Rudinger1;
import entities.bossmode.AnimatedComponentFactory;
import entities.bossmode.IBoss;
import entities.flying.enemies.EnemyManager;
import game_events.ClearObjectsEvent;
import game_events.EventHandler;
import game_events.FadeOutLoopEvent;
import game_events.GeneralEvent;
import game_events.GoToFlyingEvent;
import game_events.ObjectMoveEvent;
import game_events.PlaySFXEvent;
import game_events.SetBossVisibleEvent;
import game_events.SetVisibleEvent;
import game_events.StartAmbienceEvent;
import game_events.StartSongEvent;
import game_events.StopLoopsEvent;
import game_events.TextBoxEvent;
import gamestates.Gamestate;
import gamestates.State;
import gamestates.Statemethods;
import main_classes.Game;
import projectiles.ProjectileHandler2;
import ui.GameoverOverlay2;
import ui.PauseBoss;
import ui.TextboxManager;
import utils.Constants.Audio;
import utils.ImageContainer;
import utils.ResourceLoader;

public class BossMode extends State implements Statemethods {
   private PlayerBoss player;
   private ProjectileHandler2 projectileHandler;
   private AnimatedComponentFactory animationFactory;
   private AudioPlayer audioPlayer;
   private CutsceneManagerBoss cutsceneManager;
   private MapManager3 mapManager;
   private IBoss boss;
   private int bossNr;

   private PauseBoss pauseOverlay;
   private GameoverOverlay2 gameoverOverlay;
   public boolean shouldAmbiencePlay = false;
   public boolean shouldMusicPlay = false;
   private boolean pause = false;
   private boolean gameOver = false;

   public BossMode(Game game) {
      super(game);
      this.audioPlayer = game.getAudioPlayer();
      initClasses();
   }

   private void initClasses() {
      Rectangle2D.Float playerHitbox = new Rectangle2D.Float(500f, 600f, 50f, 50f);
      this.player = new PlayerBoss(game, playerHitbox);
      this.projectileHandler = new ProjectileHandler2(
            game,
            game.getAudioPlayer(),
            player,
            new EnemyManager(null, null, null));
      this.animationFactory = new AnimatedComponentFactory(new ImageContainer());
      this.pauseOverlay = new PauseBoss(game, this, game.getOptionsMenu());
      this.gameoverOverlay = new GameoverOverlay2(game, this);
      this.mapManager = new MapManager3();

      EventHandler eventHandler = new EventHandler();
      eventHandler.addEventListener(this::doReaction);
      TextboxManager textboxManager = game.getTextboxManager();
      this.cutsceneManager = new CutsceneManagerBoss(game, eventHandler, textboxManager, Gamestate.BOSS_MODE);
   }

   public void doReaction(GeneralEvent event) {
      if (event instanceof TextBoxEvent tbEvt) {
         this.cutsceneManager.activateTextbox(tbEvt);
      } else if (event instanceof StartSongEvent evt) {
         this.shouldMusicPlay = true;
         this.audioPlayer.startSong(evt.index(), 0, true);
      } else if (event instanceof StartAmbienceEvent evt) {
         this.shouldAmbiencePlay = true;
         audioPlayer.startAmbienceLoop(evt.index());
      } else if (event instanceof StopLoopsEvent) {
         this.shouldAmbiencePlay = false;
         this.shouldMusicPlay = false;
         this.audioPlayer.stopAllLoops();
      } else if (event instanceof FadeOutLoopEvent) {
         this.shouldAmbiencePlay = false;
         this.shouldMusicPlay = false;
         audioPlayer.fadeOutAllLoops();
      } else if (event instanceof PlaySFXEvent evt) {
         audioPlayer.playSFX(evt.SFXIndex());
      } else if (event instanceof GoToFlyingEvent evt) {
         this.goToFlying(evt.lvl());
      } else if (event instanceof SetBossVisibleEvent evt) {
         this.boss.setVisible(evt.visible());
      } else if (event instanceof ObjectMoveEvent evt) {
         this.cutsceneManager.moveObject(evt);
      } else if (event instanceof ClearObjectsEvent) {
         this.cutsceneManager.clearObjects();
      } else if (event instanceof SetVisibleEvent evt) {
         this.player.setVisible(evt.visible());
      } else {
         this.cutsceneManager.activateEffect(event);
      }
   }

   /** A wrapper method which abstracts away unecessary arguments */
   private void startCutscene(int i) {
      cutsceneManager.startCutscene(0, AUTOMATIC, i);
   }

   /** Should be called from the boss-defeated-cutscene. */
   private void goToFlying(int lvl) {
      // Bombs are first transfered to flying,
      // and later (when Flying :: exitFinishedLevel is called) from flying to
      // progressValues.
      game.getFlying().setBombsWhenBossIsFinished(projectileHandler.getBombsAtEndOfLevel());
      this.player.reset();
      this.pause = false;
      this.gameOver = false;
      this.flushImages();
      Gamestate.state = Gamestate.FLYING;
   }

   /**
    * Loads the boss and prepares all objects in BossMode.
    * Finally it starts the opening cutscene.
    */
   public void loadNewBoss(int bossNr) {
      this.bossNr = bossNr;
      loadBoss(bossNr);
      setPlayerBossParts();
      loadBackground(bossNr);
      loadCutscenes(bossNr);
      projectileHandler.setBoss(bossNr, boss);
      this.startCutscene(0);
   }

   private void setPlayerBossParts() {
      this.player.setBoss(this.boss.getBossParts());
   }

   private void loadCutscenes(int bossNr) {
      this.cutsceneManager.clear();
      List<String> cutsceneData = ResourceLoader.getBossCutsceneData(bossNr);
      ArrayList<ArrayList<Cutscene>> cutscenes = GetCutscenes(cutsceneData);
      for (ArrayList<Cutscene> cutscenesForTrigger : cutscenes) {
         cutsceneManager.addCutscene(cutscenesForTrigger);
      }
   }

   private void loadBackground(int bossNr) {
      this.mapManager.loadMap(bossNr);
   }

   private void loadBoss(int bossNr) {
      switch (bossNr) {
         case 1:
            this.boss = new Rudinger1(this.player, this.projectileHandler, this.animationFactory);
            return;
         default:
            throw new IllegalArgumentException("No boss available for bossNr: " + bossNr);
      }
   }

   @Override
   public void update() {
      checkPause();
      if (pause) {
         this.pauseOverlay.update();
      } else if (gameOver) {
         this.gameoverOverlay.update();
      } else if (cutsceneManager.isActive()) {
         this.player.updateOnlyFlame();
         cutsceneManager.handleKeyBoardInputs();
         cutsceneManager.update();
      } else {
         this.player.update(0, 0);
         this.boss.update();
         this.projectileHandler.update(boss.getXPos(), boss.getYPos(), 0);
         checkBossDeath();
      }
   }

   private void checkBossDeath() {
      if (boss.isDead()) {
         this.killBoss();
      }
   }

   private void checkPause() {
      if (game.pauseIsPressed) {
         game.pauseIsPressed = false;
         game.getAudioPlayer().stopAllLoops();
         this.flipPause();
      }
   }

   @Override
   public void draw(Graphics g) {
      this.mapManager.drawMap(g);
      // this.player.draw(g);
      this.boss.draw(g);
      // this.projectileHandler.draw(g);
      // this.cutsceneManager.draw(g);
      if (gameOver) {
         this.gameoverOverlay.draw(g);
      } else if (pause) {
         this.pauseOverlay.draw(g);
      }
   }

   /** Needed for the pauseOverlay */
   public void flipPause() {
      this.pause = !pause;
   }

   /** Resets the player, current boss and projectileHandler. */
   public void resetBossMode() {
      this.projectileHandler.reset();
      this.projectileHandler.resetBombs(false);
      this.cutsceneManager.reset();
      this.player.reset();
      this.boss.reset();
      this.gameoverOverlay.reset();
      this.pause = false;
      this.gameOver = false;
      this.shouldAmbiencePlay = false;
      this.shouldMusicPlay = false;
   }

   /** Is called from the gameOverOverlay */
   public void restartBossSong() {
      int songNr = switch (bossNr) {
         case 1 -> Audio.SONG_BOSS1;
         default -> 0;
      };
      game.getAudioPlayer().startSong(songNr, 0f, true);
   }

   /** Is called from the pauseOverlay */
   public void skipBossMode() {
      this.resetBossMode();
      Gamestate.state = Gamestate.FLYING;
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
      this.shouldAmbiencePlay = false;
      this.shouldMusicPlay = false;
      this.projectileHandler.reset();
      this.startCutscene(1);
   }

   private void flushImages() {
      mapManager.flush();
      this.boss.flush();
   }

}
