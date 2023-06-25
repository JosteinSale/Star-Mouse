package entities.exploring;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import main.Game;
import ui.InfoBox;

public class InteractableObject extends InteractableEntity {
    private InfoBox infoBox;
    // private BufferedImage objectImg;
    // private boolean isVisible;
    // private boolean isActive;
    // private Action action;   (action can be performed when object is interacted with)

    public InteractableObject(Float hitbox, String text) {
        super(hitbox);
        infoBox = new InfoBox(text);
    }

    public void drawTextBox(Graphics g) {
        this.infoBox.draw(g);
    }

    public void drawHitbox(Graphics g) {
        g.drawRect(
            (int) (hitbox.x * Game.SCALE),
            (int) (hitbox.y * Game.SCALE),
            (int) (hitbox.width * Game.SCALE),
            (int) (hitbox.height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }  
}
