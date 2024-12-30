package projectiles;

import static utils.Constants.Flying.TypeConstants.PLAYER_PROJECTILE;

import java.awt.geom.Rectangle2D;

import entities.Entity;

public class PlayerProjectile extends Entity implements Projectile {
    private int damage;
    private int xSpeed = 0;
    private int ySpeed = -10;
    private int[][] collisionPixels = new int[2][2];
    private boolean powerUp;
    private boolean active = true;
    private float powerupFactor = 1.35f; // Tried 1.5, that was maybe too much.

    public PlayerProjectile(Rectangle2D.Float hitbox, boolean powerUp, int dmg) {
        super(hitbox);
        this.powerUp = powerUp;
        this.damage = dmg;
    }

    @Override
    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
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
    public float getXSpeed() {
        return this.xSpeed;
    }

    @Override
    public float getYSpeed() {
        return this.ySpeed;
    }

    @Override
    public void updateCollisionPixels() {
        collisionPixels[0][0] = (int) (hitbox.x / 3); // x - øvre høyre hjørne
        collisionPixels[0][1] = (int) (hitbox.y / 3); // y - øvre høyre hjørne
        collisionPixels[1][0] = (int) ((hitbox.x + hitbox.width) / 3); // x - øvre venstre hjørne
        collisionPixels[1][1] = (int) (hitbox.y / 3); // y - øvre venstre hjørne
    }

    @Override
    /**
     * Returns a 2D-array of collisionPixels. In the inner layer: 0 = x, and 1 = y
     */
    public int[][] getCollisionPixels() {
        return this.collisionPixels;
    }

    public boolean isPowerUp() {
        return this.powerUp;
    }

    @Override
    public int getType() {
        return PLAYER_PROJECTILE;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }
}
