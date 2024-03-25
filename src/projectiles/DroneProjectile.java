package projectiles;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;
import static utils.Constants.Flying.TypeConstants.DRONE_PROJECTILE;

public class DroneProjectile extends Entity implements Projectile {
    BufferedImage img;
    private int damage = 20;
    private int xSpeed = 0;
    private int ySpeed = 5;
    private int[][] collisionPixels = new int[1][2];

    private boolean active = true;

    public DroneProjectile(Rectangle2D.Float hitbox, BufferedImage img) {
        super(hitbox);
        this.img = img;
    }

    @Override
    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public int getXSpeed() {
        return this.xSpeed;
    }

    @Override
    public int getYSpeed() {
        return this.ySpeed;
    }

    @Override
    public void updateCollisionPixels() {
        collisionPixels[0][0] = (int) (hitbox.x + hitbox.width/2) / 3;    // x - ned i sentrum
        collisionPixels[0][1] = (int) (hitbox.y + hitbox.height) / 3;    // y - ned i sentrum

    }

    @Override
    /** Returns a 2D-array of collisionPixels. In the inner layer: 0 = x, and 1 = y.
     * The collisionPixels are already adjusted to 1/3 size.
    */
    public int[][] getCollisionPixels() {
        return this.collisionPixels;
    }

    @Override
    public void drawHitbox(Graphics g) {
        this.drawHitbox(g, 0, 0);
    }

    @Override
    public int getType() {
        return DRONE_PROJECTILE;
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
            (int) (32 * Game.SCALE),
            (int) (32 * Game.SCALE), null);
    }
}
