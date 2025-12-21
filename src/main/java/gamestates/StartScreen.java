package gamestates;

import audio.AudioPlayer;
import main_classes.Game;
import utils.Constants.Audio;
import utils.Fader;

public class StartScreen extends State {
   public Fader fader;
   private AudioPlayer audioPlayer;

   public StartScreen(Game game) {
      super(game);
      this.audioPlayer = game.getAudioPlayer();
      this.fader = new Fader();
   }

   public void update() {
      if (fader.isFading()) {
         fader.update();
      } else {
         handleKeyBoardInputs();
      }
   }

   private void goToMainMenu() {
      Gamestate.state = Gamestate.MAIN_MENU;
      audioPlayer.startSong(Audio.SONG_MAIN_MENU, 0, true);
   }

   private void handleKeyBoardInputs() {
      if (game.interactIsPressed) {
         game.interactIsPressed = false;
         fader.startFadeOut(Fader.MEDIUM_FAST_FADE, () -> goToMainMenu());
         audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
      }
   }
}
