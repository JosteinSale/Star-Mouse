package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import data_storage.DataStorage;
import data_storage.SaveData;
import main_classes.Game;
import misc.ProgressValues;
import utils.Constants.Audio;
import utils.ResourceLoader;

import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;
import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

public class LoadSaveMenu {
   private Game game;
   private AudioPlayer audioPlayer;
   private BufferedImage pointerImg;
   private SaveData saveData;
   private Color bgColor = new Color(0, 0, 0, 230);
   private Font headerFont;
   private Font menuFont;

   private String[] menuOptions = {"Save 1", "Save 2", "Save 3", "Return"};
   private static final int SAVE_1 = 0;
   private static final int SAVE_2 = 1;
   private static final int SAVE_3 = 2;
   private static final int RETURN = 3;

   private boolean active = false;
   private int selectedIndex = 0;

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

   public LoadSaveMenu(Game game) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      initializeSaveData();
      calcDrawValues();
      loadImages();
      loadFonts();
      
   }

   private void initializeSaveData() {
      // Load data if it exists
      saveData = DataStorage.loadData();
      if (saveData == null) {
         // If no data exists, create new data
         saveData = new SaveData(new ProgressValues(), new ProgressValues(), new ProgressValues());
      }
   }

   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
   }

   private void loadFonts() {
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getMenuFont();
   }

   public void update() {
      this.handleKeyBoardInputs();
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
      else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         if (selectedIndex != RETURN) {
            this.handleLoadSave(selectedIndex + 1);
         }
         else if (selectedIndex == RETURN) {
            this.active = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         }
      }
   }

   private void handleLoadSave(int saveNr) {
      
   }

   private void mockSaveData() {
      // Use the data
      System.out.println("saveData1.credits:" + saveData.getProgValuesFor(1).getCredits());

      // Modify the data
      saveData.getProgValuesFor(2).setCredits(100);

      // Save the data back to the file
      DataStorage.saveData(saveData, false);
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
      g.drawString("LOAD SAVE", (int) (420 * Game.SCALE), (int) (150 * Game.SCALE));

      g.setFont(menuFont);
      for (int i = 0; i < menuOptions.length; i++) {
         g.drawString(
            menuOptions[i], 
            (int) (optionsX * Game.SCALE), (int) ((optionsY + i * menuOptionsDiff) * Game.SCALE));
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
