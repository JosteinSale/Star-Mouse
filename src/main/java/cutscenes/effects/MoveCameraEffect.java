package cutscenes.effects;

import game_events.GeneralEvent;
import game_events.MoveCameraEvent;
import gamestates.Gamestate;
import gamestates.exploring.Area;

/** An effect that moves the camera a given amount of pixels, over a given
 * duration. This de-attatches the camera from being locked on to the player.
 * Thus, it's important to re-attatch the camera (from a cutscene) once the
 * cutscene is over.
 */
public class MoveCameraEffect implements UpdatableEffect, AdvancableEffect {
   private boolean active;
   private boolean shouldAdvance;
   private int duration;
   private float xSpeed;
   private float ySpeed;
   private Area area;

   public MoveCameraEffect(Area area) {
      this.area = area;
   }

   @Override
   public void activate(GeneralEvent evt) {
      MoveCameraEvent moveEvt = (MoveCameraEvent) evt;
      this.active = true;
      this.calculateSpeed(moveEvt);
      this.area.deAttatchCamera();
   }

   private void calculateSpeed(MoveCameraEvent evt) {
      this.duration = evt.duration();
      this.xSpeed = evt.deltaX() / duration;
      this.ySpeed = evt.deltaY() / duration;
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new MoveCameraEvent(0, 0, 0);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.EXPLORING);
   }

   @Override
   public void reset() {
      this.active = false;
      this.shouldAdvance = false;
   }

   @Override
   public boolean shouldAdvance() {
      return shouldAdvance;
   }

   @Override
   public void update() {
      this.area.setXLevelOffset((int) (area.getXLevelOffset() + xSpeed));
      this.area.setYLevelOffset((int) (area.getYLevelOffset() + ySpeed));
      duration--;
      if (duration == 0) {
         this.active = false;
         this.shouldAdvance = true;
      }
   }


   @Override
   public boolean isActive() {
      return active;
   }
   
}
