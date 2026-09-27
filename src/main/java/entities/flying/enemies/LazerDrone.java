package entities.flying.enemies;

import com.badlogic.gdx.math.Vector2;

import cutscenes.effects.SimpleAnimation;
import entities.AnimationFrame;
import entities.Dimensions;
import entities.flying.EntityInfo;
import utils.HelpMethods;

public class LazerDrone extends BaseEnemy {
   private final double rotation;
   private final SimpleAnimation lazerChargeAnimation;

   public LazerDrone(Dimensions hitbox, EntityInfo info, Vector2 directionVector) {
      super(hitbox, info);
      Vector2 normalizedVector = HelpMethods.NormalizeVector(directionVector); // TODO - do we need to normalize it?
      this.rotation = HelpMethods.CalculateAngle(normalizedVector);
      this.rotate(rotation + Math.PI);
      animation = new AnimationFrame(IDLE, 0, 3, 2);
      allAnimations.clear();
      allAnimations.add(animation);
      this.lazerChargeAnimation = new SimpleAnimation(IDLE, HP, hitboxWidth, hitboxHeight, RIGHT, LEFT);
   }
}
