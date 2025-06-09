package rendering.misc;

import ui.BigDialogueBox;
import ui.SmallDialogueBox;
import ui.TextboxManager;
import utils.DrawUtils;
import utils.HelpMethods;
import utils.Images;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static utils.Constants.UI.*;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.Render;

public class RenderTextBox implements Render {
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
   public void draw(SpriteBatch sb) {
      if (tbM.infoActive) {
         this.rInfoBox.draw(sb);
      } else if (tbM.infoChoiceActive) {
         this.rInfoChoice.draw(sb);
      } else if (tbM.bigDialogueActive) {
         this.drawBigDialogue(sb);
      } else if (tbM.smallDialogueActive) {
         this.drawSmallDialogue(sb);
      }
   }

   private void drawBigDialogue(SpriteBatch sb) {
      BigDialogueBox dialogue = tbM.getBigDialogueBox();

      // Background
      DrawUtils.drawImage(
            sb, bigDialogueBoxImg,
            bDialogueX, bDialogueY,
            DIALOGUEBOX_WIDTH, DIALOGUEBOX_HEIGHT);

      // Portrait
      DrawUtils.drawSubImage(
            sb, portraits[dialogue.characterIndex][dialogue.portraitIndex],
            bDialogueX + 12, bDialogueY + 12,
            PORTRAIT_SIZE * 3, PORTRAIT_SIZE * 3);

      // Text - Name
      DrawUtils.drawText(
            sb, nameColor, DrawUtils.nameFont,
            dialogue.name,
            bDialogueX + 200, bDialogueY + 60);

      // Text - Dialogue
      drawBigDialogueText(
            sb, dialogue.currentLine, dialogue.formattedStrings,
            dialogue.currentLetter, bDialogueX, bDialogueY);
   }

   private void drawBigDialogueText(
         SpriteBatch sb, int currentLine, ArrayList<String> text, int currentLetter, int x, int y) {
      for (int i = 0; i < (currentLine + 1); i++) {
         int endLetter = text.get(i).length();
         if (i == currentLine) {
            endLetter = currentLetter + 1;
         }
         drawPartialSentence(
               sb, text.get(i), endLetter,
               x + 200, y + 100 + i * 35);
      }
   }

   private void drawPartialSentence(SpriteBatch sb, String s, int currentLetter, int x, int y) {
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.infoFont,
            s.substring(0, currentLetter),
            x, y);

   }

   private void drawSmallDialogue(SpriteBatch sb) {
      SmallDialogueBox dialogue = tbM.getSmallDialogueBox();

      // Background
      DrawUtils.fillRect(
            sb, sDialogueBgColor,
            sDialogueX, sDialogueY,
            850, 120);

      // Portrait
      DrawUtils.drawSubImage(
            sb, portraits[dialogue.characterIndex][dialogue.portraitIndex],
            sDialogueX + 5, sDialogueY + 5,
            110, 110);

      // Text - Name
      DrawUtils.drawText(
            sb, nameColor, DrawUtils.nameFont,
            dialogue.name,
            sDialogueX + 130, sDialogueY + 40);

      // Text - Dialogue
      drawPartialSentence(
            sb, dialogue.text, dialogue.currentLetter,
            sDialogueX + 130, sDialogueY + 80);
   }
}
