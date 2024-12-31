package ui;

import static entities.flying.EntityFactory.TypeConstants.*;

import java.awt.Graphics;
import java.util.ArrayList;

import gamestates.Statemethods;
import gamestates.flying.Flying;
import main_classes.Game;
import utils.Constants.Audio;

public class LevelFinishedOverlay implements Statemethods {
   private Game game;
   private Flying flying;

   public String[] statusText = { "Targets destroyed:", "Credits earned:", "Total credits:" };
   private int[] statusValues = { 0, 0, 0 };
   private ArrayList<Integer> enemiesKilled; // Contains killed enemyTypes for a level
   private int totalCredits;
   private int creditsEarned;

   public int lettersPerLine = 22;
   public int currentLetter = 0;
   public int linesToDraw = 0;

   public LevelFinishedOverlay(Game game, Flying flying) {
      this.game = game;
      this.flying = flying;
      this.enemiesKilled = new ArrayList<>();
   }

   @Override
   public void update() {
      handleKeyboardInputs();
      if (currentLetter < (lettersPerLine * 3)) {
         if (currentLetter % lettersPerLine == 0) {
            this.linesToDraw += 1;
         }
         currentLetter++;
      }
   }

   private void handleKeyboardInputs() {
      if (flying.getGame().interactIsPressed) {
         flying.getGame().interactIsPressed = false;
         this.flying.audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
         this.flying.exitFinishedLevel();
      }
   }

   public void setLevelStats(ArrayList<Integer> enemiesKilled) {
      this.currentLetter = 0;
      this.linesToDraw = 0;
      this.totalCredits = game.getExploring().getProgressValues().getCredits();
      this.enemiesKilled = enemiesKilled;
      calcCreditsEarned();
      updateStatusValues();
      updateStatusText();
   }

   private void calcCreditsEarned() {
      int totalEarned = 0;
      for (Integer i : enemiesKilled) {
         totalEarned += switch (i) {
            case TARGET -> 0;
            case DRONE -> 20;
            case SMALLSHIP -> 10;
            case OCTADRONE -> 40;
            case BLASTERDRONE -> 40;
            case REAPERDRONE -> 60;
            case FLAMEDRONE -> 60;
            case WASPDRONE -> 30;
            case KAMIKAZEDRONE -> 30;
            default -> throw new IllegalArgumentException(
                  "No credit bounty available for enemy of type '" + i + "' ");
         };
      }
      this.creditsEarned = totalEarned;
   }

   private void updateStatusValues() {
      this.statusValues[0] = enemiesKilled.size();
      this.statusValues[1] = creditsEarned;
      this.statusValues[2] = creditsEarned + totalCredits;
      game.getExploring().getProgressValues().setCredits(statusValues[2]);
   }

   private void updateStatusText() {
      statusText[0] = "Targets destroyed:";
      statusText[1] = "Credits earned:";
      statusText[2] = "Total credits:";
      for (int i = 0; i < statusText.length; i++) {
         String nrText = Integer.toString(statusValues[i]);
         int nrOfDigits = nrText.length();
         int nrOfPaddingSpaces = lettersPerLine - statusText[i].length() - nrOfDigits;
         statusText[i] = statusText[i] + (getSpaces(nrOfPaddingSpaces)) + statusValues[i];
      }
   }

   private String getSpaces(int nrOfPaddingSpaces) {
      String spaces = "";
      for (int i = 0; i < nrOfPaddingSpaces; i++) {
         spaces += " ";
      }
      return spaces;
   }

   @Override
   public void draw(Graphics g) {
      System.out.println("Deprecated draw method called - Delete later");
   }

}
