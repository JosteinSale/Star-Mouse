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
   private InfoBox infoBox;
   private InfoChoice infoChoice;
   private Color inventoryColor = new Color(0, 0, 0, 230);
   private Color lazerBarColor = Color.CYAN;
   private Color hpBarColor = Color.ORANGE;
   private Color displayColor = Color.GREEN;
   private Font menuFont;
   private Font itemFont;
   private Font infoFont;
   private final int highestMaxHP = 300;
   private final int highestLazerDmg = 15;
   private boolean infoBoxActive = false;
   private boolean infoChoiceActive = false;

   private String[] optionNames = {"Lazer", "Shield", "Bomb", ""};
   private String[][] optionInfo = {
      {"(+1 dmg each ray)", "Current: x/y", "$ xxxx"}, 
      {"(+10 shield)", "Current: x/y", "$ xxxx"}, 
      {"(+1 bomb)", "Current: x", "$ xxxx"}, 
      {"", "", ""}
   };
   private int selectedIndex = 3;
   private static int UPGRADE_LAZER = 0;
   private static int UPGRADE_SHIP = 1;
   private static int BUY_BOMB = 2;
   private static int EXIT = 3;
   private int lazerPrice = 1000;
   private int shieldPrice = 800;
   private int bombPrice = 500;
   private int[] prices = {lazerPrice, shieldPrice, bombPrice};
   private int[] upgradeValues = {1, 10, 1};
   private String[] buyNames = {"a lazer upgrade", "a shield upgrade", "1 bomb"};

   private int bgImgX = Game.GAME_DEFAULT_WIDTH / 2 - (MECHANIC_DISPLAY_WIDTH / 2);
   private int bgImgY = 20; 
   private int bgImgW = MECHANIC_DISPLAY_WIDTH;
   private int bgImgH = MECHANIC_DISPLAY_HEIGHT;
   private int inventoryX = bgImgX;  
   private int inventoryY = 580;   
   private int inventoryW = bgImgW;   
   private int inventoryH = 150;     
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
      this.infoBox = new InfoBox();
      this.infoChoice = new InfoChoice();
      updateBarWidths();
      updateTextInfo();
      loadImages();
      loadFonts();
   }

   private void updateTextInfo() {
      // Lazer
      optionInfo[0][1] = "Current: " + 
         Integer.toString(progValues.getLazerDmg()) + "/" + Integer.toString(highestLazerDmg);
      optionInfo[0][2] = "$" + Integer.toString(lazerPrice);
      
      // HP
      optionInfo[1][1] = "Current: " + 
         Integer.toString(progValues.getMaxHP()) + "/" + Integer.toString(highestMaxHP);
      optionInfo[1][2] = "$" + Integer.toString(shieldPrice);
      
      // Bombs
      optionInfo[2][1] = "Current: x" + Integer.toString(progValues.getBombs());
      optionInfo[2][2] = "$" + Integer.toString(bombPrice);
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
      if (infoChoiceActive) {
         handleInfoChoice();
         return;
      }
      else if (infoBoxActive) {
         if (game.interactIsPressed) {
            game.interactIsPressed = false;
            this.infoBoxActive = false;
            return;
         }
      }
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
         else {
            if (maxedOut()) {
               System.out.println("Maxed out!");
            }
            else if (hasEnoughCredits()) {
               askIfWantToBuy();
            }
            else {
               System.out.println("You don't have enough credits!");
            }
            
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

   private boolean hasEnoughCredits() {
      return (progValues.getCredits() >= prices[selectedIndex]);
   }

   private boolean maxedOut() {
      if (selectedIndex == UPGRADE_LAZER) {
         return (progValues.getLazerDmg() == highestLazerDmg);
      }
      else if (selectedIndex == UPGRADE_SHIP) {
         return (progValues.getMaxHP() == highestMaxHP);
      }
      return false;
   }

   private void askIfWantToBuy() {
      audioPlayer.playSFX(Audio.SFX_INFOBOX);
      infoChoiceActive = true;
      infoChoice.setText(
         "Buy " + buyNames[selectedIndex] + "?", "Yes", "No");
   }

   private void handleInfoChoice() {
      if (game.interactIsPressed) {
         game.interactIsPressed = false;
         infoChoiceActive = false;
         if (infoChoice.getSelectedOption() == 1) {    // Player answered 'yes'
            audioPlayer.playSFX(Audio.SFX_INVENTORY_PICKUP);
            removeCredits();
            increaseProgValues();
            updateTextInfo();
            updateBarWidths();
            infoBoxActive = true;
            infoBox.setText(
               "You bought " + buyNames[selectedIndex] + 
               " and lost " + Integer.toString(prices[selectedIndex]) + " credits!");
         }
      }
      else if (game.leftIsPressed || game.rightIsPressed) {  // Toggle options
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         game.leftIsPressed = false;
         game.rightIsPressed = false;
         infoChoice.toggle();
      }
   }

   private void removeCredits() {
      progValues.setCredits(progValues.getCredits() - prices[selectedIndex]);
   }

   private void increaseProgValues() {
      if (selectedIndex == UPGRADE_LAZER) {
         progValues.setLazerDmg(progValues.getLazerDmg() + upgradeValues[selectedIndex]);
      }
      else if (selectedIndex == UPGRADE_SHIP) {
         progValues.setMaxHP(progValues.getMaxHP() + upgradeValues[selectedIndex]);
      }
      else {
         progValues.setBombs(progValues.getBombs() + upgradeValues[selectedIndex]);
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
      g.drawString("Inventory", (int) (410 * Game.SCALE), (int) (630 * Game.SCALE));

      g.setFont(infoFont);
      g.drawString(
         "Credits: x" + Integer.toString(progValues.getCredits()), 
         (int) (250 * Game.SCALE), (int) (690 * Game.SCALE));
      g.drawString(
         "Bombs: x" + Integer.toString(progValues.getBombs()), 
         (int) (620 * Game.SCALE), (int) (690 * Game.SCALE));
      
      // Display-text
      g.setColor(displayColor);
      g.setFont(menuFont);
      g.drawString(
         optionNames[selectedIndex], 
         (int) (560 * Game.SCALE), (int) (200 * Game.SCALE));
      g.setFont(itemFont);
      for (int i = 0; i < 2; i++) {  // Item-info
         g.drawString(
            optionInfo[selectedIndex][i], 
            (int) (560 * Game.SCALE), (int) ((250 + i * 50) * Game.SCALE));
      }
      g.setFont(menuFont);  // Item-price
      g.drawString(
         optionInfo[selectedIndex][2], 
         (int) (560 * Game.SCALE), (int) (400 * Game.SCALE));

      if (infoBoxActive) {
         infoBox.draw(g);
      }
      else if (infoChoiceActive) {
         infoChoice.draw(g);
      }
   }
}
