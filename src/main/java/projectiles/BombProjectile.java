package projectiles;

import static projectiles.ProjectileFactory.TypeConstants.BOMB_PROJECTILE;

import java.awt.geom.Rectangle2D;

public class BombProjectile extends BaseProjectile {

    public BombProjectile(Rectangle2D.Float hitbox) {
        super(hitbox, BOMB_PROJECTILE, 100, 0, -7);
        collisionPixels = new int[2][2];
    }

    @Override
    public void updateCollisionPixels() {
        collisionPixels[0][0] = (int) (hitbox.x / 3); // upper left corner x
        collisionPixels[0][1] = (int) (hitbox.y / 3); // upper left corner y
        collisionPixels[1][0] = (int) ((hitbox.x + hitbox.width) / 3); // upper right corner x
        collisionPixels[1][1] = (int) (hitbox.y / 3); // upper right corner y
    }
}
