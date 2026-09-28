package entities.flying.enemies;

import com.badlogic.gdx.math.Vector2;

import entities.Dimensions;
import entities.animation.Animation;
import entities.animation.PositionedAnimation;
import entities.flying.FlyEntityInfo;
import utils.HelpMethods;

public class LazerDrone extends BaseEnemy {
   private final double rotation;
   // private final PositionedAnimation lazerChargeAnimation;

   public LazerDrone(Dimensions hitbox, FlyEntityInfo info, Vector2 directionVector) {
      super(hitbox, info);
      Vector2 normalizedVector = HelpMethods.NormalizeVector(directionVector); // TODO - do we need to normalize it?
      this.rotation = HelpMethods.CalculateAngle(normalizedVector);
      this.rotate(rotation + Math.PI);
      this.animationLength = 2;
      animation = new Animation(IDLE, 0, 3);
      allAnimations.clear();
      allAnimations.add(animation);
      // this.lazerChargeAnimation = new PositionedAnimation(IDLE, HP, hitboxWidth,
      // hitboxHeight, RIGHT, LEFT);
   }
}
