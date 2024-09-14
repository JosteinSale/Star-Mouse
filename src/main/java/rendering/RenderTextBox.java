package rendering;

import java.awt.Graphics;
import java.awt.Rectangle;

import ui.BigDialogueBox;
import ui.InfoChoice;
import ui.SmallDialogueBox;
import ui.TextboxManager;
import utils.HelpMethods;
import utils.ResourceLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.HelpMethods.DrawCenteredString;

import static utils.Constants.UI.*;

import main_classes.Game;

public class RenderTextBox implements SwingRender {
   private TextboxManager tbM;

   // Infobox
   private Font infoFont;
   private BufferedImage infoBoxImg;
   private int infoX = (int) ((Game.GAME_DEFAULT_WIDTH / 2 - INFOBOX_WIDTH / 2) * Game.SCALE);
   private int infoY = (int) (580 * Game.SCALE);

   // InfoChoice
   private Rectangle questionRect;
   private int infoChY;
   private BufferedImage cursorImg;
   private int cursorY;
   private int cursorW;
   private int cursorH;

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

   public RenderTextBox(TextboxManager textBoxManager) {
      this.tbM = textBoxManager;
      this.loadImages();
      this.loadFonts();
      this.calcInfoChoiceValues();
   }

   private void loadFonts() {
      infoFont = ResourceLoader.getInfoFont();
      dialogueFont = ResourceLoader.getInfoFont();
      nameFont = ResourceLoader.getNameFont();
   }

   private void loadImages() {
      infoBoxImg = ResourceLoader.getExpImageSprite(ResourceLoader.INFO_BOX);
      cursorImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_BLACK);
      bigDialogueBoxImg = ResourceLoader.getExpImageSprite(ResourceLoader.DIALOGUE_BOX);
      portraits = this.getAllPortraits();
   }

   private void calcInfoChoiceValues() {
      this.infoChY = (int) (580 * Game.SCALE);
      this.cursorY = infoChY + (int) (90 * Game.SCALE);
      this.cursorW = (int) (CURSOR_WIDTH * 0.6f * Game.SCALE);
      this.cursorH = (int) (CURSOR_HEIGHT * 0.6f * Game.SCALE);
      this.questionRect = new Rectangle(
            tbM.getInfoChoice().infoChX, (int) (infoChY + (20 * Game.SCALE)),
            (int) (INFOBOX_WIDTH * Game.SCALE), (int) (50 * Game.SCALE));
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

   public void setDialogue(String name) {
      this.nameColor = HelpMethods.GetNameColor(name);
   }

   @Override
   public void draw(Graphics g) {
      if (tbM.infoActive) {
         this.drawInfoBox(g);
      } else if (tbM.infoChoiceActive) {
         this.drawInfoChoice(g);
      } else if (tbM.bigDialogueActive) {
         this.drawBigDialogue(g);
      } else if (tbM.smallDialogueActive) {
         this.drawSmallDialogue(g);
      }
   }

   public void drawInfoBox(Graphics g) {
      // Background
      g.drawImage(
            infoBoxImg, infoX, infoY,
            (int) (INFOBOX_WIDTH * Game.SCALE),
            (int) (INFOBOX_HEIGHT * Game.SCALE), null);

      // Text
      g.setColor(Color.BLACK);
      g.setFont(infoFont);
      for (int i = 0; i < tbM.getInfoBoxText().size(); i++) {
         g.drawString(
               tbM.getInfoBoxText().get(i),
               infoX + (int) (60 * Game.SCALE),
               infoY + (int) (60 * Game.SCALE) + (int) ((i * 40) * Game.SCALE));
      }
   }

   private void drawInfoChoice(Graphics g) {
      InfoChoice ic = tbM.getInfoChoice();

      // Background
      g.drawImage(
            infoBoxImg, ic.infoChX, infoChY,
            (int) (INFOBOX_WIDTH * Game.SCALE),
            (int) (INFOBOX_HEIGHT * Game.SCALE), null);

      // Text - Question, left choice and right choice
      g.setColor(Color.BLACK);
      g.setFont(infoFont);
      DrawCenteredString(g, ic.question, questionRect, infoFont);
      g.drawString(ic.leftChoice,
            ic.infoChX + (int) (150 * Game.SCALE),
            infoChY + (int) (110 * Game.SCALE));
      g.drawString(ic.rightChoice,
            ic.infoChX + (int) (400 * Game.SCALE),
            infoChY + (int) (110 * Game.SCALE));

      // Cursor
      g.drawImage(cursorImg, ic.cursorX, cursorY, cursorW, cursorH, null);
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
