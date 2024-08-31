package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import main_classes.Game;
import utils.ResourceLoader;
import utils.Constants.Audio;

public class StartScreen extends State implements Statemethods {
   private BufferedImage mouseImg;
   private int mouseImgW;
   private int mouseImgH;
   private int alphaFade = 0;
   private boolean fadeActive = false;

   private AudioPlayer audioPlayer;
   private Font font;

   public StartScreen(Game game) {
      super(game);
      this.audioPlayer = game.getAudioPlayer();
      mouseImg = ResourceLoader.getExpImageSprite(ResourceLoader.BASIC_MOUSE);
      mouseImgW = 100 * 3;
      mouseImgH = 100 * 3;
      font = ResourceLoader.getInfoFont();
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
      this.mouseImg.flush();
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

   @Override
   public void draw(Graphics g) {
      // Background
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

      // MouseImg
      g.drawImage(mouseImg,
            (int) ((Game.GAME_DEFAULT_WIDTH / 2 - mouseImgW / 2) * Game.SCALE),
            (int) ((Game.GAME_DEFAULT_HEIGHT / 2 - mouseImgH / 2) * Game.SCALE),
            (int) (mouseImgW * Game.SCALE),
            (int) (mouseImgH * Game.SCALE), null);

      // Text
      g.setColor(Color.WHITE);
      g.setFont(font);
      g.drawString(
            "Press SPACE",
            (int) (430 * Game.SCALE), (int) (600 * Game.SCALE));

      // Fade
      if (fadeActive) {
         g.setColor(new Color(0, 0, 0, alphaFade));
         g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
      }
   }

}
