package cutscenes.effects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cutscenes.FellowShip;
import game_events.FellowShipEvent;
import game_events.GeneralEvent;
import gamestates.Gamestate;
import utils.LoadSave;

public class FellowShipEffect implements UpdatableEffect, DrawableEffect {
   private boolean active;
   private BufferedImage shipImg;
   private ArrayList<FellowShip> fellowShips;

   public FellowShipEffect() {
      this.shipImg = LoadSave.getFlyImageSprite(LoadSave.FELLOWSHIP_SPRITES);
      this.fellowShips = new ArrayList<>();
   }

   @Override
   public void activate(GeneralEvent evt) {
      FellowShipEvent fEvt = (FellowShipEvent) evt;
      int[] xPositions = fEvt.xPositions();
      int[] yPositions = fEvt.yPositions();
      int[] takeOffTimers = fEvt.takeOffTimers();

      for (int i = 0; i < xPositions.length; i++) {
         FellowShip ship = new FellowShip(xPositions[i], yPositions[i], takeOffTimers[i], shipImg);
         fellowShips.add(ship);
      }
      this.active = true;
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new FellowShipEvent(null, null, null);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.FLYING);
   }

   @Override
   public void reset() {
      this.active = false;
      this.fellowShips.clear();
   }

   @Override
   public void update() {
      boolean allShipsDone = true;
      for (FellowShip ship : fellowShips) {
         ship.update();
         if (ship.isOnScreen()) {
            allShipsDone = false;
         }
      }
      if (allShipsDone) {
         this.active = false;
      }
   }

   @Override
   public void draw(Graphics g) {
      for (FellowShip ship : fellowShips) {
         if (ship.isOnScreen()) {
            ship.draw(g);
         }
      }
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public boolean shouldAdvance() {
      return false;  // We don't need to call advance for this effect
   }

}
