package ui;

import static utils.Constants.UI.MECHANIC_DISPLAY_HEIGHT;
import static utils.Constants.UI.MECHANIC_DISPLAY_WIDTH;
import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import main.Game;
import misc.ProgressValues;
import utils.Constants.Audio;
import utils.LoadSave;

public class MechanicOverlay {
   private Game game;
   private AudioPlayer audioPlayer;
   private ProgressValues progValues;
   private BufferedImage bgImg;
   private BufferedImage pointerImg;
   private Color inventoryColor = new Color(0, 0, 0, 230);
   private Color lazerBarColor = Color.CYAN;
   private Color hpBarColor = Color.ORANGE;
   private Color displayColor = Color.GREEN;
   private Font menuFont;
   private Font itemFont;
   private Font infoFont;
   private final int highestMaxHP = 200;
   private final int highestLazerDmg = 15;

   private String[] optionNames = {"Lazer", "Shield", "Bomb", ""};
   private String[][] optionInfo = {
      {"Upgrade lazer", "   (+2 damage)", "Current: x/y", "Price: xxxx"}, 
      {"Upgrade shield", "   (+100 shield)", "Current: x/y", "Price: xxxx"}, 
      {"Buy a bomb", "   (+1 bomb)", "", "Price: xxxx"}, 
      {"", "", "", ""}
   };
   private int lazerPrice = 1000;
   private int shieldPrice = 1000;
   private int bombPrice = 1000;
   private static int UPGRADE_LAZER = 0;
   private static int UPGRADE_SHIP = 1;
   private static int BUY_BOMB = 2;
   private static int EXIT = 3;
   private int selectedIndex = 3;

   private int bgImgX = Game.GAME_DEFAULT_WIDTH / 2 - (MECHANIC_DISPLAY_WIDTH / 2);
   private int bgImgY = 20; 
   private int bgImgW = MECHANIC_DISPLAY_WIDTH;
   private int bgImgH = MECHANIC_DISPLAY_HEIGHT;

   private int inventoryX = bgImgX;  
   private int inventoryY = 560;   
   private int inventoryW = bgImgW;   
   private int inventoryH = 170;     

   private int cursorX = 200;
   private int cursorMinY = 190;
   private int cursorMaxY = 420;
   private int cursorY = cursorMaxY;
   private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;

   private int lazerBarW;
   private int hpBarW;
   private int barH = 21;
   private int barMaxW = 159;
   

   public MechanicOverlay(Game game, ProgressValues progressValues) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      this.progValues = progressValues;
      updateBarWidths();
      updateTextInfo();
      loadImages();
      loadFonts();
   }

   private void updateTextInfo() {
      // Lazer
      optionInfo[0][1] = "Current: " + 
         Integer.toString(progValues.getLazerDmg()) + "/" + Integer.toString(highestLazerDmg);
      optionInfo[0][2] = "Price: " + 
         Integer.toString(lazerPrice) + " (+2 damage)";
      
      // HP
      optionInfo[1][1] = "Current: " + 
         Integer.toString(progValues.getMaxHP()) + "/" + Integer.toString(highestMaxHP);
      optionInfo[1][2] = "Price: " + 
         Integer.toString(shieldPrice) + " (+100 shield)";
      
      // Bombs
      optionInfo[2][2] = "Price: " + 
         Integer.toString(bombPrice) + " (+1 bomb)";
   }

   private void updateBarWidths() {
      float lazerScale = ((float) progValues.getLazerDmg()) / highestLazerDmg;
      float hpScale = ((float) progValues.getMaxHP()) / highestMaxHP;
      lazerBarW = (int) (lazerScale * barMaxW);
      hpBarW = (int) (hpScale * barMaxW);
   }

   private void loadImages() {
      this.pointerImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_BLACK);
      this.bgImg = LoadSave.getExpImageSprite(LoadSave.MECHANIC_DISPLAY);
   }

   private void loadFonts() {
      this.menuFont = LoadSave.getNameFont();
      this.infoFont = LoadSave.getInfoFont();
      this.itemFont = LoadSave.getItemFont();
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
      else if (game.interactIsPressed) {
         game.interactIsPressed = false;

         if (selectedIndex == EXIT) {
            game.getExploring().setMechanicActive(false);
         }
      }
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
      // Display
      g.drawImage(
         bgImg, 
         (int) (bgImgX * Game.SCALE), (int) (bgImgY * Game.SCALE), 
         (int) (bgImgW * Game.SCALE), (int) (bgImgH * Game.SCALE), null);
      
      // Cursor
      g.drawImage(
         pointerImg,
         (int) (cursorX * Game.SCALE), (int) ((cursorY - 30) * Game.SCALE),
         (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);

      // Inventory
      g.setColor(inventoryColor);
      g.fillRect(
         (int) (inventoryX * Game.SCALE), (int) (inventoryY * Game.SCALE), 
         (int) (inventoryW * Game.SCALE), (int) (inventoryH * Game.SCALE));
      
      g.setColor(Color.WHITE);
      g.drawRect(
         (int) ((inventoryX + 10) * Game.SCALE), (int) ((inventoryY + 10) * Game.SCALE), 
         (int) ((inventoryW - 20) * Game.SCALE), (int) ((inventoryH - 20) * Game.SCALE));

      // Bars
      g.setColor(lazerBarColor);
      g.fillRect(
         (int) (370 * Game.SCALE), (int) (167 * Game.SCALE), 
         (int) (lazerBarW * Game.SCALE),
         (int) (barH * Game.SCALE));
      g.setColor(hpBarColor);
      g.fillRect(
         (int) (370 * Game.SCALE), (int) (240 * Game.SCALE), 
         (int) (hpBarW * Game.SCALE),
         (int) (barH * Game.SCALE));

      // Menu text
      g.setFont(menuFont);
      g.setColor(Color.GRAY.darker());
      g.drawString("EXIT", (int) (360 * Game.SCALE), (int) (410 * Game.SCALE));

      g.setFont(menuFont);
      g.setColor(Color.WHITE);
      g.drawString("Inventory", (int) (410 * Game.SCALE), (int) (610 * Game.SCALE));

      g.setFont(infoFont);
      g.drawString(
         "Credits: x" + Integer.toString(progValues.getCredits()), 
         (int) (250 * Game.SCALE), (int) (670 * Game.SCALE));
      g.drawString(
         "Bombs: x" + Integer.toString(progValues.getBombs()), 
         (int) (620 * Game.SCALE), (int) (670 * Game.SCALE));
      
      // Display text
      g.setColor(displayColor);
      g.setFont(menuFont);
      g.drawString(
         optionNames[selectedIndex], 
         (int) (560 * Game.SCALE), (int) (200 * Game.SCALE));
      g.setFont(itemFont);
      for (int i = 0; i < 4; i++) {
         g.drawString(
            optionInfo[selectedIndex][i], 
            (int) (560 * Game.SCALE), (int) ((250 + i * 50) * Game.SCALE));
      }
   }

   
   
}
