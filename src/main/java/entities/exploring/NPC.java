package entities.exploring;

import java.awt.geom.Rectangle2D;

import entities.AnimationFrame;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;

public interface NPC {

   public void update();

   public Rectangle2D.Float getHitbox();

   public Rectangle2D.Float getTriggerBox();

   public void setStartCutscene(int startCutscene);

   public int getStartCutscene();

   public String getName();

   public void setDir(Direction dir);

   public void setSprite(boolean poseActive, int colIndex, int rowIndex);

   public void setAction(CharacterAction action);

   public void adjustPos(float deltaX, float deltaY);

   public boolean inForeground();

   public float getXDrawOffset();

   public float getYDrawOffset();

   public AnimationFrame getAnimation();
}