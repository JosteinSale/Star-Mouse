package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;

import main.Game;
import utils.LoadSave;
import utils.Constants.Audio;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.OPTIONS_WIDTH;
import static utils.Constants.UI.SLIDER_HEIGHT;
import static utils.Constants.UI.SLIDER_WIDTH;
import static utils.Constants.UI.OPTIONS_HEIGHT;

public class OptionsMenu {
   private Game game;
   private AudioPlayer audioPlayer;
   private Color bgColor = new Color(0, 0, 0, 230);
   private Font headerFont;
   private Font menuFont;
   private boolean active = false;

   private String[] menuOptions = { "Music volume", "SFX volume", "Controls", "Return"};
   private BufferedImage pointerImg;
   private BufferedImage sliderImg;
   private int selectedIndex = 0;
   private static final int UP = 1;
   private static final int DOWN = -1;
   private static final int MUSIC_VOLUME = 0;
   private static final int SFX_VOLUME = 1;
   private static final int CONTROLS = 2;
   private static final int RETURN = 3;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;

   private int optionsX = 250;
   private int optionsY = 330;
   private int cursorX = 170;
   private int cursorMinY = 330;
   private int cursorMaxY = 550; 
   private int cursorY = cursorMinY;
   private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;
   private int sliderBarWidth = 300;
   private int musicSliderX;
   private int sfxSliderX;
   private int sliderMinX = 570;
   private int sliderMaxX = 830;
   private int musicPercent;
   private int sfxPercent;

   public OptionsMenu(Game game, AudioPlayer audioPlayer) {
      this.game = game;
      this.audioPlayer = audioPlayer;
      musicPercent = fromGainToPercent(audioPlayer.getMusicVolume());
      sfxPercent = fromGainToPercent(audioPlayer.getSfxVolume()); 
      calcVolumeXs();
      calcDrawValues();
      loadImages();
      loadFonts();
   }

   private void calcVolumeXs() {
      musicSliderX = (int) (sliderMinX + ((sliderBarWidth - 16) * (musicPercent / 100f))) - 25;
      sfxSliderX = (int) (sliderMinX + ((sliderBarWidth - 16) * (sfxPercent / 100f))) - 25;
   }

   /** Takes the actual song / sfx-gain, and converts it to percent.
    * Since volume gain seems to be working logarithmically, this method
    * converts the gain to percent exponentially.
    * @param gain
    * @return
    */
   private int fromGainToPercent(float gain) {
      return (int) Math.pow(10, gain) * 10;
   }

   /** Takes the volume percent and converts it to actual volume gain.
    * Since volume gain seems to be working logarithmically, this method
    * converts percent to gain logarithmically.
    * @param gain
    * @return
    */
   private float fromPercentToGain(int percent) {
      return (float) Math.log10(percent/10);
   }

   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   private void loadImages() {
      this.pointerImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);
      this.sliderImg = LoadSave.getExpImageSprite(LoadSave.SLIDER_SPRITE);
   }

   private void loadFonts() {
      this.headerFont = LoadSave.getHeaderFont();
      this.menuFont = LoadSave.getMenuFont();
   }

   public void update() {
      handleKeyBoardInputs();
   }

   private void handleKeyBoardInputs() {
      if (game.downIsPressed) {
         game.downIsPressed = false;
         goDown();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } 
      else if (game.upIsPressed) {
         game.upIsPressed = false;
         goUp();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } 
      else if (game.rightIsPressed) {
         game.rightIsPressed = false;
         changeVolume(selectedIndex, UP);
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } 
      else if (game.leftIsPressed) { 
         game.leftIsPressed = false;
         changeVolume(selectedIndex, DOWN);
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      }
      else if (game.spaceIsPressed) {
         game.spaceIsPressed = false;
         if (selectedIndex == RETURN) {
            this.active = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         }
      }

   }

   private void changeVolume(int selected, int change) {
      if (selected == MUSIC_VOLUME) {
         musicPercent += 10 * change;
         if (musicPercent > 100) {
            musicPercent = 100;
         }
         else if (musicPercent <= 0) {
            musicPercent = 0;
         }
         float newVolume = fromPercentToGain(musicPercent);
         audioPlayer.setSongVolume(newVolume);
      }
      else if (selected == SFX_VOLUME) {
         sfxPercent += 10 * change;
         if (sfxPercent > 100) {
            sfxPercent = 100;
         }
         else if (sfxPercent <= 0) {
            sfxPercent = 0;
         }
         float newVolume = fromPercentToGain(sfxPercent);
         audioPlayer.setSfxVolume(newVolume);
      }
      calcVolumeXs();
   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 3) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 3;
         cursorY = cursorMaxY;
      }
   }

   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      // Background
      g.setColor(bgColor);
      g.fillRect(
         (int) (bgX * Game.SCALE), (int) (bgY * Game.SCALE), 
         (int) (bgW * Game.SCALE), (int) (bgH * Game.SCALE));
      
      g2.setColor(Color.WHITE);
      g2.drawRect(
         (int) ((bgX + 10) * Game.SCALE), (int) ((bgY + 10) * Game.SCALE), 
         (int) ((bgW - 20) * Game.SCALE), (int) ((bgH - 20) * Game.SCALE));

      // Text
      g.setFont(headerFont);
      g.setColor(Color.WHITE);
      g.drawString("OPTIONS", (int) (420 * Game.SCALE), (int) (150 * Game.SCALE));

      g.setFont(menuFont);
      for (int i = 0; i < menuOptions.length; i++) {
         g.drawString(
            menuOptions[i], 
            (int) (optionsX * Game.SCALE), (int) ((optionsY + i * menuOptionsDiff) * Game.SCALE));
      }

      // Sliders
      g.fillRect(
         (int) (550 * Game.SCALE), (int) (318 * Game.SCALE), 
         (int) (sliderBarWidth * Game.SCALE), (int) (5 * Game.SCALE));
      g.fillRect(
         (int) (550 * Game.SCALE), (int) (390 * Game.SCALE), 
         (int) (sliderBarWidth * Game.SCALE), (int) (5 * Game.SCALE));
      g.drawImage(sliderImg, 
         (int) (musicSliderX * Game.SCALE), (int) (295 * Game.SCALE),
         (int) (SLIDER_WIDTH * Game.SCALE), (int) (SLIDER_HEIGHT * Game.SCALE), null);
      g.drawImage(sliderImg, 
         (int) (sfxSliderX * Game.SCALE), (int) (370 * Game.SCALE),
         (int) (SLIDER_WIDTH * Game.SCALE), (int) (SLIDER_HEIGHT * Game.SCALE), null);

      // Cursor
      g2.drawImage(
         pointerImg,
         (int) (cursorX * Game.SCALE), (int) ((cursorY - 30) * Game.SCALE),
         (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }
}
