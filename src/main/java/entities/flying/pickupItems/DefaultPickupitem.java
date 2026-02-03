package entities.flying.pickupItems;

import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;
import entities.flying.StaticGlow;

public class DefaultPickupitem extends Entity implements PickupItem {
   private EntityInfo info;
   protected StaticGlow glow;
   private float startY;
   private int aniIndex;
   private int aniTick;
   protected int aniTickPerFrame;
   protected int nrOfImages;
   private boolean active = true;

   public DefaultPickupitem(Rectangle2D.Float hitbox, EntityInfo info, int aniTickPerFrame, int nrOfImages,
         StaticGlow glow) {
      super(hitbox);
      startY = hitbox.y;
      this.info = info;
      this.aniTickPerFrame = aniTickPerFrame;
      this.nrOfImages = nrOfImages;
      this.glow = glow;
   }

   public void update(float yLevelSpeed) {
      this.hitbox.y += yLevelSpeed;
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

   @Override
   public StaticGlow getGlow() {
      return this.glow;
   }

}
