package entities.flying.pickupItems;

import static entities.animation.Animation.Type.LOOP_FORWARDS;

import entities.Dimensions;
import entities.MyRectangle;
import entities.animation.Animation;
import entities.flying.FlyEntityInfo;
import entities.flying.StaticGlow;

public class DefaultPickupitem extends MyRectangle implements PickupItem {
   private FlyEntityInfo info;
   protected StaticGlow glow;
   private float startY;
   protected int nrOfImages;
   private Animation animation;
   private boolean active = true;

   public DefaultPickupitem(Dimensions hitbox, FlyEntityInfo info, int aniTickPerFrame, int nrOfImages,
         StaticGlow glow) {
      super(hitbox);
      startY = hitbox.y;
      animation = new Animation(0, 0, aniTickPerFrame);
      this.nrOfImages = nrOfImages;
      this.info = info;
      this.glow = glow;
   }

   public void update(float yLevelSpeed) {
      move(0, yLevelSpeed);
      setGlowPos();
      animation.play(LOOP_FORWARDS, nrOfImages - 1);
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
      return animation.getCol();
   }

   @Override
   public FlyEntityInfo getDrawInfo() {
      return this.info;
   }

   @Override
   public StaticGlow getGlow() {
      return this.glow;
   }

}
