package projectiles;

import static utils.Constants.Flying.TypeConstants.BOMB_PROJECTILE;

import java.awt.geom.Rectangle2D;
import entities.Entity;

public class BombProjectile extends Entity implements Projectile {
    private int damage = 100;
    private int xSpeed = 0;
    private int ySpeed = -7;
    private int[][] collisionPixels = new int[2][2];
    private boolean active = true;

    public BombProjectile(Rectangle2D.Float hitbox) {
        super(hitbox);
    }

    @Override
    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public int getDamage() {
        return damage;
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

    @Override
    public int getType() {
        return BOMB_PROJECTILE;
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
