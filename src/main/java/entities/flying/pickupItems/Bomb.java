package entities.flying.pickupItems;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;
import main_classes.Game;

public class Bomb extends Entity implements PickupItem {
    private EntityInfo info;
    private float startY;
    private int aniIndex;
    private int aniTick;
    private int aniTickPerFrame = 3;
    private boolean active = true;

    public Bomb(Rectangle2D.Float hitbox, EntityInfo info) {
        super(hitbox);
        startY = hitbox.y;
        this.info = info;
    }

    public void update(float yLevelSpeed) {
        this.hitbox.y += yLevelSpeed;
        aniTick++;
        if (aniTick == aniTickPerFrame) {
            aniIndex++;
            aniTick = 0;
            if (aniIndex > 1) {
                aniIndex = 0;
            }
        }
    }

    public void draw(Graphics g) {
        //drawHitbox(g, 0, 0);
        if (active) {
            g.drawImage(
            info.animation[0][aniIndex], 
            (int) ((hitbox.x - info.drawOffsetX) * Game.SCALE), 
            (int) ((hitbox.y - info.drawOffsetY) * Game.SCALE), 
            (int) (info.spriteW * 3 * Game.SCALE),
            (int) (info.spriteH * 3 * Game.SCALE), null);
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
        return info.typeConstant;
    }

    @Override
    public void resetTo(float y) {
        this.active = true;
        hitbox.y = startY + y;
    }
}
