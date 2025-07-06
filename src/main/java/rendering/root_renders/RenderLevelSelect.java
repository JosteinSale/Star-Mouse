package rendering.root_renders;

import static utils.Constants.UI.LEVEL_ICON_DRAW_SIZE;
import static utils.Constants.UI.LEVEL_ICON_SIZE;
import static utils.Constants.UI.LEVEL_SELECT_BOX_SIZE;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gamestates.level_select.BaseLevelLayout;
import gamestates.level_select.LevelSelect;
import gamestates.level_select.LevelSlot;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.Render;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

public class RenderLevelSelect implements Render {
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
      this.bgImg = images.getMiscImage(
            Images.LEVEL_SELECT_BG, true);

      // Layout images
      this.layoutImgs = new ArrayList<>();
      this.layoutImgs.add(images.getMiscImage(
            Images.LEVEL_SELECT_LAYOUT1, true));
      this.layoutImgs.add(images.getMiscImage(
            Images.LEVEL_SELECT_LAYOUT2, true));
      this.layoutImgs.add(images.getMiscImage(
            Images.LEVEL_SELECT_LAYOUT3, true));
      this.cursorBox = images.getExpImageSprite(
            Images.LEVEL_SELECT_BOX, true);

      // Level icons
      this.levelIcons = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getExpImageSprite(Images.LEVEL_ICONS, true),
            13, LEVEL_ICON_SIZE, LEVEL_ICON_SIZE);
   }

   @Override
   public void draw(SpriteBatch sb) {
      // BgImg
      DrawUtils.drawImage(
            sb, bgImg,
            (int) levelSelect.bgX, 0,
            Game.GAME_DEFAULT_WIDTH + 55, Game.GAME_DEFAULT_HEIGHT + 50);

      // Current layout
      drawCurrentLayout(sb);

      // Fade
      if (levelSelect.fadeInActive || levelSelect.fadeOutActive) {
         DrawUtils.fillScreen(sb, new MyColor(0, 0, 0, levelSelect.alphaFade));
      }
   }

   private void drawCurrentLayout(SpriteBatch sb) {
      BaseLevelLayout layout = levelSelect.getCurrentLayout();

      // Layout
      DrawUtils.drawImage(
            sb, layoutImgs.get(levelSelect.getLayoutNr() - 1),
            layout.X, layout.Y,
            layout.W, layout.H);

      // Levels
      for (int i = 0; i < layout.levelSlots.size(); i++) {
         LevelSlot slot = layout.levelSlots.get(i);
         if (!slot.isEmpty) {
            this.drawSlot(sb, slot);
            if (i == layout.selectedIndex) {
               this.drawSlotText(sb, slot);
            }
         }
         if (i == layout.selectedIndex) {
            this.drawSelect(sb, slot);
         }
      }
   }

   /** OBS: only call if the slot is not empty. */
   public void drawSlot(SpriteBatch sb, LevelSlot slot) {
      DrawUtils.drawSubImage(
            sb, this.levelIcons[slot.levelInfo.getLevelNr() - 1],
            slot.xPos, slot.yPos,
            LEVEL_ICON_DRAW_SIZE, LEVEL_ICON_DRAW_SIZE);
   }

   /** Call if the slot is currently selected */
   public void drawSelect(SpriteBatch sb, LevelSlot slot) {
      DrawUtils.drawImage(
            sb, cursorBox,
            slot.xPos - 15, slot.yPos - 15,
            LEVEL_SELECT_BOX_SIZE, LEVEL_SELECT_BOX_SIZE);
   }

   /** OBS: only call if the slot is not empty */
   public void drawSlotText(SpriteBatch sb, LevelSlot slot) {
      String name = slot.levelInfo.getName();
      Integer killCount = slot.levelInfo.getKillCount();
      Integer totalEnemies = slot.levelInfo.getTotalEnemies();
      DrawUtils.drawCenteredText(
            sb, name, slot.lvlNameRect,
            DrawUtils.menuFont, MyColor.WHITE);
      DrawUtils.drawCenteredText(
            sb, killCount.toString() + "/" + totalEnemies.toString(), slot.killCountRect,
            DrawUtils.menuFont, MyColor.WHITE);
   }

}
