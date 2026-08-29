package projectiles;

import entities.CollisionPixels;
import entities.Dimensions;

import static entities.CollisionPixels.CollisionAt;

import static projectiles.ProjectileFactory.TypeConstants.BOMB_PROJECTILE;

/**
 * The BombProjectile has collission detection with the map at the top of its hitbox.
 */
public class BombProjectile extends BaseProjectile {

    public BombProjectile(Dimensions dimensions) {
        super(dimensions, BOMB_PROJECTILE, 100, 0, -7);
        this.collisionPixels = new CollisionPixels(this, CollisionAt.TOP_TWO_CORNERS);
    }
}
