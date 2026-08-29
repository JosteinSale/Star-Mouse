package projectiles;

import entities.CollisionPixels;
import entities.Dimensions;

import static entities.CollisionPixels.CollisionAt;
import static projectiles.ProjectileFactory.TypeConstants.PLAYER_PROJECTILE;

/**
 * The PlayerProjectile has collission detection with the map at the top of its hitbox
 */
public class PlayerProjectile extends BaseProjectile {
    private boolean powerUp;
    private float powerupFactor = 1.35f; // Tried 1.5, that was maybe too much.

    public PlayerProjectile(Dimensions dimensions, boolean powerUp, int dmg) {
        super(dimensions, PLAYER_PROJECTILE, dmg, 0, -10);
        this.collisionPixels = new CollisionPixels(this, CollisionAt.TOP_TWO_CORNERS);
        this.powerUp = powerUp;
    }

    @Override
    public int getDamage() {
        if (powerUp) {
            return (int) (damage * powerupFactor);
        } else {
            return damage;
        }
    }

    public boolean isPowerUp() {
        return this.powerUp;
    }
}
