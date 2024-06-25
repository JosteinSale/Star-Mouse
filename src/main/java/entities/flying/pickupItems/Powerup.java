package entities.flying.pickupItems;

import static utils.Constants.Flying.DrawOffsetConstants.POWERUP_OFFSET_X;
import static utils.Constants.Flying.DrawOffsetConstants.POWERUP_OFFSET_Y;
import static utils.Constants.Flying.SpriteSizes.POWERUP_SPRITE_SIZE;
import static utils.Constants.Flying.TypeConstants.POWERUP;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.Entity;
import main_classes.Game;
import utils.ResourceLoader;

public class Powerup extends Entity implements PickupItem {
    private BufferedImage[] animations;
    private float startY;
    private int aniIndex;
    private int aniTick;
    private int aniTickPerFrame = 5;
    private boolean active = true;

    public Powerup(Float hitbox) {
        super(hitbox);
        startY = hitbox.y;
        loadAnimations();
    }

    private void loadAnimations() {
        BufferedImage img = ResourceLoader.getFlyImageSprite(ResourceLoader.POWERUP_SPRITE);
        this.animations = new BufferedImage[7];
        for (int i = 0; i < animations.length; i++) {
            animations[i] = img.getSubimage(
                i * POWERUP_SPRITE_SIZE, 0, POWERUP_SPRITE_SIZE, POWERUP_SPRITE_SIZE);
        }
    }

    public void update(float yLevelSpeed) {
        this.hitbox.y += yLevelSpeed;
        aniTick++;
        if (aniTick == aniTickPerFrame) {
            aniIndex++;
            aniTick = 0;
            if (aniIndex > 6) {
                aniIndex = 0;
            }
        }
    }

    public void draw(Graphics g) {
        //drawHitbox(g, 0, 0);
        if (active) {
            g.drawImage(
            animations[aniIndex], 
            (int) ((hitbox.x - POWERUP_OFFSET_X) * Game.SCALE), 
            (int) ((hitbox.y - POWERUP_OFFSET_Y) * Game.SCALE), 
            (int) (POWERUP_SPRITE_SIZE * 3 * Game.SCALE),
            (int) (POWERUP_SPRITE_SIZE * 3 * Game.SCALE), null);
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    public int getType() {
        return POWERUP;
    }

    @Override
    public void resetTo(float y) {
        this.active = true;
        hitbox.y = startY + y;
    }
}
