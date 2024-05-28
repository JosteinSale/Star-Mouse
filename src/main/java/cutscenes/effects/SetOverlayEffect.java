package cutscenes.effects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game_events.GeneralEvent;
import game_events.SetOverlayImageEvent;
import gamestates.Gamestate;
import main_classes.Game;
import utils.LoadSave;

public class SetOverlayEffect implements DrawableEffect {
   private boolean active;
   private BufferedImage overlayImage;

   @Override
   public void activate(GeneralEvent evt) {
      SetOverlayImageEvent imgEvt = (SetOverlayImageEvent) evt;
      this.active = imgEvt.active();
      if (active) {
         // TODO - make global folder for overlayImages?
         this.overlayImage = LoadSave.getExpImageBackground(imgEvt.fileName());
      }
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new SetOverlayImageEvent(active, null);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.EXPLORING);
   }

   @Override
   public void draw(Graphics g) {
      g.drawImage(
         overlayImage, 0, 0, 
         (int) (overlayImage.getWidth() * 3 * Game.SCALE), 
         (int) (overlayImage.getHeight() * 3 * Game.SCALE), null);
   }

   @Override
   public boolean isActive() {
      return this.active;
   }
   
}
