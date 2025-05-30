package rendering.root_renders;

import static utils.Constants.UI.LEVEL_ICON_DRAW_SIZE;
import static utils.Constants.UI.LEVEL_ICON_SIZE;
import static utils.Constants.UI.LEVEL_SELECT_BOX_SIZE;

import java.awt.Graphics;
import java.util.ArrayList;

import gamestates.level_select.BaseLevelLayout;
import gamestates.level_select.LevelSelect;
import gamestates.level_select.LevelSlot;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

public class RenderLevelSelect implements SwingRender {
   private LevelSelect levelSelect;
   private MyImage bgImg;
   private ArrayList<MyImage> layoutImgs;
   private MySubImage[] levelIcons;
   protected MyImage cursorBox;

   public RenderLevelSelect(Game game) {
      this.levelSelect = game.getLevelSelect();
      this.loadImgs(game.getImages());
   }

   private void loadImgs(Images images) {
      // Backgroun image
      this.bgImg = images.getImageWithCustomFilePath(
            Images.LEVEL_SELECT_BG, true);

      // Layout images
      this.layoutImgs = new ArrayList<>();
      this.layoutImgs.add(images.getExpImageBackground(
            Images.LEVEL_SELECT_LAYOUT1));
      this.layoutImgs.add(images.getExpImageBackground(
            Images.LEVEL_SELECT_LAYOUT2));
      this.layoutImgs.add(images.getExpImageBackground(
            Images.LEVEL_SELECT_LAYOUT3));
      this.cursorBox = images.getExpImageSprite(
            Images.LEVEL_SELECT_BOX, true);

      // Level icons
      this.levelIcons = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getExpImageSprite(Images.LEVEL_ICONS, true),
            13, LEVEL_ICON_SIZE, LEVEL_ICON_SIZE);
   }

   @Override
   public void draw(Graphics g) {
      // BgImg
      DrawUtils.drawImage(
            g, bgImg,
            (int) levelSelect.bgX, 0,
            Game.GAME_DEFAULT_WIDTH + 55, Game.GAME_DEFAULT_HEIGHT + 50);

      // Current layout
      drawCurrentLayout(g);

      // Fade
      if (levelSelect.fadeInActive || levelSelect.fadeOutActive) {
         DrawUtils.fillScreen(g, new MyColor(0, 0, 0, levelSelect.alphaFade));
      }
   }

   private void drawCurrentLayout(Graphics g) {
      BaseLevelLayout layout = levelSelect.getCurrentLayout();

      // Layout
      DrawUtils.drawImage(
            g, layoutImgs.get(levelSelect.getLayoutNr() - 1),
            layout.X, layout.Y,
            layout.W, layout.H);

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
      DrawUtils.drawSubImage(
            g, this.levelIcons[i],
            slot.xPos, slot.yPos,
            LEVEL_ICON_DRAW_SIZE, LEVEL_ICON_DRAW_SIZE);
   }

   /** Call if the slot is currently selected */
   public void drawSelect(Graphics g, LevelSlot slot) {
      DrawUtils.drawImage(
            g, cursorBox,
            slot.xPos - 15, slot.yPos - 15,
            LEVEL_SELECT_BOX_SIZE, LEVEL_SELECT_BOX_SIZE);
   }

   /** OBS: only call if the slot is not empty */
   public void drawSlotText(Graphics g, LevelSlot slot) {
      String name = slot.levelInfo.getName();
      Integer killCount = slot.levelInfo.getKillCount();
      Integer totalEnemies = slot.levelInfo.getTotalEnemies();
      DrawUtils.DrawCenteredString(
            g, name, slot.lvlNameRect,
            DrawUtils.menuFont, MyColor.WHITE);
      DrawUtils.DrawCenteredString(
            g, killCount.toString() + "/" + totalEnemies.toString(), slot.killCountRect,
            DrawUtils.menuFont, MyColor.WHITE);
   }

   @Override
   public void dispose() {
   }

}
