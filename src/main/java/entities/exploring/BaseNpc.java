package entities.exploring;

import entities.Dimensions;
import entities.MyRectangle;
import entities.animation.Animation;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;

public class BaseNpc extends MyRectangle implements NPC {
   protected String name;
   protected MyRectangle triggerBox;
   public int xDrawOffset;
   public int yDrawOffset;
   protected int startCutscene = 0;
   public boolean inForeground;
   protected Animation animation;

   public BaseNpc(String name, Dimensions hitbox, int xDrawOffset, int yDrawOffset,
         boolean inForeground) {
      super(hitbox);
      makeTriggerBox();
      this.name = name;
      this.xDrawOffset = xDrawOffset;
      this.yDrawOffset = yDrawOffset;
      this.inForeground = inForeground;
      this.animation = new Animation(0, 0, 0);
   }

   @Override
   public void update() {
      // Do nothing by default
   }

   private void makeTriggerBox() {
      this.triggerBox = new MyRectangle(
            x() - 8, y() - 8, width() + 16, height() + 16);
   }

   @Override
   public MyRectangle getHitbox() {
      return this;
   }

   @Override
   public MyRectangle getTriggerBox() {
      return this.triggerBox;
   }

   @Override
   public void setStartCutscene(int startCutscene) {
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
   public void setDir(Direction dir) {
      // Do nothing
   }

   @Override
   public void setPose(boolean poseActive, int colIndex, int rowIndex) {
      // Do nothing
   }

   @Override
   public void setAction(CharacterAction action) {
      // Do nothing
   }

   @Override
   public void adjustPos(float deltaX, float deltaY) {
      // Do nothing
   }

   @Override
   public boolean inForeground() {
      return this.inForeground;
   }

   @Override
   public float getXDrawOffset() {
      return this.xDrawOffset;
   }

   @Override
   public float getYDrawOffset() {
      return this.yDrawOffset;
   }

   @Override
   public Animation getAnimation() {
      return this.animation;
   }
}
