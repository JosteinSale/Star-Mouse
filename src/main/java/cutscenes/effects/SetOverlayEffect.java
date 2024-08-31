package cutscenes.effects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game_events.GeneralEvent;
import game_events.SetOverlayImageEvent;
import gamestates.Gamestate;
import main_classes.Game;
import utils.ResourceLoader;

public class SetOverlayEffect implements DrawableEffect {
   private boolean active;
   private BufferedImage overlayImage;
   private float scaleW;
   private float scaleH;

   @Override
   public void activate(GeneralEvent evt) {
      SetOverlayImageEvent imgEvt = (SetOverlayImageEvent) evt;
      this.active = imgEvt.active();
      if (active) {
         this.overlayImage = ResourceLoader.getCutsceneImage(imgEvt.fileName());
         this.scaleW = imgEvt.scaleW();
         this.scaleH = imgEvt.ScaleH();
      } else {
         this.overlayImage.flush();
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
   public void draw(Graphics g) {
      if (overlayImage == null) {
         /*
          * If the overlayImage is big, it might take an extra frame to load it.
          * In such case it will be null when we try to draw it -> return.
          */
         return;
      }
      g.drawImage(
            overlayImage, 0, 0,
            (int) (overlayImage.getWidth() * scaleW * Game.SCALE),
            (int) (overlayImage.getHeight() * scaleH * Game.SCALE), null);
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
