package cutscenes.cutsceneManagers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import cutscenes.Cutscene;
import cutscenes.effects.AdvancableEffect;
import cutscenes.effects.CutsceneEffect;
import cutscenes.effects.DrawableEffect;
import cutscenes.effects.UpdatableEffect;

import static utils.Constants.Exploring.Cutscenes.*;

import game_events.EventHandler;
import game_events.GeneralEvent;
import game_events.TextBoxEvent;
import gamestates.Gamestate;
import main_classes.Game;
import ui.ITextboxManager;

/**
 * A non-gamestate-specific cutsceneManager that can be used as a base
 * for other, gamestate-specific cutsceneManagers.
 */
public class DefaultCutsceneManager {
   // Base objects all cutsceneManagers need
   protected Gamestate state;  // Each cutsceneManager will be associated with one state
   protected Game game;
   protected ITextboxManager textBoxManager;
   protected EventHandler eventHandler;

   // CutsceneEffects organized by properties
	protected HashMap<String, CutsceneEffect> allEffects;    // Used to add and activate effects
	protected ArrayList<UpdatableEffect> updateableEffects;  // Will be updated
	protected ArrayList<DrawableEffect> drawableEffects;     // Will be drawn

   // Cutscenes: are organized by which kind of object triggers them
   private ArrayList<ArrayList<Cutscene>> objectCutscenes;
   private ArrayList<ArrayList<Cutscene>> doorCutscenes;
   private ArrayList<ArrayList<Cutscene>> npcCutscenes;
   protected ArrayList<ArrayList<Cutscene>> automaticCutscenes;

   protected boolean active = false;
   protected boolean canAdvance = true;
   protected boolean cutsceneJump = false;

   protected int triggerType;     // object | npc | door | automatic. TODO - Use ENUM?
   protected int elementNr;       // e.g. door #2
   protected int cutsceneIndex;   // e.g. cutscene #1 (for door #2)

   public DefaultCutsceneManager(Game game, EventHandler eventHandler, ITextboxManager textboxManager, Gamestate state) {
      this.game = game;
      this.eventHandler = eventHandler;
      this.textBoxManager = textboxManager;
      this.state = state;

      this.allEffects = new HashMap<>();
      this.updateableEffects = new ArrayList<>();
      this.drawableEffects = new ArrayList<>();

      this.objectCutscenes = new ArrayList<>();
      this.doorCutscenes = new ArrayList<>();
      this.npcCutscenes = new ArrayList<>();
      this.automaticCutscenes = new ArrayList<>();
   }

   public void addCutscene(ArrayList<Cutscene> cutscenesForElement) {
      int trigger = cutscenesForElement.get(0).getTrigger();
      getCutsceneListForTrigger(trigger).add(cutscenesForElement);
   }

   protected ArrayList<ArrayList<Cutscene>> getCutsceneListForTrigger(int trigger) {
      switch (trigger) {
         case OBJECT: return objectCutscenes;
         case DOOR: return doorCutscenes;
         case NPC: return npcCutscenes;
         case AUTOMATIC: return automaticCutscenes;
         default: throw new IllegalArgumentException("No cutscene-list available for " + trigger);
      }
   }

   /** Call this method from subclass when adding the effects. 
    * Note that effects added first will be drawn at the bottom layer, 
    * and vice versa. */
	protected void addEffect(CutsceneEffect e) {
		if (!e.supportsGamestate(this.state)) {
			throw new IllegalArgumentException(e + " does not support gamestate " + this.state);
      }
		if (e instanceof UpdatableEffect) {this.updateableEffects.add((UpdatableEffect)e);}
		if (e instanceof DrawableEffect) {this.drawableEffects.add((DrawableEffect)e);}
		allEffects.put(e.getAssociatedEvent().getClass().toString(), e);
   }

   /**
    * This method is always called when a cutscene is initiated
    * (except standard fades). Returns false if the cutscene has been played
    * before, and is not resettable.
    */
   public boolean startCutscene(int elementNr, int triggerType, int cutsceneIndex) {
      this.cutsceneIndex = cutsceneIndex;
      this.triggerType = triggerType;
      this.elementNr = elementNr;
      Cutscene cutscene = getCutsceneListForTrigger(triggerType).get(elementNr).get(cutsceneIndex);
      if (cutscene.hasPlayed() && !cutscene.canReset()) {
         return false;
      } else {
         this.active = true;
         ArrayList<GeneralEvent> firstSequence = cutscene.getFirstSequence();
         for (GeneralEvent event : firstSequence) {
            eventHandler.addEvent(event);
         }
         eventHandler.triggerEvents();
      }
      return true;
   }

   /** Can be called from the doReaction-method in the associated gamestate.
    * First it checks if the effect is included in the CutsceneManager.
    * If no, it throws an error.
    * Then it activates the effect. If it's updatable, canAdvance is set to false.
    * Note that textBoxes are not effects, and are not handled in this method.
    * @param evt
    */
   public void activateEffect(GeneralEvent evt) {
		if (!allEffects.containsKey(evt.getClass().toString())) {
         throw new IllegalArgumentException("No effect associated with " + evt.getClass().toString());
      } 
		CutsceneEffect effect = allEffects.get(evt.getClass().toString());
		effect.activate(evt);
      
		if (effect instanceof UpdatableEffect) {
			this.canAdvance = false;
      }
   }

   /** Activates the textBoxManager with the given evt. Depending on what the
    * textBoxManager supports, it could be an infoBox, infoChoice, 
    * small- or bigDialogueBox. The player can advance at any time.
    */
   public void activateTextbox(TextBoxEvent evt) {
      this.textBoxManager.activateTextbox(evt);
   }

   /** Updates all active effects and textBoxes. */
   public void update() {
      textBoxManager.update();
		for (UpdatableEffect effect : updateableEffects) {
			if (effect.isActive()) {
				effect.update();
				if (effect instanceof AdvancableEffect) {
               checkAdvance((AdvancableEffect) effect);
            } 
         }
      }
      if (cutsceneJump) {  // TODO - can this be done differently?
         this.cutsceneJump = false;
         startCutscene(elementNr, triggerType, cutsceneIndex);
      }
   }

   private void checkAdvance(AdvancableEffect effect) {
      if (effect.shouldAdvance()) {
         effect.reset();
         this.canAdvance = true;
			this.advance();
      }
   }

   /** Advances the cutscene if canAdvance. 
    * If dialogue is in the midst of appearing, it forwards
    * the dialogue and returns. Else, it goes to the next cutscene sequence.
    */
   public void advance() {
      if (!canAdvance) {
         return;
      }
      else if (textBoxManager.isDialogueAppearing()) {
         textBoxManager.forwardDialogue();
         return;
      }
      else {
         this.goToNextSequence();
      }
   }

   /** If the cutscene is at an end, it resets the cutscene
    * (if it's resettable) and deactivates the cutsceneManager. 
    * Also it resets the textBoxManager. */
   private void goToNextSequence() {
      textBoxManager.resetBooleans();
      Cutscene cutscene = getCutsceneListForTrigger(triggerType).get(elementNr).get(cutsceneIndex);
      if (cutscene.hasMoreSequences()) {
         ArrayList<GeneralEvent> nextSequence = cutscene.getNextSequence();
         for (GeneralEvent event : nextSequence) {
            eventHandler.addEvent(event);
         }
         eventHandler.triggerEvents();
      } else {
         this.resetCutscene(cutscene);
         this.active = false;
      }
   }

   private void resetCutscene(Cutscene cutscene) {
      cutscene.setPlayed();
      if (cutscene.canReset()) {
         cutscene.reset();
      }
   }

   /** Starting new cutscenes cannot be done directly by 
     * triggering events in the eventHandler.
     * Doing this seems to get the program stuck in a loop where the same 
     * event is called multiple times.
     * Thus this workaround method wich uses the update-method instead
    */
    public void jumpToCutscene(int opt) {
      this.advance();
      cutsceneIndex += opt;
      this.cutsceneJump = true;
   }

   /** Draws all drawable effects / textBoxes that are currently active.
    * OBS: effects are drawn in the order they are added to the cutsceneManager.
    * Effects added first will be drawn at the bottom layer, and so forth.
    */
   public void draw(Graphics g) {
      for (DrawableEffect effect : drawableEffects) {
         if (effect.isActive()) {
            effect.draw(g);
         }
      }
      textBoxManager.draw(g);
   }

   public boolean isActive() {
      return this.active;
   }

   public void clearCutscenes() {
      this.objectCutscenes.clear();
      this.doorCutscenes.clear();
      this.npcCutscenes.clear();
      this.automaticCutscenes.clear();
   }

   /** Usually the effects will inactivate themselves automatically, or it's done
    * manually in a cutscene. But sometimes (e.g. when exiting a finished flying level),
    * some effects may not be reset properly. Then you can call this method manually
    * to reset all effects. 
    *
    * NOTE: some effects may continue to be updated and drawn after all the cutscene
    * sequences have been executed. E.g. the FellowShipEffect. Therefore, you shouldn't
    * call this method in the advance()-method, as it will abort the effect too early.
    */
   protected void resetEffectsManually() {
      for (CutsceneEffect effect : allEffects.values()) {
         effect.reset();
      }
   }

   /** Resets the cutsceneManager and all associated cutscene effects. */
   public void reset() {
      active = false; 
      canAdvance = true;
      this.textBoxManager.resetBooleans();
      this.resetEffectsManually();
   }
}
