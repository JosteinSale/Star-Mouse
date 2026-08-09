package projectiles;

import static utils.Constants.Flying.TypeConstants.DRONE_PROJECTILE;

import java.awt.geom.Rectangle2D;

public class DroneProjectile extends BaseProjectile {

    /**
     * This constructor can be used in case of standard drones.
     * The x- and y-speed will have default values.
     *
     * @param hitbox
     * @param img
     */
    public DroneProjectile(Rectangle2D.Float hitbox) {
        super(hitbox, DRONE_PROJECTILE, 20, 0, 5);
        collisionPixels = new int[1][2];
    }

    /** This constructor can be used if a custom x- and y-speed is needed. */
    public DroneProjectile(Rectangle2D.Float hitbox, int xSpeed, int ySpeed) {
        super(hitbox, DRONE_PROJECTILE, 20, xSpeed, ySpeed);
        collisionPixels = new int[1][2];
    }
}
