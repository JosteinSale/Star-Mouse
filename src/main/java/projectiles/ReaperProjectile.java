package projectiles;

import entities.CollisionPixels;
import entities.Dimensions;

import static projectiles.ProjectileFactory.TypeConstants.REAPER_PROJECTILE;
import static entities.CollisionPixels.CollisionAt;

public class ReaperProjectile extends BaseProjectile {

   public ReaperProjectile(Dimensions dimensions) {
      super(dimensions, REAPER_PROJECTILE, 10, 0, 8);
      this.collisionPixels = new CollisionPixels(this, CollisionAt.BOTTOM_CENTER);
   }
}
