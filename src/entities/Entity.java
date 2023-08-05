package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class Entity {
    protected Rectangle2D.Float hitbox;

    public Entity(Rectangle2D.Float hitbox) {
        this.hitbox = hitbox;
    }

    public void drawHitbox(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawRect(
            (int) ((hitbox.x - xLevelOffset) * Game.SCALE),
            (int) ((hitbox.y - yLevelOffset) * Game.SCALE),
            (int) (hitbox.width * Game.SCALE),
            (int) (hitbox.height * Game.SCALE));
    }
}
