package entities.exploring;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import entities.AnimationFrame;
import entities.Entity;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;

public class StandardNpc extends Entity implements NPC {
   public String name;
   private Rectangle2D.Float triggerBox;
   public int xDrawOffset; // = (spriteWidth - characterWidth) / 2
   public int yDrawOffset;
   private int startCutscene = 0;
   public boolean inForeground;
   private AnimationFrame animation;

   public StandardNpc(String name, Float hitbox, int xDrawOffset, int yDrawOffset,
         boolean inForeground) {
      super(hitbox);
      makeTriggerBox();
      this.name = name;
      this.xDrawOffset = xDrawOffset;
      this.yDrawOffset = yDrawOffset;
      this.inForeground = inForeground;
      this.animation = new AnimationFrame(
            0, 0,
            8, 1);
   }

   @Override
   public void update() {
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
   public void setSprite(boolean poseActive, int colIndex, int rowIndex) {
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
   public AnimationFrame getAnimation() {
      return this.animation;
   }
}
