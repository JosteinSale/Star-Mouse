package projectiles;

import static utils.Constants.Flying.TypeConstants.REAPER_PROJECTILE;

import java.awt.geom.Rectangle2D;

public class ReaperProjectile extends BaseProjectile {

   public ReaperProjectile(Rectangle2D.Float hitbox) {
      super(hitbox, REAPER_PROJECTILE, 10, 0, 8);
      collisionPixels = new int[1][2];
   }
}
