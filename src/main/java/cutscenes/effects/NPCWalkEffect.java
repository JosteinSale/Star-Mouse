package cutscenes.effects;

import entities.exploring.NpcManager;
import game_events.GeneralEvent;
import game_events.NPCWalkEvent;
import gamestates.Gamestate;

import static utils.Constants.Exploring.DirectionConstants.STANDING;


// TODO - Inlcude support for having many NPC's walk at the same time?
// Could have a list of xSpeed, ySpeed, and a list of active npc-indexes
public class NPCWalkEffect implements UpdatableEffect, AdvancableEffect {
   private NpcManager npcManager;
   private boolean active;
   private boolean shouldAdvance;
   private int walkDuration;
   private float xSpeed;
   private float ySpeed;
   private int npcIndex;

   public NPCWalkEffect(NpcManager npcManager) {
      this.npcManager = npcManager;
   }

   @Override
   public void activate(GeneralEvent evt) {
      NPCWalkEvent npcEvt = (NPCWalkEvent) evt;
      this.active = true;
      this.npcIndex = npcEvt.npcIndex();
      this.walkDuration = npcEvt.walkDuration();
      this.npcManager.getNpc(npcIndex).setAction(npcEvt.sheetRowIndex()); 

      float xDistance = npcEvt.targetX() - this.npcManager.getNpc(npcIndex).getHitbox().x;
      float yDistance = npcEvt.targetY() - this.npcManager.getNpc(npcIndex).getHitbox().y;
      this.xSpeed = xDistance / walkDuration;
      this.ySpeed = yDistance / walkDuration;
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new NPCWalkEvent(0, 0, 0, 0, 0);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.EXPLORING);
   }

   @Override
   public void update() {
      this.npcManager.getNpc(npcIndex).adjustPos(xSpeed, ySpeed);
      this.walkDuration--;
      if (walkDuration == 0) {
         this.active = false;
         this.shouldAdvance = true;
         this.npcManager.getNpc(npcIndex).setAction(STANDING);
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

