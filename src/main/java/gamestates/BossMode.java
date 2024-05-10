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

public class BossMode extends State implements Statemethods {
   private PlayerBoss player;
   private ProjectileHandler2 projectileHandler;
   private AnimationFactory animationFactory;
   private IBoss boss;

   public BossMode(Game game) {
      super(game);
      initClasses();
      loadNewBoss(1);   // Temporary call in constructor, for testing porposes.
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
      this.player.update(0, 0);
      this.boss.update();
      this.projectileHandler.update(boss.getXPos(), boss.getYPos(), 0);
   }

   @Override
   public void draw(Graphics g) {
      this.player.draw(g);
      this.projectileHandler.draw(g);
      this.boss.draw(g);
   }
   
}
