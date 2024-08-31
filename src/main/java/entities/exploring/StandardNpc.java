package entities.exploring;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.Entity;
import main_classes.Game;
import utils.ResourceLoader;

public class StandardNpc extends Entity implements NPC {
    private String name;
    private BufferedImage sprite;
    private Rectangle2D.Float triggerBox;
    private int xDrawOffset; // = (spriteWidth - characterWidth) / 2
    private int yDrawOffset;
    private int spriteWidth;
    private int spriteHeight;
    private int startCutscene = 0;
    private boolean inForeground;

    public StandardNpc(String name, Float hitbox, String fileName, int xDrawOffset, int yDrawOffset,
            boolean inForeground) {
        super(hitbox);
        makeTriggerBox();
        this.name = name;
        this.sprite = ResourceLoader.getExpImageSprite(fileName);
        this.xDrawOffset = xDrawOffset;
        this.yDrawOffset = yDrawOffset;
        this.inForeground = inForeground;
        this.spriteWidth = (int) (sprite.getWidth() * 3 * Game.SCALE);
        this.spriteHeight = (int) (sprite.getHeight() * 3 * Game.SCALE);
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(
                sprite,
                (int) ((hitbox.x - xDrawOffset - xLevelOffset) * Game.SCALE),
                (int) ((hitbox.y - yDrawOffset - yLevelOffset) * Game.SCALE),
                spriteWidth, spriteHeight,
                null);

        // TriggerBox (hitboxes can be drawn from Area :: draw() )
        // g.setColor(Color.CYAN);
        // g.drawRect(
        // (int) ((triggerBox.x - xLevelOffset) * Game.SCALE),
        // (int) ((triggerBox.y - yLevelOffset) * Game.SCALE),
        // (int) (triggerBox.width * Game.SCALE),
        // (int) (triggerBox.height * Game.SCALE));
    }

    private void makeTriggerBox() {
        this.triggerBox = new Rectangle2D.Float(
                hitbox.x - 8, hitbox.y - 8, hitbox.width + 16, hitbox.height + 16);
    }

    @Override
    public Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public Rectangle2D.Float getTriggerBox() {
        return this.triggerBox;
    }

    @Override
    public void setNewStartingCutscene(int startCutscene) {
        this.startCutscene = startCutscene;
    }

    @Override
    public int getStartCutscene() {
        return this.startCutscene;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setDir(int dir) {
        // Do nothing
    }

    @Override
    public void setSprite(boolean poseActive, int colIndex, int rowIndex) {
        // Do nothing
    }

    @Override
    public void setAction(int action) {
        // Do nothing
    }

    @Override
    public void adjustPos(float deltaX, float deltaY) {
        // Do nothing
    }

    public boolean inForeground() {
        return this.inForeground;
    }

    @Override
    public void flushImages() {
        this.sprite.flush();
    }
}
