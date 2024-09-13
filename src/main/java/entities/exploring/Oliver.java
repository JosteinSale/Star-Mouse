package entities.exploring;

import static utils.Constants.Exploring.DirectionConstants.*;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import entities.Entity;

public class Oliver extends Entity implements NPC {
   private String name = "Oliver";
   private Rectangle2D.Float triggerBox;
   private int xDrawOffset = 80;
   private int yDrawOffset = 30;
   private int startCutscene = 0;
   private boolean inForeground;

   private int oliverAction = STANDING;
   private boolean poseActive = false;
   private int oliverDirection;

   private int aniTick = 0;
   private int aniTickPerFrame = 8; // Antall ticks per gang animasjonen oppdateres
   private int aniIndex = 0;

   public Oliver(Float hitbox, int direction, boolean inForeground) {
      super(hitbox);
      this.oliverDirection = direction;
      this.inForeground = inForeground;
      makeTriggerBox();
   }

   private void makeTriggerBox() {
      this.triggerBox = new Rectangle2D.Float(
            hitbox.x - 8, hitbox.y, hitbox.width + 16, hitbox.height + 8);
   }

   @Override
   public void update() {
      updateAniTick();
   }

   private void updateAniTick() {
      if (poseActive) {
         return;
      }
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniIndex++;
         aniTick = 0;
      }
      if (aniIndex >= GetSpriteAmount(oliverAction)) {
         aniIndex = 0;
      }
      if (oliverAction == STANDING) {
         aniIndex = oliverDirection;
      }
   }

   public void adjustPos(float deltaX, float deltaY) {
      this.hitbox.x += deltaX;
      this.hitbox.y += deltaY;
      this.triggerBox.x += deltaX;
      this.triggerBox.y += deltaY;
   }

   @Override
   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   @Override
   public Rectangle2D.Float getTriggerBox() {
      return this.triggerBox;
   }

   public int getStartCutscene() {
      return this.startCutscene;
   }

   @Override
   public void setNewStartingCutscene(int startCutscene) {
      this.startCutscene = startCutscene;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void setDir(int dir) {
      this.oliverDirection = dir;
   }

   @Override
   public void setSprite(boolean poseActive, int colIndex, int rowIndex) {
      if (poseActive == true) {
         this.poseActive = true;
         this.oliverAction = rowIndex;
         this.aniIndex = colIndex;
      } else {
         this.poseActive = false;
         this.aniIndex = 0;
         this.aniTick = 0;
      }
   }

   @Override
   public boolean inForeground() {
      return this.inForeground;
   }

   public void setAction(int action) {
      this.oliverAction = action;
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
      return this.oliverAction;
   }

   @Override
   public int getAniIndex() {
      return this.aniIndex;
   }

}
