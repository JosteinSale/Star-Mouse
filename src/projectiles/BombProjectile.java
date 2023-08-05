package projectiles;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;
import static utils.Constants.Flying.TypeConstants.BOMB_PROJECTILE;
import static utils.Constants.Flying.Sprites.BOMB_SPRITE_SIZE;

public class BombProjectile extends Entity implements Projectile {
    BufferedImage img;
    private int damage = 100;
    private int xSpeed = 0;
    private int ySpeed = -7;
    private int[][] collisionPixels = new int[2][2];
    private boolean active = true;

    public BombProjectile(Rectangle2D.Float hitbox, BufferedImage img) {
        super(hitbox);
        this.img = img;
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
    public int getXSpeed() {
        return this.xSpeed;
    }

    @Override
    public int getYSpeed() {
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

    @Override
    public void drawHitbox(Graphics g) {
        this.drawHitbox(g, 0, 0);
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

    @Override
    public void draw(Graphics g) {
        g.drawImage(
            img, 
            (int) ((hitbox.x - 20) * Game.SCALE), 
            (int) ((hitbox.y - 18) * Game.SCALE), 
            (int) (BOMB_SPRITE_SIZE * 2.5f * Game.SCALE),
            (int) (BOMB_SPRITE_SIZE * 2.5f * Game.SCALE), null);
    }
    
}
