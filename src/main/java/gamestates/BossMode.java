package gamestates;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import entities.bossmode.PlayerBoss;
import entities.bossmode.rudinger1.Rudinger1;
import entities.bossmode.AnimationFactory;
import entities.bossmode.IBoss;
import entities.flying.enemies.EnemyManager;
import main_classes.Game;
import projectiles.ProjectileHandler2;
import ui.GameoverOverlay2;
import ui.PauseBoss;
import utils.Constants.Audio;

public class BossMode extends State implements Statemethods {
   private PlayerBoss player;
   private ProjectileHandler2 projectileHandler;
   private AnimationFactory animationFactory;
   private IBoss boss;
   private int bossNr;

   private PauseBoss pauseOverlay;
   private GameoverOverlay2 gameoverOverlay;
   private boolean pause = false;
   private boolean gameOver = false;

   private Image scaledBgImg;

   public BossMode(Game game) {
      super(game);
      initClasses();
   }

   private void initClasses() {
      Rectangle2D.Float playerHitbox = new Rectangle2D.Float(500f, 600f, 50f, 50f);
      this.player = new PlayerBoss(game, playerHitbox);
      this.projectileHandler = new ProjectileHandler2(
         game, 
         game.getAudioPlayer(), 
         player, 
         new EnemyManager(null, null));
      this.animationFactory = new AnimationFactory();
      this.pauseOverlay = new PauseBoss(game, this, game.getOptionsMenu());
      this.gameoverOverlay = new GameoverOverlay2(game, this);
   }

   public void loadNewBoss(int bossNr) {
      this.bossNr = bossNr;
      loadBoss(bossNr);
      setPlayerBossParts();
      loadBackground(bossNr);
      loadCutscenes(bossNr);
      projectileHandler.setBoss(bossNr, boss);
   }

   private void setPlayerBossParts() {
      this.player.setBoss(this.boss.getBossParts());
   }

   private void loadCutscenes(int bossNr) {

   }

   private void loadBackground(int bossNr) {
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
      this.checkPause();
      if (pause) {
         this.pauseOverlay.update();
      }
      else if (gameOver) {
         this.gameoverOverlay.update();
      }
      else {
         this.player.update(0, 0);
         this.boss.update();
         this.projectileHandler.update(boss.getXPos(), boss.getYPos(), 0);
      }
   }

   private void checkPause() {
      if (game.pauseIsPressed) {
         game.pauseIsPressed = false;
         game.getAudioPlayer().flipAudioOnOff();
         this.flipPause();
      }
   }

   @Override
   public void draw(Graphics g) {
      this.player.draw(g);
      this.boss.draw(g);
      this.projectileHandler.draw(g);
      if (gameOver) {
         this.gameoverOverlay.draw(g);
      }
      else if (pause) {
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
      this.player.reset();
      this.boss.reset();
      this.gameoverOverlay.reset();
      this.pause = false;
      this.gameOver = false;
   }

   /** Is called from the gameOverOverlay */
   public void restartBossSong() {
      int songNr = switch (bossNr) {
         case 1 -> Audio.SONG_BOSS1;
         default -> 0;
      };
      game.getAudioPlayer().startSongLoop(songNr, 0f);
   }

   public void skipBoss() {
      this.boss.skipBoss();
   }

   public void killPlayer() {
      gameOver = true;
      player.setVisible(false);
      gameoverOverlay.setPlayerPos(player.getHitbox().x, player.getHitbox().y);
      game.getAudioPlayer().stopAllLoops();
      game.getAudioPlayer().startAmbienceLoop(Audio.AMBIENCE_SILENCE);
      game.getAudioPlayer().playSFX(Audio.SFX_DEATH);
    }
   
}
