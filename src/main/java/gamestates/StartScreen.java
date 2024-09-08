package gamestates;

import java.awt.Graphics;

import audio.AudioPlayer;
import main_classes.Game;
import utils.Constants.Audio;

public class StartScreen extends State implements Statemethods {
   public int alphaFade = 0;
   public boolean fadeActive = false;
   private AudioPlayer audioPlayer;

   public StartScreen(Game game) {
      super(game);
      this.audioPlayer = game.getAudioPlayer();
   }

   @Override
   public void update() {
      if (fadeActive) {
         updateFade();
      } else {
         handleKeyBoardInputs();
      }
   }

   private void updateFade() {
      this.alphaFade += 5;
      if (alphaFade > 255) {
         alphaFade = 255;
         goToMainMenu();
      }
   }

   private void goToMainMenu() {
      game.getView().dispose();
      Gamestate.state = Gamestate.MAIN_MENU;
      audioPlayer.startSong(Audio.SONG_MAIN_MENU, 0, true);
   }

   private void handleKeyBoardInputs() {
      if (game.interactIsPressed) {
         game.interactIsPressed = false;
         fadeActive = true;
         audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
      }
   }

   // TODO - Delete when ready
   @Override
   public void draw(Graphics g) {
   }

}
