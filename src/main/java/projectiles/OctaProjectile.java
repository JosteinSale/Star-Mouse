package projectiles;

import static utils.Constants.Flying.TypeConstants.OCTA_PROJECTILE;

import java.awt.geom.Rectangle2D;

public class OctaProjectile extends BaseProjectile {

    public OctaProjectile(Rectangle2D.Float hitbox, int xSpeed, int ySpeed) {
        super(hitbox, OCTA_PROJECTILE, 10, xSpeed, ySpeed);
        collisionPixels = new int[1][2];
    }

    @Override
    public void updateCollisionPixels() {
        collisionPixels[0][0] = (int) (hitbox.x + hitbox.width / 2) / 3; // x - i midten
        collisionPixels[0][1] = (int) (hitbox.y + hitbox.height / 2) / 3; // y - i midten
    }
}
