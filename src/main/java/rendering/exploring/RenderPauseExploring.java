package rendering.exploring;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.Render;
import rendering.misc.RenderOptionsMenu;
import ui.OptionsMenu;
import ui.PauseExploring;
import utils.DrawUtils;
import utils.Images;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.ITEM_BOX_HEIGHT;
import static utils.Constants.UI.ITEM_BOX_WIDTH;
import static utils.Constants.UI.PAUSE_EXPLORING_HEIGHT;
import static utils.Constants.UI.PAUSE_EXPLORING_WIDTH;

public class RenderPauseExploring implements Render {
   private PauseExploring pause;
   private RenderOptionsMenu rOptionsMenu;
   private OptionsMenu optionsMenu;
   private MyColor bgColor = new MyColor(0, 0, 0, 230);
   private MyImage itemBoxImg;
   private MyImage itemSelectedImg;
   private MyImage pointerImg;
   private Rectangle itemInfoBox; // Not adjusted to Game.SCALE
   private ArrayList<Rectangle> menuRects;
   public HashMap<String, MyImage> itemImages;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;
   private int itemBoxW;
   private int itemBoxH;
   private int itemBoxX;
   private int itemBoxY;
   private int cursorX = 505;

   /**
    * This assumes that the optionsMenu used for PauseExploring is the same
    * optionsMenu used in the main menu.
    */
   public RenderPauseExploring(Game game, RenderOptionsMenu rOptionsMenu) {
      this.pause = game.getExploring().getPauseOverlay();
      this.rOptionsMenu = rOptionsMenu; // This will already have a reference to the correct OptionsMenu
      this.optionsMenu = game.getOptionsMenu();
      this.itemImages = new HashMap<>();
      this.constructMenuRects();
      calcDrawValues();
      loadImages(game.getImages());
   }

   private void constructMenuRects() {
      this.menuRects = new ArrayList<>();
      for (int i = 0; i < pause.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               600, (pause.cursorMinY - 35 + i * pause.menuOptionsDiff),
               200, 50);
         menuRects.add(rect);
      }
   }

   private void calcDrawValues() {
      bgW = PAUSE_EXPLORING_WIDTH;
      bgH = PAUSE_EXPLORING_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));

      itemBoxW = ITEM_BOX_WIDTH;
      itemBoxH = ITEM_BOX_HEIGHT;
      itemBoxX = bgX + (bgW / 2) - 20;
      itemBoxY = bgY + 120;

      this.itemInfoBox = new Rectangle(
            170, itemBoxY,
            300, itemBoxH);
   }

   private void loadImages(Images images) {
      this.itemBoxImg = images.getExpImageSprite(
            Images.ITEM_BOX, true);
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
      this.itemSelectedImg = images.getExpImageSprite(
            Images.ITEM_SELECTED, true);

      // Inventory items
      itemImages.put("GRADUATION PRESENT",
            images.getExpImageSprite(Images.PRESENT_IMAGE, true));
      itemImages.put("COMFORT PET PLUSHIE",
            images.getExpImageSprite("item_plushie.png", true));
   }

   @Override
   public void draw(SpriteBatch sb) {
      if (optionsMenu.isActive()) {
         rOptionsMenu.draw(sb);
      } else {
         drawBackground(sb);
         drawItems(sb);
         drawText(sb);
         drawStatusValues(sb);
         drawPointer(sb);
      }
   }

   private void drawStatusValues(SpriteBatch sb) {
      for (int i = 0; i < pause.valueNames.length; i++) {
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.infoFont,
               pause.valueNames[i] + Integer.toString(pause.statusValues[i]),
               170, 480 + (i * 50));
      }
   }

   private void drawPointer(SpriteBatch sb) {
      if (pause.selectedIndex >= 8) {
         DrawUtils.drawImage(
               sb, pointerImg,
               cursorX, pause.cursorY - 30,
               CURSOR_WIDTH, CURSOR_HEIGHT);
      }
   }

   private void drawText(SpriteBatch sb) {
      // Header
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.headerFont,
            "Inventory",
            380, 150);

      // Menu options
      for (int i = 0; i < pause.menuOptions.length; i++) {
         DrawUtils.drawCenteredText(
               sb, pause.menuOptions[i], menuRects.get(i),
               DrawUtils.menuFont, MyColor.WHITE);
      }

      // Item Info
      if ((pause.items.size() - 1) >= pause.selectedIndex) {
         String itemName = pause.items.get(pause.selectedIndex).getName();
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.itemFont,
               itemName,
               (int) itemInfoBox.x + 10, (int) itemInfoBox.y + 30);

         ArrayList<String> info = pause.items.get(pause.selectedIndex).getDescription();
         for (int i = 0; i < info.size(); i++) {
            DrawUtils.drawText(
                  sb, MyColor.WHITE, DrawUtils.itemFont,
                  info.get(i),
                  (int) itemInfoBox.x + 10, (int) itemInfoBox.y + 80 + (i * 40));
         }
      }
   }

   private void drawItems(SpriteBatch sb) {
      DrawUtils.drawImage(
            sb, itemBoxImg,
            itemBoxX, itemBoxY,
            itemBoxW, itemBoxH);

      for (int j = 0; j < 2; j++) {
         for (int i = 0; i < 4; i++) {
            int currentIndex = i + (j * 4);
            if ((pause.isItemAtIndex(currentIndex))) {
               String itemName = pause.items.get(currentIndex).getName();
               DrawUtils.drawImage(
                     sb, itemImages.get(itemName),
                     itemBoxX + 6 + (i * 96), itemBoxY + 6 + (j * 96),
                     90, 90);
            }
            // The selected item gets a white frame around it
            if (currentIndex == pause.selectedIndex) {
               DrawUtils.drawImage(
                     sb, itemSelectedImg,
                     itemBoxX + (i * 96), itemBoxY + (j * 96),
                     103, 103);
            }
         }
      }
   }

   private void drawBackground(SpriteBatch sb) {
      DrawUtils.fillRect(
            sb, bgColor,
            bgX, bgY,
            bgW, bgH);
      DrawUtils.drawRect(
            sb, MyColor.WHITE,
            bgX + 10, bgY + 10,
            bgW - 20, bgH - 20);
      DrawUtils.fillRect(
            sb, MyColor.DARKER_GRAY,
            itemInfoBox.x, itemInfoBox.y,
            itemInfoBox.width, itemInfoBox.height);
   }

}
