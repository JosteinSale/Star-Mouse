package projectiles;

import static projectiles.ProjectileFactory.TypeConstants.DRONE_PROJECTILE;

import java.awt.geom.Rectangle2D;

public class DroneProjectile extends BaseProjectile {

    public DroneProjectile(Rectangle2D.Float hitbox, int xSpeed, int ySpeed) {
        super(hitbox, DRONE_PROJECTILE, 20, xSpeed, ySpeed);
        collisionPixels = new int[1][2];
    }
}
