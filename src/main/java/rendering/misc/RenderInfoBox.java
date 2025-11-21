package rendering.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.Render;
import ui.InfoBox;
import utils.DrawUtils;
import utils.Images;

import static utils.Constants.UI.INFOBOX_WIDTH;
import static utils.Constants.UI.INFOBOX_HEIGHT;

public class RenderInfoBox implements Render {
   private InfoBox infoBox;
   private MyImage infoBoxImg;
   private int infoX = Game.GAME_DEFAULT_WIDTH / 2 - INFOBOX_WIDTH / 2;
   private int infoY = 580;

   public RenderInfoBox(InfoBox infoBox, Images images) {
      this.infoBoxImg = images.getExpImageSprite(Images.INFO_BOX, true);
      this.infoBox = infoBox;
   }

   @Override
   public void draw(SpriteBatch sb) {
      // Background
      DrawUtils.drawImage(
            sb, infoBoxImg,
            infoX, infoY,
            INFOBOX_WIDTH, INFOBOX_HEIGHT);

      // Text
      for (int i = 0; i < infoBox.formattedStrings.size(); i++) {
         DrawUtils.drawText(
               sb, MyColor.BLACK, DrawUtils.infoFont,
               infoBox.formattedStrings.get(i),
               infoX + 45, infoY + 60 + (i * 40));
      }
   }
}
