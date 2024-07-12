package cutscenes.effects;

import java.awt.Color;
import java.awt.Graphics;

import game_events.GeneralEvent;
import game_events.SetRedLightEvent;
import gamestates.Gamestate;
import main_classes.Game;

public class RedLightEffect implements UpdatableEffect, DrawableEffect {
   private boolean active;
   private int redLightLvl = 0;
   private int redLightDir = 1;    // 1 = opp, -1 = ned

   @Override
   public void activate(GeneralEvent evt) {
      SetRedLightEvent redEvt = (SetRedLightEvent) evt;
      this.active = redEvt.active();
      if (active == true) {
         this.active = true;
      }
      else {
         this.active = false;
      }
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new SetRedLightEvent(false);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // Supports all gamestates
   }

   @Override
   public void update() {
      redLightLvl += (2 * redLightDir);
      if (redLightLvl < 0) {
         redLightLvl = 0;
         redLightDir *= -1;
      }
      else if (redLightLvl > 100) {
         redLightLvl = 100;
         redLightDir *= -1;
      }
   }

   @Override
   public void draw(Graphics g) {
      g.setColor(new Color(250, 0, 0, redLightLvl));
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void reset() {
      this.redLightLvl = 0;
      this.active = false;
   }
}
