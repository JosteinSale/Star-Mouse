package rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import main_classes.Game;
import ui.OptionsMenu;
import ui.PauseExploring;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.ITEM_BOX_HEIGHT;
import static utils.Constants.UI.ITEM_BOX_WIDTH;
import static utils.Constants.UI.PAUSE_EXPLORING_HEIGHT;
import static utils.Constants.UI.PAUSE_EXPLORING_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

public class RenderPauseExploring implements SwingRender {
   private PauseExploring pause;
   private RenderOptionsMenu rOptionsMenu;
   private OptionsMenu optionsMenu;
   private Color bgColor = new Color(0, 0, 0, 230);
   private Font headerFont;
   private Font menuFont;
   private Font infoFont;
   private Font itemFont;
   private BufferedImage itemBoxImg;
   private BufferedImage itemSelectedImg;
   private BufferedImage pointerImg;
   private Rectangle itemInfoBox; // All Rectangles are adjusted to game.Scale
   public HashMap<String, BufferedImage> itemImages;

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
    * optionsMenu used in the the main menu.
    */
   public RenderPauseExploring(Game game, RenderOptionsMenu rOptionsMenu) {
      this.pause = game.getExploring().getPauseOverlay();
      this.rOptionsMenu = rOptionsMenu; // This will already have a reference to the correct OptionsMenu
      this.optionsMenu = game.getOptionsMenu();
      this.itemImages = new HashMap<>();
      calcDrawValues();
      loadImages();
      loadFonts();
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
            (int) (170 * Game.SCALE), (int) (itemBoxY * Game.SCALE),
            (int) (300 * Game.SCALE), (int) (itemBoxH * Game.SCALE));
   }

   private void loadImages() {
      this.itemBoxImg = ResourceLoader.getExpImageSprite(ResourceLoader.ITEM_BOX);
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
      this.itemSelectedImg = ResourceLoader.getExpImageSprite(ResourceLoader.ITEM_SELECTED);

      // Inventory items
      itemImages.put("GRADUATION PRESENT", ResourceLoader.getExpImageSprite(ResourceLoader.PRESENT_IMAGE));
      itemImages.put("COMFORT PET PLUSHIE", ResourceLoader.getExpImageSprite("item_plushie.png"));
   }

   private void loadFonts() {
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getNameFont();
      this.infoFont = ResourceLoader.getInfoFont();
      this.itemFont = ResourceLoader.getItemFont();
   }

   @Override
   public void draw(Graphics g) {
      if (optionsMenu.isActive()) {
         rOptionsMenu.draw(g);
      } else {
         Graphics2D g2 = (Graphics2D) g;
         drawBackground(g2);
         drawItems(g2);
         drawText(g2);
         drawStatusValues(g2);
         drawPointer(g2);
      }
   }

   private void drawStatusValues(Graphics2D g2) {
      g2.setFont(infoFont);
      g2.setColor(Color.WHITE);
      for (int i = 0; i < pause.valueNames.length; i++) {
         g2.drawString(
               pause.valueNames[i] + Integer.toString(pause.statusValues[i]),
               (int) (170 * Game.SCALE), (int) ((480 + (i * 50)) * Game.SCALE));
      }
   }

   private void drawPointer(Graphics2D g2) {
      if (pause.selectedIndex >= 8) {
         g2.drawImage(
               pointerImg,
               (int) (cursorX * Game.SCALE), (int) ((pause.cursorY - 30) * Game.SCALE),
               (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
      }
   }

   private void drawText(Graphics2D g2) {
      g2.setColor(Color.WHITE);

      // Header
      g2.setFont(headerFont);
      g2.drawString("Inventory", (int) (380 * Game.SCALE), (int) (150 * Game.SCALE));

      // Menu options
      for (int i = 0; i < pause.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (600 * Game.SCALE), (int) ((pause.cursorMinY - 35 + i * pause.menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
         DrawCenteredString(g2, pause.menuOptions[i], rect, menuFont);
      }

      // Item Info
      g2.setFont(itemFont);
      if ((pause.items.size() - 1) >= pause.selectedIndex) {
         String name = pause.items.get(pause.selectedIndex).getName();
         g2.drawString(
               name,
               itemInfoBox.x + (int) (10 * Game.SCALE),
               itemInfoBox.y + (int) (30 * Game.SCALE));
         ArrayList<String> info = pause.items.get(pause.selectedIndex).getDescription();
         for (int i = 0; i < info.size(); i++) {
            g2.drawString(
                  info.get(i),
                  itemInfoBox.x + (int) (10 * Game.SCALE),
                  itemInfoBox.y + (int) (((80 + (i * 40)) * Game.SCALE)));
         }
      }
   }

   private void drawItems(Graphics2D g2) {
      g2.drawImage(
            itemBoxImg,
            (int) (itemBoxX * Game.SCALE), (int) (itemBoxY * Game.SCALE),
            (int) (itemBoxW * Game.SCALE), (int) (itemBoxH * Game.SCALE), null);

      for (int j = 0; j < 2; j++) {
         for (int i = 0; i < 4; i++) {
            int currentIndex = i + (j * 4);
            if ((pause.isItemAtIndex(currentIndex))) {
               String itemName = pause.items.get(currentIndex).getName();
               g2.drawImage(
                     itemImages.get(itemName),
                     (int) ((itemBoxX + 6 + (i * 96)) * Game.SCALE),
                     (int) ((itemBoxY + 6 + (j * 96)) * Game.SCALE),
                     (int) (90 * Game.SCALE), (int) (90 * Game.SCALE), null);
            }
            // The selected item gets a white frame around it
            if (currentIndex == pause.selectedIndex) {
               g2.drawImage(
                     itemSelectedImg,
                     (int) ((itemBoxX + (i * 96)) * Game.SCALE),
                     (int) ((itemBoxY + (j * 96)) * Game.SCALE),
                     (int) (103 * Game.SCALE), (int) (103 * Game.SCALE), null);
            }
         }
      }
   }

   private void drawBackground(Graphics2D g2) {
      g2.setColor(bgColor);
      g2.fillRect(
            (int) (bgX * Game.SCALE), (int) (bgY * Game.SCALE),
            (int) (bgW * Game.SCALE), (int) (bgH * Game.SCALE));
      g2.setColor(Color.WHITE);
      g2.drawRect(
            (int) ((bgX + 10) * Game.SCALE), (int) ((bgY + 10) * Game.SCALE),
            (int) ((bgW - 20) * Game.SCALE), (int) ((bgH - 20) * Game.SCALE));

      g2.setColor(Color.DARK_GRAY.darker());
      g2.fill(itemInfoBox);
   }

   @Override
   public void dispose() {
   }

}
