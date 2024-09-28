package rendering.root_renders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main_classes.Game;
import rendering.SwingRender;
import ui.InfoBox;
import utils.ResourceLoader;

import static utils.Constants.UI.INFOBOX_WIDTH;
import static utils.Constants.UI.INFOBOX_HEIGHT;

public class RenderInfoBox implements SwingRender {
   private InfoBox infoBox;
   private Font infoFont;
   private BufferedImage infoBoxImg;
   private int infoX = (int) ((Game.GAME_DEFAULT_WIDTH / 2 - INFOBOX_WIDTH / 2) * Game.SCALE);
   private int infoY = (int) (580 * Game.SCALE);

   public RenderInfoBox(InfoBox infoBox) {
      this.infoBoxImg = ResourceLoader.getExpImageSprite(ResourceLoader.INFO_BOX);
      this.infoFont = ResourceLoader.getInfoFont();
      this.infoBox = infoBox;
   }

   @Override
   public void draw(Graphics g) {
      // Background
      g.drawImage(
            infoBoxImg, infoX, infoY,
            (int) (INFOBOX_WIDTH * Game.SCALE),
            (int) (INFOBOX_HEIGHT * Game.SCALE), null);

      // Text
      g.setColor(Color.BLACK);
      g.setFont(infoFont);
      for (int i = 0; i < infoBox.formattedStrings.size(); i++) {
         g.drawString(
               infoBox.formattedStrings.get(i),
               infoX + (int) (60 * Game.SCALE),
               infoY + (int) (60 * Game.SCALE) + (int) ((i * 40) * Game.SCALE));
      }
   }

   @Override
   public void dispose() {
   }

   public BufferedImage getBackground() {
      return this.infoBoxImg;
   }

   public Font getFont() {
      return this.infoFont;
   }

}
