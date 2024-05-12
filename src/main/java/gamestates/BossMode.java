package gamestates;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import entities.bossmode.PlayerBoss;
import entities.bossmode.rudinger1.Rudinger1;
import entities.bossmode.AnimationFactory;
import entities.bossmode.IBoss;
import entities.flying.enemies.EnemyManager;
import main_classes.Game;
import projectiles.ProjectileHandler2;
import ui.PauseBoss;

public class BossMode extends State implements Statemethods {
   private PlayerBoss player;
   private ProjectileHandler2 projectileHandler;
   private AnimationFactory animationFactory;
   private IBoss boss;

   private PauseBoss pauseOverlay;
   private boolean pause = false;

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
   }

   public void loadNewBoss(int bossNr) {
      loadBoss(bossNr);
      setPlayerBossParts();
      loadBackground(bossNr);
      loadCutscenes(bossNr);
      projectileHandler.setBoss(bossNr, boss.getBossParts());
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
      this.projectileHandler.draw(g);
      this.boss.draw(g);
      if (pause) {
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
      this.player.reset();
      this.boss.reset();
      this.pause = false;
   }

   public void skipBoss() {
      this.boss.skipBoss();
   }
   
}
