package cutscenes.effects;

import entities.exploring.PlayerExp;
import game_events.GeneralEvent;
import game_events.PlayerWalkEvent;
import gamestates.Gamestate;

public class PlayerWalkEffect implements UpdatableEffect, AdvancableEffect {
   private boolean active;
   private boolean shouldAdvance;
   private PlayerExp player;
   private int walkDuration;
   private float xSpeed;
   private float ySpeed;

   public PlayerWalkEffect(PlayerExp player) {
      this.player = player;
   }

   @Override
   public void activate(GeneralEvent evt) {
      PlayerWalkEvent playEvt = (PlayerWalkEvent) evt;
      this.active = true;
      this.walkDuration = playEvt.walkDuration();
      this.player.setAction(playEvt.sheetRowIndex()); 

      float xDistance = playEvt.targetX() - this.player.getHitbox().x;
      float yDistance = playEvt.targetY() - this.player.getHitbox().y;
      this.xSpeed = xDistance / walkDuration;
      this.ySpeed = yDistance / walkDuration;
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new PlayerWalkEvent(0, 0, 0, 0);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.EXPLORING);
   }

   @Override
   public void update() {
      this.player.adjustPos(xSpeed, ySpeed);
      this.walkDuration--;
      if (walkDuration == 0) {
         this.active = false;
         this.shouldAdvance = true;
         this.player.resetAll();
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
