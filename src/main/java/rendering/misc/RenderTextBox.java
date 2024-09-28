package rendering.misc;

import java.awt.Graphics;

import ui.BigDialogueBox;
import ui.SmallDialogueBox;
import ui.TextboxManager;
import utils.HelpMethods;
import utils.ResourceLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.UI.*;

import main_classes.Game;
import rendering.SwingRender;
import rendering.root_renders.RenderInfoBox;
import rendering.root_renders.RenderInfoChoice;

public class RenderTextBox implements SwingRender {
   private TextboxManager tbM;

   // Infobox
   private RenderInfoBox rInfoBox;

   // InfoChoice
   private RenderInfoChoice rInfoChoice;

   // Big dialogue box
   private BufferedImage bigDialogueBoxImg;
   private BufferedImage[][] portraits;
   private Font dialogueFont;
   private Font nameFont;
   private int bDialogueX = (int) ((Game.GAME_DEFAULT_WIDTH / 2 - DIALOGUEBOX_WIDTH / 2) * Game.SCALE);
   private int bDialogueY = (int) (550 * Game.SCALE);
   private Color nameColor;

   // Small dialogue box (uses some of the BigDialogueBox-variables)
   private Color sDialogueBgColor = new Color(0, 0, 0, 100);
   private int sDialogueX = (int) (40 * Game.SCALE);
   private int sDialogueY = (int) (600 * Game.SCALE);

   public RenderTextBox(TextboxManager textBoxManager, RenderInfoBox rInfoBox, RenderInfoChoice rInfoChoice) {
      this.tbM = textBoxManager;
      this.rInfoBox = rInfoBox;
      this.rInfoChoice = rInfoChoice;
      this.loadImages();
      this.loadFonts();
   }

   private void loadFonts() {
      dialogueFont = ResourceLoader.getInfoFont();
      nameFont = ResourceLoader.getNameFont();
   }

   private void loadImages() {
      bigDialogueBoxImg = ResourceLoader.getExpImageSprite(ResourceLoader.DIALOGUE_BOX);
      portraits = this.getAllPortraits();
   }

   private BufferedImage[][] getAllPortraits() {
      BufferedImage[][] portraits;
      int nrOfSpecialCharacters = 4;
      int nrOfNpcs = 17;
      int maxAmountOfPortraits = 15;
      int nrOfCharacters = nrOfSpecialCharacters + nrOfNpcs;
      portraits = new BufferedImage[nrOfCharacters][maxAmountOfPortraits];
      portraits[0] = getPortraitsFor(ResourceLoader.MAX_PORTRAITS, 15, 0);
      portraits[1] = getPortraitsFor(ResourceLoader.OLIVER_PORTRAITS, 9, 0);
      portraits[2] = getPortraitsFor(ResourceLoader.LT_RED_PORTRAITS, 6, 0);
      portraits[3] = getPortraitsFor(ResourceLoader.RUDINGER_PORTRAITS, 7, 0);
      for (int i = 0; i < nrOfNpcs; i++) {
         portraits[i + nrOfSpecialCharacters] = getPortraitsFor(ResourceLoader.NPC_PORTRAITS, 4, i);
      }
      return portraits;
   }

   private BufferedImage[] getPortraitsFor(String fileName, int length, int rowIndex) {
      BufferedImage img = ResourceLoader.getExpImageSprite(fileName);
      BufferedImage[] portraits = new BufferedImage[length];
      for (int i = 0; i < portraits.length; i++) {
         portraits[i] = img.getSubimage(
               i * PORTRAIT_SIZE, rowIndex * PORTRAIT_SIZE,
               PORTRAIT_SIZE, PORTRAIT_SIZE);
      }
      return portraits;
   }

   /** Must be set to get the correct name color */
   public void setDialogue(String name) {
      this.nameColor = HelpMethods.GetNameColor(name);
   }

   @Override
   public void draw(Graphics g) {
      if (tbM.infoActive) {
         this.rInfoBox.draw(g);
      } else if (tbM.infoChoiceActive) {
         this.rInfoChoice.draw(g);
      } else if (tbM.bigDialogueActive) {
         this.drawBigDialogue(g);
      } else if (tbM.smallDialogueActive) {
         this.drawSmallDialogue(g);
      }
   }

   private void drawBigDialogue(Graphics g) {
      BigDialogueBox dialogue = tbM.getBigDialogueBox();

      // Background
      g.drawImage(
            bigDialogueBoxImg, bDialogueX, bDialogueY,
            (int) (DIALOGUEBOX_WIDTH * Game.SCALE),
            (int) (DIALOGUEBOX_HEIGHT * Game.SCALE), null);

      // Portrait
      g.drawImage(
            portraits[dialogue.characterIndex][dialogue.portraitIndex],
            bDialogueX + (int) (12 * Game.SCALE), bDialogueY + (int) (12 * Game.SCALE),
            (int) (PORTRAIT_SIZE * 3 * Game.SCALE), (int) (PORTRAIT_SIZE * 3 * Game.SCALE), null);

      // Text - Name
      g.setColor(nameColor);
      g.setFont(nameFont);
      g.drawString(
            dialogue.name,
            bDialogueX + (int) (200 * Game.SCALE),
            bDialogueY + (int) (60 * Game.SCALE));

      // Text - Dialogue
      g.setColor(Color.WHITE);
      g.setFont(dialogueFont);
      drawBigDialogueText(
            g, dialogue.currentLine, dialogue.formattedStrings,
            dialogue.currentLetter, bDialogueX, bDialogueY);
   }

   private void drawBigDialogueText(
         Graphics g, int currentLine, ArrayList<String> text, int currentLetter, int x, int y) {
      for (int i = 0; i < (currentLine + 1); i++) {
         int endLetter = text.get(i).length();
         if (i == currentLine) {
            endLetter = currentLetter + 1;
         }
         drawPartialSentence(g, text.get(i),
               endLetter,
               x + (int) (200 * Game.SCALE), y + (int) ((i * 35 + 100) * Game.SCALE));
      }
   }

   private void drawPartialSentence(Graphics g, String s, int currentLetter, int x, int y) {
      g.drawString(
            s.substring(0, currentLetter), x, y);
   }

   private void drawSmallDialogue(Graphics g) {
      SmallDialogueBox dialogue = tbM.getSmallDialogueBox();

      // Background
      g.setColor(sDialogueBgColor);
      g.fillRect(sDialogueX, sDialogueY, (int) (850 * Game.SCALE), (int) (120 * Game.SCALE));

      // Portrait
      g.drawImage(
            portraits[dialogue.characterIndex][dialogue.portraitIndex],
            sDialogueX + (int) (5 * Game.SCALE), sDialogueY + (int) (5 * Game.SCALE),
            (int) (110 * Game.SCALE), (int) (110 * Game.SCALE), null);

      // Text - Name
      g.setColor(nameColor);
      g.setFont(nameFont);
      g.drawString(
            dialogue.name,
            sDialogueX + (int) (130 * Game.SCALE),
            sDialogueY + (int) (40 * Game.SCALE));

      // Text - Dialogue
      g.setColor(Color.WHITE);
      g.setFont(dialogueFont);
      drawPartialSentence(g, dialogue.text, dialogue.currentLetter, sDialogueX, sDialogueY);
   }

   @Override
   public void dispose() {
   }

}
