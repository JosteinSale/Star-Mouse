package entities.flying.pickupItems;

import entities.Dimensions;
import entities.MyRectangle;
import entities.flying.EntityInfo;
import entities.flying.StaticGlow;

public class DefaultPickupitem extends MyRectangle implements PickupItem {
   private EntityInfo info;
   protected StaticGlow glow;
   private float startY;
   private int aniIndex;
   private int aniTick;
   protected int aniTickPerFrame;
   protected int nrOfImages;
   private boolean active = true;

   public DefaultPickupitem(Dimensions hitbox, EntityInfo info, int aniTickPerFrame, int nrOfImages,
         StaticGlow glow) {
      super(hitbox);
      startY = hitbox.y();
      this.info = info;
      this.aniTickPerFrame = aniTickPerFrame;
      this.nrOfImages = nrOfImages;
      this.glow = glow;
   }

   public void update(float yLevelSpeed) {
      move(0, yLevelSpeed);
      aniTick++;
      setGlowPos();
      if (aniTick == aniTickPerFrame) {
         aniIndex++;
         aniTick = 0;
         if (aniIndex == nrOfImages) {
            aniIndex = 0;
         }
      }
   }

   protected void setGlowPos() {
      // Default implementation does nothing. Subclasses may override.
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public MyRectangle getHitbox() {
      return this;
   }

   public int getType() {
      return info.typeConstant;
   }

   @Override
   public void resetTo(float y) {
      this.active = true;
      setY(startY + y);
   }

   @Override
   public int getAniIndex() {
      return this.aniIndex;
   }

   @Override
   public EntityInfo getDrawInfo() {
      return this.info;
   }

   @Override
   public StaticGlow getGlow() {
      return this.glow;
   }

}
