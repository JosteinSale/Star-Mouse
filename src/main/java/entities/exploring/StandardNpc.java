package entities.exploring;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import entities.Entity;

public class StandardNpc extends Entity implements NPC {
   public String name;
   private Rectangle2D.Float triggerBox;
   public int xDrawOffset; // = (spriteWidth - characterWidth) / 2
   public int yDrawOffset;
   private int startCutscene = 0;
   public boolean inForeground;

   public StandardNpc(String name, Float hitbox, int xDrawOffset, int yDrawOffset,
         boolean inForeground) {
      super(hitbox);
      makeTriggerBox();
      this.name = name;
      this.xDrawOffset = xDrawOffset;
      this.yDrawOffset = yDrawOffset;
      this.inForeground = inForeground;
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
   public float getXDrawOffset() {
      return this.xDrawOffset;
   }

   @Override
   public float getYDrawOffset() {
      return this.yDrawOffset;
   }

   @Override
   public int getAction() {
      throw new IllegalArgumentException("Standard NPC's don't have actions");
   }

   @Override
   public int getAniIndex() {
      throw new IllegalArgumentException("Standard NPC's don't have animations");
   }
}
