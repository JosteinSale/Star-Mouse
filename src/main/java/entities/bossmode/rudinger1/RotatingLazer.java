package entities.bossmode.rudinger1;

import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;
import java.util.Arrays;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.AnimationInfo;
import entities.bossmode.DefaultBossPart;
import main_classes.Game;
import utils.Images;

public class RotatingLazer extends DefaultBossPart {
   private Double rotationSpeed = 0.013;
   private Double initialRotation;
   private boolean isCharging = false;
   private int chargeTick = 0;
   private int chargeDuration = 140;
   private int visualWarningPoint = 100;

   public AnimatedComponent chargeAnimation; // Can be null

   private final int SHOOTING_ANIM = 0;
   private final int VISUALWARNING_ANIM = 1;

   private int aniTick = 0;
   private int aniSpeed = 3;
   // aniIndex inherited from super class

   public RotatingLazer(
         Float hitbox, String spriteSheet, int aniRows, int aniCols, int spriteW, int spriteH,
         double startRotation, boolean shouldDrawCharge) {
      super(hitbox, spriteSheet, aniRows, aniCols, spriteW, spriteH);
      this.initialRotation = startRotation;
      this.updatePosition(0, 0, initialRotation);

      if (shouldDrawCharge) {
         this.chargeAnimation = new AnimatedComponent(
               Images.LAZER_CHARGE_SPRITE1,
               100, 100, 1, 5,
               new ArrayList<>(Arrays.asList(
                     new AnimationInfo("CHARGE", 0, 5, 3, 0, false))),
               Game.GAME_DEFAULT_WIDTH / 2 - 150,
               Game.GAME_DEFAULT_HEIGHT / 2 - 170);
      }
   }

   @Override
   public void startAttack() {
      this.isCharging = true;
   }

   @Override
   public void updateBehavior() {
      if (isCharging) {
         updateChargingFase();
      } else { // The active fase
         this.updatePosition(0, 0, rotationSpeed);
      }
      this.updateAnimations();
   }

   private void updateChargingFase() {
      // The last 20 ticks of the charging, a visualWarning is given.
      chargeTick++;
      if (chargeTick >= visualWarningPoint && chargeTick < chargeDuration) {
         this.rotatedImgVisible = true;
         this.animAction = VISUALWARNING_ANIM;
      } else if (chargeTick >= chargeDuration) {
         this.animAction = SHOOTING_ANIM;
         this.isCharging = false;
         this.collisionEnabled = true;
      }
   }

   @Override
   public void updateAnimations() {
      // Lazer animation
      aniTick++;
      if (aniTick > aniSpeed) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex > 2) {
            aniIndex = 0;
         }
      }
      // Charging animation
      if (chargeAnimation != null) {
         chargeAnimation.updateAnimations();
      }
      ;
   }

   @Override
   public void onPlayerCollision() {
      // No behavior
   }

   @Override
   public void onTeleportHit() {
      // No behavior
   }

   @Override
   public boolean isCharging() {
      return isCharging;
   }

   @Override
   public void finishAttack() {
      this.collisionEnabled = false;
      this.rotatedImgVisible = false;
      this.chargeTick = 0;
      this.setPosition( // Resets to start position
            (int) nonRotatedHitbox.getCenterX(),
            (int) nonRotatedHitbox.getCenterY(),
            initialRotation);
   }
}
