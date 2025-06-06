package rendering.misc;

import java.awt.Graphics;
import ui.BigDialogueBox;
import ui.SmallDialogueBox;
import ui.TextboxManager;
import utils.DrawUtils;
import utils.HelpMethods;
import utils.Images;

import java.util.ArrayList;
import static utils.Constants.UI.*;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.SwingRender;

public class RenderTextBox implements SwingRender {
   private TextboxManager tbM;

   private RenderInfoBox rInfoBox;
   private RenderInfoChoice rInfoChoice;

   // Big dialogue box
   private MyImage bigDialogueBoxImg;
   private MySubImage[][] portraits;
   private int bDialogueX = Game.GAME_DEFAULT_WIDTH / 2 - DIALOGUEBOX_WIDTH / 2;
   private int bDialogueY = 550;
   private MyColor nameColor;

   // Small dialogue box (uses some of the BigDialogueBox-variables)
   private MyColor sDialogueBgColor = new MyColor(0, 0, 0, 100);
   private int sDialogueX = 40;
   private int sDialogueY = 600;

   public RenderTextBox(TextboxManager textBoxManager, RenderInfoBox rInfoBox,
         RenderInfoChoice rInfoChoice, Images images) {
      this.tbM = textBoxManager;
      this.rInfoBox = rInfoBox;
      this.rInfoChoice = rInfoChoice;
      this.loadImages(images);
   }

   private void loadImages(Images images) {
      bigDialogueBoxImg = images.getExpImageSprite(
            Images.DIALOGUE_BOX, true);
      portraits = this.getAllPortraits(images);
   }

   private MySubImage[][] getAllPortraits(Images images) {
      MySubImage[][] portraits;
      int nrOfSpecialCharacters = 4;
      int nrOfNpcs = 17;
      int maxPortraitColumns = 15;
      int nrOfCharacters = nrOfSpecialCharacters + nrOfNpcs;
      portraits = new MySubImage[nrOfCharacters][maxPortraitColumns];
      portraits[0] = getPortraitsFor(
            Images.MAX_PORTRAITS, 15, 0, images);
      portraits[1] = getPortraitsFor(
            Images.OLIVER_PORTRAITS, 9, 0, images);
      portraits[2] = getPortraitsFor(
            Images.LT_RED_PORTRAITS, 6, 0, images);
      portraits[3] = getPortraitsFor(
            Images.RUDINGER_PORTRAITS, 7, 0, images);
      for (int i = 0; i < nrOfNpcs; i++) {
         portraits[i + nrOfSpecialCharacters] = getPortraitsFor(
               Images.NPC_PORTRAITS, 4, i, images);
      }
      return portraits;
   }

   private MySubImage[] getPortraitsFor(String fileName, int length, int rowIndex, Images images) {
      MyImage img = images.getExpImageSprite(fileName, true);
      MySubImage[] portraits = new MySubImage[length];
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
      DrawUtils.drawImage(
            g, bigDialogueBoxImg,
            bDialogueX, bDialogueY,
            DIALOGUEBOX_WIDTH, DIALOGUEBOX_HEIGHT);

      // Portrait
      DrawUtils.drawSubImage(
            g, portraits[dialogue.characterIndex][dialogue.portraitIndex],
            bDialogueX + 12, bDialogueY + 12,
            PORTRAIT_SIZE * 3, PORTRAIT_SIZE * 3);

      // Text - Name
      DrawUtils.drawText(
            g, nameColor, DrawUtils.nameFont,
            dialogue.name,
            bDialogueX + 200, bDialogueY + 60);

      // Text - Dialogue
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
         drawPartialSentence(
               g, text.get(i), endLetter,
               x + 200, y + 100 + i * 35);
      }
   }

   private void drawPartialSentence(Graphics g, String s, int currentLetter, int x, int y) {
      DrawUtils.drawText(
            g, MyColor.WHITE, DrawUtils.infoFont,
            s.substring(0, currentLetter),
            x, y);

   }

   private void drawSmallDialogue(Graphics g) {
      SmallDialogueBox dialogue = tbM.getSmallDialogueBox();

      // Background
      DrawUtils.fillRect(
            g, sDialogueBgColor,
            sDialogueX, sDialogueY,
            850, 120);

      // Portrait
      DrawUtils.drawSubImage(
            g, portraits[dialogue.characterIndex][dialogue.portraitIndex],
            sDialogueX + 5, sDialogueY + 5,
            110, 110);

      // Text - Name
      DrawUtils.drawText(
            g, nameColor, DrawUtils.nameFont,
            dialogue.name,
            sDialogueX + 130, sDialogueY + 40);

      // Text - Dialogue
      drawPartialSentence(
            g, dialogue.text, dialogue.currentLetter,
            sDialogueX + 130, sDialogueY + 80);
   }

   @Override
   public void dispose() {
   }

}
