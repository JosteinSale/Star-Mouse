package gamestates;

import audio.AudioPlayer;
import main_classes.Game;
import utils.Constants.Audio;

public class StartScreen extends State {
   private AudioPlayer audioPlayer;

   public StartScreen(Game game) {
      super(game);
      this.audioPlayer = game.getAudioPlayer();
   }

   public void update() {
      if (!game.isFading()) {
         handleKeyBoardInputs();
      }
   }

   private void handleKeyBoardInputs() {
      if (game.interactIsPressed) {
         game.interactIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         game.returnToMainMenu(null);
      }
   }
}
