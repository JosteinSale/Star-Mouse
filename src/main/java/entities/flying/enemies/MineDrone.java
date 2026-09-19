package entities.flying.enemies;

import entities.AnimationFrame;
import entities.Dimensions;
import entities.MyRectangle;
import entities.flying.EntityInfo;
import entities.flying.PlayerFly;
import main_classes.Game;

public class MineDrone extends BaseEnemy {
   private EnemyManager enemyManager;
   private PlayerFly player;
   private MyRectangle triggerZone;
   private final int triggerZoneRadius = 150;
   private final int explosionAt = 40;
   private final int damageAmount = 40;
   private boolean chargingUpToExplosion = false;

   // Actions: correspond to a row in the spritesheet.
   protected int IDLE = 0;
   protected int TAKING_DAMAGE = 1;
   protected int CHARGING_UP = 2;

   public MineDrone(Dimensions hitbox, EntityInfo info, EnemyManager enemyManager, PlayerFly player) {
      super(hitbox, info, 1000, null);
      this.enemyManager = enemyManager;
      this.player = player;
      animation = new AnimationFrame(IDLE, 0, 10, 8);
      allAnimations.clear();
      allAnimations.add(animation);
      this.rotate(Math.PI / 4);
      this.constructTriggerZone();
   }

   private void constructTriggerZone() {
      Dimensions dim = new Dimensions(
            this.centerX() - triggerZoneRadius,
            this.centerY() - triggerZoneRadius,
            triggerZoneRadius * 2,
            triggerZoneRadius * 2);
      this.triggerZone = new MyRectangle(dim);
      triggerZone.rotate(Math.PI / 4);
   }

   @Override
   public double getAnimationRotation() {
      // The animation already has the correct rotation - the hitbox doesn't.
      // So the hitbox and animation rotation needs to be different.
      return 0.0;
   }

   @Override
   protected void checkOnScreen(float levelYSpeed) {
      onScreen = (((this.centerY() + triggerZoneRadius * 1.5) > 0) &&
            ((this.centerY() - triggerZoneRadius * 1.5) < Game.GAME_DEFAULT_HEIGHT));
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      // 1. If dead, do nothing
      if (dead) {
         return;
      }
      // 2. Update triggerZone position
      this.triggerZone.setPosition(
            this.centerX() - triggerZoneRadius,
            this.centerY() - triggerZoneRadius);

      // 3. Update idle behavior
      if (!chargingUpToExplosion) {
         if (player.intersects(triggerZone)) {
            enemyManager.playMineChargeSfx();
            this.chargingUpToExplosion = true;
            this.animation.setAction(CHARGING_UP);
            this.animation.setCol(0);
            this.animation.setAniTickPerFrame(6); // Faster animation
         }
      }
   }

   @Override
   protected void updateChargeTick() {
      // Handles explosion logic
      if (chargingUpToExplosion && !dead) {
         chargeTick++;
         if (chargeTick == explosionAt) {
            this.HP = 0;
            this.dead = true;
            boolean hitsPlayer = player.intersects(triggerZone);
            enemyManager.handleMineExplosion(this, hitsPlayer, damageAmount);
         }
      }
   }

   @Override
   public void takeDamage(int damage) {
      this.HP -= damage;
      if (!chargingUpToExplosion) {
         setAnimationsToDamage();
      }
      damageTick = 4;
      if (HP <= 0) {
         dead = true;
      }
   }

   @Override
   protected void resetCustomVars() {
      this.chargingUpToExplosion = false;
   }
}
