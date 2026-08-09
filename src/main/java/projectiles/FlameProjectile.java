package projectiles;

import static utils.Constants.Flying.TypeConstants.FLAME_PROJECTILE;

import java.awt.geom.Rectangle2D;

public class FlameProjectile extends BaseProjectile {

   public FlameProjectile(Rectangle2D.Float hitbox) {
      super(hitbox, FLAME_PROJECTILE, 20, 0, 4);
      collisionPixels = new int[1][2];
   }
}
