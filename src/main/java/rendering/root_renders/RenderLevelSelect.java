package rendering.root_renders;

import static utils.Constants.UI.LEVEL_ICON_DRAW_SIZE;
import static utils.Constants.UI.LEVEL_ICON_SIZE;
import static utils.Constants.UI.LEVEL_SELECT_BOX_SIZE;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.level_select.BaseLevelLayout;
import gamestates.level_select.LevelSelect;
import gamestates.level_select.LevelSlot;
import main_classes.Game;
import rendering.SwingRender;
import utils.ResourceLoader;

public class RenderLevelSelect implements SwingRender {
   private LevelSelect levelSelect;
   private BufferedImage bgImg;
   private ArrayList<BufferedImage> layoutImgs;
   private BufferedImage[] levelIcons;
   protected BufferedImage cursorBox;
   private Font font;

   public RenderLevelSelect(Game game, BufferedImage bgImg) {
      this.levelSelect = game.getLevelSelect();
      this.bgImg = bgImg;
      this.font = ResourceLoader.getMenuFont();
      this.levelIcons = loadLevelIcons();
      this.loadLayoutImgs();

   }

   private void loadLayoutImgs() {
      this.layoutImgs = new ArrayList<>();
      this.layoutImgs.add(ResourceLoader.getExpImageBackground(ResourceLoader.LEVEL_SELECT_LAYOUT1));
      this.layoutImgs.add(ResourceLoader.getExpImageBackground(ResourceLoader.LEVEL_SELECT_LAYOUT2));
      this.layoutImgs.add(ResourceLoader.getExpImageBackground(ResourceLoader.LEVEL_SELECT_LAYOUT3));
      this.cursorBox = ResourceLoader.getExpImageSprite(ResourceLoader.LEVEL_SELECT_BOX);
   }

   private BufferedImage[] loadLevelIcons() {
      BufferedImage[] images = new BufferedImage[13];
      BufferedImage img = ResourceLoader.getExpImageSprite(ResourceLoader.LEVEL_ICONS);
      for (int i = 0; i < 13; i++) {
         images[i] = img.getSubimage(
               i * LEVEL_ICON_SIZE, 0,
               LEVEL_ICON_SIZE, LEVEL_ICON_SIZE);
      }
      return images;
   }

   @Override
   public void draw(Graphics g) {
      // BgImg
      g.drawImage(bgImg, (int) levelSelect.bgX, 0, Game.GAME_WIDTH + 55, Game.GAME_HEIGHT + 50, null);

      // Current layout
      drawCurrentLayout(g);

      // Fade
      if (levelSelect.fadeInActive || levelSelect.fadeOutActive) {
         g.setColor(new Color(0, 0, 0, levelSelect.alphaFade));
         g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
      }
   }

   private void drawCurrentLayout(Graphics g) {
      BaseLevelLayout layout = levelSelect.getCurrentLayout();
      // Layout
      g.drawImage(
            layoutImgs.get(levelSelect.getLayoutNr() - 1),
            (int) (layout.X * Game.SCALE), (int) (layout.Y * Game.SCALE),
            (int) (layout.W * Game.SCALE), (int) (layout.H * Game.SCALE), null);

      // Levels
      for (int i = 0; i < layout.levelSlots.size(); i++) {
         LevelSlot slot = layout.levelSlots.get(i);
         if (!slot.isEmpty) {
            this.drawSlot(g, i, slot);
            if (i == layout.selectedIndex) {
               this.drawSlotText(g, slot);
            }
         }
         if (i == layout.selectedIndex) {
            this.drawSelect(g, slot);
         }
      }
   }

   /** OBS: only call if the slot is not empty. */
   public void drawSlot(Graphics g, int i, LevelSlot slot) {
      g.drawImage(
            this.levelIcons[i],
            (int) (slot.xPos * Game.SCALE),
            (int) (slot.yPos * Game.SCALE),
            (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE),
            (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE), null);
   }

   /** Call if the slot is currently selected */
   public void drawSelect(Graphics g, LevelSlot slot) {
      g.drawImage(cursorBox,
            (int) ((slot.xPos - 15) * Game.SCALE), (int) ((slot.yPos - 15) * Game.SCALE),
            (int) (LEVEL_SELECT_BOX_SIZE * Game.SCALE),
            (int) (LEVEL_SELECT_BOX_SIZE * Game.SCALE), null);
   }

   /** OBS: only call if the slot is not empty */
   public void drawSlotText(Graphics g, LevelSlot slot) {
      String name = slot.levelInfo.getName();
      Integer killCount = slot.levelInfo.getKillCount();
      Integer totalEnemies = slot.levelInfo.getTotalEnemies();
      g.setFont(font);
      g.setColor(Color.WHITE);
      DrawCenteredString(g, name, slot.lvlNameRect, font);
      DrawCenteredString(g, killCount.toString() + "/" + totalEnemies.toString(), slot.killCountRect, font);
   }

   @Override
   public void dispose() {
   }

}
