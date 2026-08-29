package projectiles;

import entities.CollisionPixels;
import entities.Dimensions;
import static entities.CollisionPixels.CollisionAt;

import static projectiles.ProjectileFactory.TypeConstants.DRONE_PROJECTILE;

public class DroneProjectile extends BaseProjectile {

    public DroneProjectile(Dimensions dimensions, int xSpeed, int ySpeed) {
        super(dimensions, DRONE_PROJECTILE, 20, xSpeed, ySpeed);
        this.collisionPixels = new CollisionPixels(this, CollisionAt.CENTER);
    }
}
