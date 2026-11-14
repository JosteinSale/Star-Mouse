package entities.flying.enemies;

import static entities.animation.Animation.Type.LOOP_FORWARDS;
import static utils.Constants.Flying.LAZER_ACTIVATE_Y_POS;

import com.badlogic.gdx.math.Vector2;

import entities.Dimensions;
import entities.animation.PositionedAnimation;
import entities.flying.FlyEntityInfo;
import main_classes.Game;
import utils.HelpMethods;
import utils.Images;

public class LazerDrone extends BaseEnemy {
   private final double rotation;
   public final PositionedAnimation chargeAnimation;
   private final Vector2 normalizedVector;

   public LazerDrone(Dimensions hitbox, FlyEntityInfo info, int chargeDone, Vector2 directionVector) {
      super(hitbox, info);
      this.chargeDone = chargeDone;
      this.chargeTick = -1;
      this.normalizedVector = HelpMethods.NormalizeVector(directionVector);
      this.rotation = HelpMethods.CalculateAngle(normalizedVector);
      this.rotate(rotation + Math.PI);
      this.animationLength = 2;
      animation.setAniTickPerFrame(3);
      this.chargeAnimation = new PositionedAnimation(
            Images.RED_CHARGE,
            0, 0, 5,
            getChargeX(), getChargeY(), 1.5f, 1.5f);
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      chargeAnimation.xPos = getChargeX();
      chargeAnimation.yPos = getChargeY();
      if (isPreparingToShoot()) {
         chargeAnimation.play(LOOP_FORWARDS, 4);
      }
   }

   @Override
   protected void checkOnScreen(float levelYSpeed) {
      onScreen = (((y() + LAZER_ACTIVATE_Y_POS) > 0) &&
            (y() - height() * 0.2 < Game.GAME_DEFAULT_HEIGHT));
   }

   private float getChargeX() {
      return (float) (centerX() - 75) + (90 * normalizedVector.x);
   }

   private float getChargeY() {
      return (float) (centerY() - 75) + (90 * normalizedVector.y);
   }

   public boolean isPreparingToShoot() {
      return (chargeTick < chargeDone);
   }

   @Override
   public void onShoot() {
      // Do nothing
   }

   protected void resetCustomVars() {
      this.chargeTick = -1;
   }

}
