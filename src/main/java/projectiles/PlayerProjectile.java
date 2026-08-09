package projectiles;

import static projectiles.ProjectileFactory.TypeConstants.PLAYER_PROJECTILE;

import java.awt.geom.Rectangle2D;

public class PlayerProjectile extends BaseProjectile {
    private boolean powerUp;
    private float powerupFactor = 1.35f; // Tried 1.5, that was maybe too much.

    public PlayerProjectile(Rectangle2D.Float hitbox, boolean powerUp, int dmg) {
        super(hitbox, PLAYER_PROJECTILE, dmg, 0, -10);
        collisionPixels = new int[2][2];
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

    @Override
    public void updateCollisionPixels() {
        collisionPixels[0][0] = (int) (hitbox.x / 3); // upper left corner x
        collisionPixels[0][1] = (int) (hitbox.y / 3); // upper left corner y
        collisionPixels[1][0] = (int) ((hitbox.x + hitbox.width) / 3); // upper right corner x
        collisionPixels[1][1] = (int) (hitbox.y / 3); // upper right corner y
    }

    public boolean isPowerUp() {
        return this.powerUp;
    }
}
