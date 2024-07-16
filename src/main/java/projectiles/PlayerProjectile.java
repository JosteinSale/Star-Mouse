package projectiles;

import static utils.Constants.Flying.TypeConstants.PLAYER_PROJECTILE;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main_classes.Game;

public class PlayerProjectile extends Entity implements Projectile {
    BufferedImage img;
    private int damage;
    private int xSpeed = 0;
    private int ySpeed = -10;
    private int[][] collisionPixels = new int[2][2];
    private boolean powerUp;
    private boolean active = true;
    private float powerupFactor = 1.35f;  // Tried 1.5, that was maybe too much.

    public PlayerProjectile(Rectangle2D.Float hitbox, boolean powerUp, int dmg, BufferedImage img) {
        super(hitbox);
        this.powerUp = powerUp;
        this.damage = dmg;
        this.img = img;
    }

    @Override
    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public int getDamage() {
        if (powerUp) {
            return (int) (damage * powerupFactor);
        }
        else {
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
        collisionPixels[0][0] = (int) (hitbox.x / 3);    // x - øvre høyre hjørne
        collisionPixels[0][1] = (int) (hitbox.y / 3);    // y - øvre høyre hjørne
        collisionPixels[1][0] = (int) ((hitbox.x + hitbox.width) / 3);    // x - øvre venstre hjørne
        collisionPixels[1][1] = (int) (hitbox.y / 3);    // y - øvre venstre hjørne
    }

    @Override
    /** Returns a 2D-array of collisionPixels. In the inner layer: 0 = x, and 1 = y */
    public int[][] getCollisionPixels() {
        return this.collisionPixels;
    }

    public boolean isPowerUp() {
        return this.powerUp;
    }

    @Override
    public void drawHitbox(Graphics g) {
        this.drawHitbox(g, 0, 0);
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

    @Override
    public void draw(Graphics g) {
        g.drawImage(
            img, 
            (int) (hitbox.x * Game.SCALE), 
            (int) (hitbox.y * Game.SCALE), 
            (int) (img.getWidth() * Game.SCALE),
            (int) (img.getHeight() * Game.SCALE), null);
    }
}
