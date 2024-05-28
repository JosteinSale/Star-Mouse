package cutscenes.effects;

import game_events.GeneralEvent;
import game_events.ScreenShakeEvent;
import gamestates.Gamestate;
import gamestates.exploring.Area;

public class ScreenShakeEffect implements UpdatableEffect {
   private boolean active;
   private boolean shouldAdvance;
   private int duration;
   private Area area;
   private int xLevelOffset;
   private int yLevelOffset;
   private int shakeSize = 10;
   private int shakeDirection = 1;  // 1 = høyre/opp, -1 = venstre/ned

   public ScreenShakeEffect(Area area) {
      this.area = area;
   }

   @Override
   public void activate(GeneralEvent evt) {
      ScreenShakeEvent shakeEvt = (ScreenShakeEvent) evt;
      this.active = true;
      this.duration = shakeEvt.duration();
      this.xLevelOffset = area.getXLevelOffset();
      this.yLevelOffset = area.getYLevelOffset();
      adjustOffsetToAvoidGoingOffScreen();
   }

   private void adjustOffsetToAvoidGoingOffScreen() {
      if (xLevelOffset < shakeSize) {
         xLevelOffset += shakeSize;
      }
      if (yLevelOffset < shakeSize) {
         yLevelOffset += shakeSize;
      }
      else if ((xLevelOffset + shakeSize ) > area.getMaxLevelOffsetX()) {
         xLevelOffset -= shakeSize;
      }
      else if ((yLevelOffset + shakeSize ) > area.getMaxLevelOffsetY()) {
         yLevelOffset -= shakeSize;
      }
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new ScreenShakeEvent(0);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.EXPLORING);
   }

   @Override
   public void update() {
      this.duration--;
      if (this.duration == 0) {
         this.active = false;
         this.shouldAdvance = true;
      }
      else {
         updateScreenShake();
      }
   }

   private void updateScreenShake() {
      if (duration % 6 > 2) {
         int newXoffset = xLevelOffset + (shakeSize * shakeDirection);
         this.area.setXLevelOffset(newXoffset);
     }
     else {
         int newYoffset = yLevelOffset + (shakeSize * shakeDirection);
         this.area.setYLevelOffset(newYoffset);
         this.shakeDirection *= -1;
     }
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void reset() {
      this.shouldAdvance = false;
      this.active = false;
   }

   @Override
   public boolean shouldAdvance() {
      return this.shouldAdvance;
   }
   
}
