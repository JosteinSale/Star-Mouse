package rendering.misc;

import java.awt.Graphics;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.SwingRender;
import ui.InfoBox;
import utils.DrawUtils;
import utils.Images;

import static utils.Constants.UI.INFOBOX_WIDTH;
import static utils.Constants.UI.INFOBOX_HEIGHT;

public class RenderInfoBox implements SwingRender {
   private InfoBox infoBox;
   private MyImage infoBoxImg;
   private int infoX = Game.GAME_DEFAULT_WIDTH / 2 - INFOBOX_WIDTH / 2;
   private int infoY = 580;

   public RenderInfoBox(InfoBox infoBox, Images images) {
      this.infoBoxImg = images.getExpImageSprite(Images.INFO_BOX, true);
      this.infoBox = infoBox;
   }

   @Override
   public void draw(Graphics g) {
      // Background
      DrawUtils.drawImage(
            g, infoBoxImg,
            infoX, infoY,
            INFOBOX_WIDTH, INFOBOX_HEIGHT);

      // Text
      for (int i = 0; i < infoBox.formattedStrings.size(); i++) {
         DrawUtils.drawText(
               g, MyColor.BLACK, DrawUtils.infoFont,
               infoBox.formattedStrings.get(i),
               infoX + 60, infoY + 60 + (i * 40));
      }
   }

   @Override
   public void dispose() {
   }

}
