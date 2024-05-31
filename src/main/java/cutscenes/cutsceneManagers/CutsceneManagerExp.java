package cutscenes.cutsceneManagers;

import cutscenes.effects.FadeEffect;
import cutscenes.effects.FillScreenEffect;
import cutscenes.effects.NPCWalkEffect;
import cutscenes.effects.NumberDisplayEffect;
import cutscenes.effects.PlayerWalkEffect;
import cutscenes.effects.RedLightEffect;
import cutscenes.effects.ScreenShakeEffect;
import cutscenes.effects.SetOverlayEffect;
import cutscenes.effects.WaitEffect;
import entities.exploring.NpcManager;
import entities.exploring.PlayerExp;
import game_events.EventHandler;
import game_events.FadeEvent;
import gamestates.Gamestate;
import gamestates.exploring.Area;
import main_classes.Game;
import ui.ITextboxManager;
import ui.NumberDisplay;

public class CutsceneManagerExp extends DefaultCutsceneManager {
   private FadeEffect fadeEffect;         // Is needed to check isFadeActive from area
   private ScreenShakeEffect shakeEffect; // Is needed to check isShakeActive from area
   private Area area;
   private PlayerExp player;
   private NpcManager npcManager;
   private NumberDisplay numberDisplay;

   public CutsceneManagerExp(Gamestate state, Game game, Area area, EventHandler eventHandler, ITextboxManager textboxManager, PlayerExp player, NpcManager npcManager) {
      super(game, eventHandler, textboxManager, state);
      this.area = area;
      this.player = player;
      this.npcManager = npcManager;
      this.numberDisplay = new NumberDisplay(game);
      this.addCutsceneEffects();
   }

   /* OBS: drawable effects will be drawn in the order they are added */
   private void addCutsceneEffects() {
      this.addEffect(new WaitEffect());
      this.addEffect(new SetOverlayEffect());
      this.addEffect(new FillScreenEffect());
      this.addEffect(new PlayerWalkEffect(this.player));
      this.addEffect(new NPCWalkEffect(this.npcManager));
      this.addEffect(new NumberDisplayEffect(numberDisplay));
      this.addEffect(new RedLightEffect());
      
      // Effects that need to be accessible from the cutsceneManager:
      this.shakeEffect = new ScreenShakeEffect(this.area);
      this.fadeEffect = new FadeEffect(this.eventHandler);
      this.addEffect(fadeEffect);
      this.addEffect(shakeEffect);
   }

   /** Is called from the state's update-loop if the cutsceneManager is active */
   public void handleKeyBoardInputs() {
      if (textBoxManager.isChoiceActive()) {
         this.handleInfoChoiceInputs();
      }
      else if (numberDisplay.isActive()) {
         this.handleNumberDisplayInputs();
      }
      else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         this.advance();
      }
   }

   private void handleInfoChoiceInputs() {
      if ((game.leftIsPressed) || (game.rightIsPressed)) {
         game.leftIsPressed = false;
         game.rightIsPressed = false;
         this.textBoxManager.toggleOptions();
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         int playerChoice = this.textBoxManager.getSelectedOption();
         this.advance(); // Stops the current cutscene
         this.cutsceneIndex = cutsceneIndex + playerChoice;
         this.startCutscene(elementNr, triggerType, cutsceneIndex); // Starts the next one
      }
   }

   private void handleNumberDisplayInputs() {
      // The act of changing the numbers on the display is handled in the 
      // NumberDisplay itself.
      if (game.interactIsPressed) {
         game.interactIsPressed = false;
         int answerGiven = 2;     // 1 = right, 2 = wrong
         if (numberDisplay.isCodeCorrect()) {
            answerGiven = 1;
         }
         this.canAdvance = true;
         this.numberDisplay.reset();
         this.advance(); // Stops the current cutscene
         this.cutsceneIndex = cutsceneIndex + answerGiven;
         this.startCutscene(elementNr, triggerType, cutsceneIndex); // Starts the next one
      }
   }

   /** Starts a standard fade in/out.
    * A standard fade circumvents the 'activateEffect'-method, and thus 'canAdvace'
    * remains true. Instead, use the 'isStandardFadeActive'-method to prevent
    * handling of user input during this time. */
   public void startStandardFade(String in_out) {
      FadeEvent evt = new FadeEvent(in_out, 10, true);
      this.fadeEffect.activate(evt);
   }

   /** Can be called from area to check if a fade is active */
   public boolean isStandardFadeActive() {
      return (this.fadeEffect.isStandardFadeActive());
   }

   /** Can be called from area to check if a shake is active */
   public boolean isShakeActive() {
      return this.shakeEffect.isActive();
   }
   
}
