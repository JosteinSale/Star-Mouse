package ui;

import audio.AudioPlayer;
import data_storage.ProgressValues;
import main_classes.Game;
import utils.Constants.Audio;

public class MechanicOverlay {
   private Game game;
   private AudioPlayer audioPlayer;
   private InfoBox infoBox;
   private InfoChoice infoChoice;
   private final int highestMaxHP = 300;
   private final int highestLazerDmg = 15;
   private boolean active = false;
   public boolean infoBoxActive = false;
   public boolean infoChoiceActive = false;

   public String[] optionNames = { "Lazer", "Shield", "Bomb", "" };
   public String[][] optionInfo = {
         { "(+1 dmg each ray)", "Current: x/y", "$ xxxx" },
         { "(+10 shield)", "Current: x/y", "$ xxxx" },
         { "(+1 bomb)", "Current: x", "$ xxxx" },
         { "", "", "" }
   };
   public int selectedIndex = 3;
   private static int UPGRADE_LAZER = 0;
   private static int UPGRADE_SHIP = 1;
   private static int BUY_BOMB = 2;
   private static int EXIT = 3;
   private int lazerPrice = 1000;
   private int shieldPrice = 800;
   private int bombPrice = 500;
   private int[] prices = { lazerPrice, shieldPrice, bombPrice };
   private int[] upgradeValues = { 1, 10, 1 };
   private String[] buyNames = { "a lazer upgrade", "a shield upgrade", "1 bomb" };
   public boolean[] maxedOut = { false, false, false };

   private int cursorMinY = 190;
   private int cursorMaxY = 420;
   public int cursorY = cursorMaxY;
   private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;
   public int lazerBarW;
   public int hpBarW;
   private int barMaxW = 159;

   public MechanicOverlay(Game game, InfoBox infoBox, InfoChoice infoChoice) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      this.infoBox = infoBox;
      this.infoChoice = infoChoice;
   }

   /**
    * Call this method when you open the mechanicOverlay, to update the display
    * in accordance with progValues.
    */
   public void onOpen() {
      for (int i = 0; i <= 2; i++) {
         checkAndSetMaxedOut(i);
      }
      updateBarWidths();
      updateTextInfo();
   }

   private void updateTextInfo() {
      ProgressValues progValues = game.getProgressValues();
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
      ProgressValues progValues = game.getProgressValues();
      float lazerScale = ((float) progValues.getLazerDmg()) / highestLazerDmg;
      float hpScale = ((float) progValues.getMaxHP()) / highestMaxHP;
      lazerBarW = (int) (lazerScale * barMaxW);
      hpBarW = (int) (hpScale * barMaxW);
   }

   public void update() {
      handleKeyBoardInputs();
   }

   private void handleKeyBoardInputs() {
      if (infoChoiceActive) {
         handleBuy();
         return;
      } else if (infoBoxActive) {
         if (game.interactIsPressed) {
            game.interactIsPressed = false;
            this.infoBoxActive = false;
         }
         return;
      }
      if (game.downIsPressed) {
         game.downIsPressed = false;
         goDown();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.upIsPressed) {
         game.upIsPressed = false;
         goUp();
         audioPlayer.playSFX(Audio.SFX_CURSOR);
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;

         if (selectedIndex == EXIT) {
            game.getExploring().setMechanicActive(false);
         } else {
            if (maxedOut[selectedIndex]) {
               audioPlayer.playSFX(Audio.SFX_HURT);
            } else if (hasEnoughCredits()) {
               askIfWantToBuy();
            } else {
               audioPlayer.playSFX(Audio.SFX_HURT);
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
      return (game.getProgressValues().getCredits() >= prices[selectedIndex]);
   }

   private void askIfWantToBuy() {
      audioPlayer.playSFX(Audio.SFX_INFOBOX);
      infoChoiceActive = true;
      infoChoice.setText(
            "Buy " + buyNames[selectedIndex] + "?", "Yes", "No");
   }

   private void handleBuy() {
      if (game.interactIsPressed) {
         game.interactIsPressed = false;
         infoChoiceActive = false;
         if (infoChoice.getSelectedOption() == 1) { // Player answered 'yes'
            audioPlayer.playSFX(Audio.SFX_INVENTORY_PICKUP);
            removeCredits();
            increaseProgValues();
            updateTextInfo();
            updateBarWidths();
            checkAndSetMaxedOut(selectedIndex);
            infoBoxActive = true;
            infoBox.setText(
                  "You bought " + buyNames[selectedIndex] +
                        " and lost " + Integer.toString(prices[selectedIndex]) + " credits!");
         }
      } else if (game.leftIsPressed || game.rightIsPressed) { // Toggle options
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         game.leftIsPressed = false;
         game.rightIsPressed = false;
         infoChoice.toggle();
      }
   }

   private void checkAndSetMaxedOut(int selIndex) {
      ProgressValues progValues = game.getProgressValues();
      if (selIndex == UPGRADE_LAZER) {
         if (progValues.getLazerDmg() >= highestLazerDmg) {
            maxedOut[selIndex] = true;
         }
      } else if (selIndex == UPGRADE_SHIP) {
         if (progValues.getMaxHP() >= highestMaxHP) {
            maxedOut[selIndex] = true;
         }
      }
   }

   private void removeCredits() {
      game.getProgressValues().setCredits(
            game.getProgressValues().getCredits() - prices[selectedIndex]);
   }

   private void increaseProgValues() {
      ProgressValues progValues = game.getProgressValues();
      if (selectedIndex == UPGRADE_LAZER) {
         progValues.setLazerDmg(progValues.getLazerDmg() + upgradeValues[selectedIndex]);
      } else if (selectedIndex == UPGRADE_SHIP) {
         progValues.setMaxHP(progValues.getMaxHP() + upgradeValues[selectedIndex]);
         game.getFlying().getPlayer().setMaxHp(progValues.getMaxHP());
         game.getBossMode().getPlayer().setMaxHp(progValues.getMaxHP());
      } else {
         progValues.setBombs(progValues.getBombs() + upgradeValues[selectedIndex]);
      }
      game.getExploring().updatePauseInventory();
      game.saveDataToDisc();
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }
}
