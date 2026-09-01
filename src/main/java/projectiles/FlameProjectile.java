package projectiles;

import entities.CollisionPixels;
import entities.Dimensions;

import static entities.CollisionPixels.CollisionAt;

import static projectiles.ProjectileFactory.TypeConstants.FLAME_PROJECTILE;

public class FlameProjectile extends BaseProjectile {

   public FlameProjectile(Dimensions dimensions) {
      super(dimensions, FLAME_PROJECTILE, 20, 0, 4);
      this.collisionPixels = new CollisionPixels(this, CollisionAt.BOTTOM_CENTER);
   }
}
