package entities.exploring;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public interface NPC {

    public void update();

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset);

    public void drawHitbox(Graphics g, int xLevelOffset, int yLevelOffset);

    public Rectangle2D.Float getHitbox();

    public Rectangle2D.Float getTriggerBox();

    public void setNewStartingCutscene(int startCutscene);

    public int getStartCutscene();

    public String getName();

    public void setDir(int dir);

    public void setSprite(boolean poseActive, int colIndex, int rowIndex);

    public void setAction(int action);

    public void adjustPos(float deltaX, float deltaY);

    public boolean inForeground();

    public void flushImages();
}