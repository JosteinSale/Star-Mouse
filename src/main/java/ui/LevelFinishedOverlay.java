package ui;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static entities.flying.EntityFactory.TypeConstants.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Statemethods;
import gamestates.flying.Flying;
import main_classes.Game;
import utils.ResourceLoader;
import utils.Constants.Audio;

public class LevelFinishedOverlay implements Statemethods {
   private Game game;
   private Flying flying;
   private Font infoFont;
   private Font continueFont;
   private BufferedImage cursorImg;
   private String[] statusText = { "Targets destroyed:", "Credits earned:", "Total credits:" };
   private int[] statusValues = { 0, 0, 0 };
   private ArrayList<Integer> enemiesKilled; // Contains killed enemyTypes for a level
   private int totalCredits;
   private int creditsEarned;

   private int statusX = 330;
   private int statusY = 390;
   private int yDiff = 50;
   private int lettersPerLine = 22;
   private int currentLetter = 0;
   private int linesToDraw = 0;

   public LevelFinishedOverlay(Game game, Flying flying) {
      this.game = game;
      this.flying = flying;
      this.enemiesKilled = new ArrayList<>();
      this.infoFont = ResourceLoader.getInfoFont();
      this.continueFont = ResourceLoader.getNameFont();
      this.cursorImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
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
      g.setColor(Color.WHITE);
      g.setFont(infoFont);
      for (int i = 0; i < linesToDraw; i++) {
         int endLetter = lettersPerLine;
         if ((i + 1) == linesToDraw) {
            endLetter = currentLetter - (i * lettersPerLine);
         }
         drawPartialSentence(
               g, statusText[i], endLetter,
               (int) (statusX * Game.SCALE),
               (int) ((statusY + i * yDiff) * Game.SCALE));
      }

      // Draws 'Continue' with cursor if all text has appeared
      if (currentLetter == (lettersPerLine * 3)) {
         g.setFont(continueFont);
         g.drawString(
               "Continue",
               (int) (420 * Game.SCALE),
               (int) (600 * Game.SCALE));
         g.drawImage(
               cursorImg,
               (int) (330 * Game.SCALE),
               (int) (570 * Game.SCALE),
               (int) (CURSOR_WIDTH * Game.SCALE),
               (int) (CURSOR_HEIGHT * Game.SCALE), null);
      }
   }

   private void drawPartialSentence(Graphics g, String s, int endLetter, int x, int y) {
      g.drawString(
            s.substring(0, endLetter), x, y);
   }

}
