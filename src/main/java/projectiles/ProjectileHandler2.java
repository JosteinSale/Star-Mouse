package projectiles;

import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import entities.bossmode.PlayerBoss;
import entities.flying.enemies.EnemyManager;
import main_classes.Game;

public class ProjectileHandler2 extends ProjectileHandler {

   public ProjectileHandler2(Game game, AudioPlayer audioPlayer, PlayerBoss player, EnemyManager enemyManager) {
      super(game, audioPlayer, player, enemyManager);
   }

   @Override
   public void update(float yLevelOffset, float xLevelOffset, float fgCurSpeed) {
      checkPlayerShoot();
      updatePlayerShootTick();
      moveProjectiles();
      removeOffScreenProjectiles();
      checkProjectileCollisions(yLevelOffset, xLevelOffset);
      updateHits(fgCurSpeed);
      updateBombExplosions(fgCurSpeed);
   }

   @Override
   protected void checkProjectileCollisions(float yLevelOffset, float xLevelOffset) {
      // TODO - custom implementation
   }

   @Override
   protected void checkBombCollision(Projectile p, float yLevelOffset, float xLevelOffset) {
      // TODO - custom implementation
   }

   @Override
   protected void updateBombExplosions(float fgSpeed) {
      // TODO - custom implementation
   }

   @Override
   public void resetBombs(boolean toCheckPoint) {
      this.setBombs(game.getExploring().getProgressValues().getBombs());
   }

   @Override
   public void setClImg(BufferedImage clImg) {/*Do nothing*/}

   @Override
   public void checkPointReached() {/*Do nothing*/}

}
