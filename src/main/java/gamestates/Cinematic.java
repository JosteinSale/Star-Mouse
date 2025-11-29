package gamestates;

import static utils.HelpMethods.ParseCutscenes;

import java.util.List;

import audio.AudioPlayer;
import cutscenes.cutsceneManagers.CutsceneManagerCinematic;
import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import game_events.ClearObjectsEvent;
import game_events.EventHandler;
import game_events.ExitCinematicEvent;
import game_events.FadeOutLoopEvent;
import game_events.GeneralEvent;
import game_events.ObjectMoveEvent;
import game_events.PlaySFXEvent;
import game_events.StartAmbienceEvent;
import game_events.StartSongEvent;
import game_events.StopLoopsEvent;
import game_events.TextBoxEvent;
import main_classes.Game;
import utils.ResourceLoader;

/**
 * A gamestate that will exclusively be for cutscenes.
 * These are cutscenes that don't need to be interwoven with gameplay,
 * and don't belong to any one level.
 * They can technically fit in in any part of the game.
 * 
 * At present the cutscenes are loaded when they are started.
 * The startCutscene-method takes a given filename, loads the corresponding
 * cutscene,
 * and then starts it. Afterwards it returns to the given gamestate.
 */
public class Cinematic extends State {

   private EventHandler eventHandler;
   private AudioPlayer audioPlayer;
   private CutsceneManagerCinematic cutsceneManager;
   private Gamestate returnGamestate;

   public Cinematic(Game game) {
      super(game);
      this.audioPlayer = game.getAudioPlayer();
      this.eventHandler = new EventHandler();
      this.cutsceneManager = new CutsceneManagerCinematic(
            game, eventHandler, game.getTextboxManager(), Gamestate.CINEMATIC);
      this.loadEventReactions();
   }

   private void loadEventReactions() {
      this.eventHandler.addEventListener(this::doReaction);
   }

   public void doReaction(GeneralEvent event) {
      if (event instanceof TextBoxEvent tbEvt) {
         this.cutsceneManager.activateTextbox(tbEvt);
      } else if (event instanceof StartSongEvent evt) {
         this.audioPlayer.startSong(evt.index(), 0, evt.shouldLoop());
      } else if (event instanceof StopLoopsEvent) {
         this.audioPlayer.stopAllLoops();
      } else if (event instanceof FadeOutLoopEvent) {
         audioPlayer.fadeOutAllLoops();
      } else if (event instanceof StartAmbienceEvent evt) {
         audioPlayer.startAmbienceLoop(evt.index());
      } else if (event instanceof PlaySFXEvent evt) {
         audioPlayer.playSFX(evt.SFXIndex());
      } else if (event instanceof ObjectMoveEvent evt) {
         this.cutsceneManager.moveObject(evt);
      } else if (event instanceof ClearObjectsEvent) {
         this.cutsceneManager.clearObjects();
      } else if (event instanceof ExitCinematicEvent) {
         this.exitCinematic();
      } else {
         this.cutsceneManager.activateEffect(event);
      }
   }

   private void loadCutscene(String filename) {
      List<String> cutsceneData = ResourceLoader.getCinematicData(filename);
      cutsceneManager.addCutscenes(ParseCutscenes(cutsceneData));
   }

   /**
    * Loads and starts the cutscene with the given file name.
    * Also sets the DefaultCutsceneManager for RenderCutscene.
    * After the cutscene is done, it returns to the given gamestate.
    * This will mostly be the calling gamestate
    * (since mechanisms for handling transitions are already implemented here).
    * Transition back happens in the doReaction-method, through an event.
    */
   public void startCutscene(String fileName, Gamestate returnGamestate) {
      this.returnGamestate = returnGamestate;
      this.loadCutscene(fileName);
      game.getView().getRenderCutscene().setCutsceneManager(cutsceneManager);
      this.cutsceneManager.startCutscene(fileName, 0);
   }

   public void exitCinematic() {
      this.cutsceneManager.reset();
      this.cutsceneManager.resetCurrentCutscene();
      setReturnCutsceneManager(returnGamestate);
      Gamestate.state = this.returnGamestate;
   }

   /**
    * Sets the cutsceneManager for RenderCutscene depending on the
    * return gamestate.
    */
   private void setReturnCutsceneManager(Gamestate rGamestate) {
      DefaultCutsceneManager cm = switch (rGamestate.name()) {
         case "EXPLORING" -> game.getExploring().getCurrentCutsceneManager();
         case "FLYING" -> game.getFlying().getCutsceneManager();
         case "BOSS_MODE" -> game.getBossMode().getCutsceneManager();
         default -> throw new IllegalArgumentException("No case defined for " + rGamestate);
      };
      game.getView().getRenderCutscene().setCutsceneManager(cm);
   }

   public void update() {
      this.handleKeyBoardInputs();
      this.cutsceneManager.update();
   }

   private void handleKeyBoardInputs() {
      if (game.pauseIsPressed) {
         game.pauseIsPressed = false;
         audioPlayer.stopAllLoops();
         this.exitCinematic();
      } else {
         this.cutsceneManager.handleKeyBoardInputs();
      }
   }
}
