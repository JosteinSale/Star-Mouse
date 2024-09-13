package cutscenes.effects;

import game_events.GeneralEvent;
import game_events.SetOverlayImageEvent;
import gamestates.Gamestate;
import main_classes.Game;

public class SetOverlayEffect implements DrawableEffect {
   private Game game;
   private boolean active;

   public SetOverlayEffect(Game game) {
      this.game = game;
   }

   @Override
   public void activate(GeneralEvent evt) {
      SetOverlayImageEvent imgEvt = (SetOverlayImageEvent) evt;
      this.active = imgEvt.active();
      if (active) {
         this.game.getView().getRenderCutscene().setOverlayImage(imgEvt.fileName(), imgEvt.scaleW(), imgEvt.ScaleH());
      }
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new SetOverlayImageEvent(active, null, 0f, 0f);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // Supports all gamestates
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void reset() {
      this.active = false;
   }

}
