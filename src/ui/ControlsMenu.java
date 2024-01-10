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

public class ControlsMenu {
   private Game game;
   private AudioPlayer audioPlayer;
   private Color bgColor = new Color(0, 0, 0, 230);
   private Font headerFont;
   private Font menuFont;
   private boolean active = false;

   private String[] keyFunctions = { "Up", "Right", "Down", "Left", "Interact / Shoot lazer", "Shoot bombs", "Teleport", "Pause / Unpause", "Return"};
   private String[] keyBindings = { "W", "D", "S", "A", "SPACE", "B", "M", "ENTER"};
   private BufferedImage pointerImg;
   private int selectedIndex = 8;

   private static final int UP = 1;
   private static final int DOWN = -1;
   private static final int OPTION_UP = 0;
   private static final int OPTION_RIGHT = 1;
   private static final int OPTION_DOWN = 2;
   private static final int OPTION_LEFT = 3;
   private static final int OPTION_INTERACT = 4;
   private static final int OPTION_BOMBS = 5;
   private static final int OPTION_TELEPORT = 6;
   private static final int OPTION_PAUSE = 7;
   private static final int RETURN = 8;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;

   private int keyFuncX = 250;
   private int keyBindX = 750;
   private int cursorMinY = 280;
   private int cursorMaxY = 620; 
   private int cursorX = 170;
   private int cursorY = cursorMaxY;
   private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 8;

   public ControlsMenu(Game game, AudioPlayer audioPlayer) {
      this.game = game;
      this.audioPlayer = audioPlayer;
      calcDrawValues();
      loadImages();
      loadFonts();
   }


   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   private void loadImages() {
      this.pointerImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);
   }

   private void loadFonts() {
      this.headerFont = LoadSave.getHeaderFont();
      this.menuFont = LoadSave.getInfoFont();
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
      else if (game.spaceIsPressed) {
         game.spaceIsPressed = false;
         if (selectedIndex == RETURN) {
            this.active = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         }
      }

   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 8) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 8;
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
      g.drawString("CONTROLS", (int) (420 * Game.SCALE), (int) (150 * Game.SCALE));

      g.setFont(menuFont);
      for (int i = 0; i < keyFunctions.length; i++) {
         g.drawString(
            keyFunctions[i], 
            (int) (keyFuncX * Game.SCALE), (int) ((cursorMinY + i * menuOptionsDiff) * Game.SCALE));
      }
      for (int i = 0; i < keyBindings.length; i++) {
         g.drawString(
            keyBindings[i], 
            (int) (keyBindX * Game.SCALE), (int) ((cursorMinY + i * menuOptionsDiff) * Game.SCALE));
      }

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
