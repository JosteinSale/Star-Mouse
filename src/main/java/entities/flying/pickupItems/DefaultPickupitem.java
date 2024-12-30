package entities.flying.pickupItems;

import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;

public class DefaultPickupitem extends Entity implements PickupItem {
   private EntityInfo info;
   private float startY;
   private int aniIndex;
   private int aniTick;
   protected int aniTickPerFrame;
   protected int nrOfImages;
   private boolean active = true;

   public DefaultPickupitem(Rectangle2D.Float hitbox, EntityInfo info, int aniTickPerFrame, int nrOfImages) {
      super(hitbox);
      startY = hitbox.y;
      this.info = info;
      this.aniTickPerFrame = aniTickPerFrame;
      this.nrOfImages = nrOfImages;
   }

   public void update(float yLevelSpeed) {
      this.hitbox.y += yLevelSpeed;
      aniTick++;
      if (aniTick == aniTickPerFrame) {
         aniIndex++;
         aniTick = 0;
         if (aniIndex == nrOfImages) {
            aniIndex = 0;
         }
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

   @Override
   public int getAniIndex() {
      return this.aniIndex;
   }

   @Override
   public EntityInfo getDrawInfo() {
      return this.info;
   }

}
