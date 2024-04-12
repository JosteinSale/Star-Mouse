package gamestates;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.bossmode.PlayerBoss;
import entities.bossmode.BossPart;
import entities.flying.enemies.EnemyManager;
import main_classes.Game;
import projectiles.ProjectileHandler2;
import utils.LoadSave;

public class BossMode extends State implements Statemethods {
   private PlayerBoss player;
   private ProjectileHandler2 projectileHandler;
   private BossPart boss;   // Temporary for testing

   public BossMode(Game game) {
      super(game);
      initClasses();
      loadNewBoss(1);   // Temporary call in constructor, for testing porposes.
   }

   private void initClasses() {
      Rectangle2D.Float playerHitbox = new Rectangle2D.Float(500f, 600f, 50f, 50f);
      this.player = new PlayerBoss(game, playerHitbox);
      this.projectileHandler = new ProjectileHandler2(game, game.getAudioPlayer(), player, new EnemyManager(null, null));
   }

   public void loadNewBoss(int bossNr) {
      loadBoss(bossNr);
      loadBackground(bossNr);
      loadCutscenes(bossNr);
   }

   private void loadCutscenes(int bossNr) {
   }

   private void loadBackground(int bossNr) {
   }

   private void loadBoss(int bossNr) {
      BufferedImage partImg = LoadSave.getFlyImageSprite(LoadSave.BOSS_TEST);
      this.boss = new BossPart(
         new Rectangle2D.Float(300f, 300f, partImg.getWidth(), partImg.getHeight()),
         partImg
         );
   }

   @Override
   public void update() {
      this.player.update(0, 0);
      this.projectileHandler.update(0, 0, 0);
      this.boss.update(player.getHitbox());
   }

   @Override
   public void draw(Graphics g) {
      this.player.draw(g);
      this.projectileHandler.draw(g);
      this.boss.draw(g);
   }
   
}
