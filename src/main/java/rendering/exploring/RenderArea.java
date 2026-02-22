package rendering.exploring;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.exploring.PlayerExp;
import gamestates.exploring.Area;
import main_classes.Game;
import main_classes.Testing;
import rendering.MySubImage;
import rendering.MyColor;
import rendering.Render;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_HEIGHT;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_WIDTH;

public class RenderArea implements Render {
   private Game game;
   private Area area;
   private RenderMap1 rMap;
   private RenderNPCs rNPCs;

   // Player
   private static int playerSpriteWidth;
   private static int playerSpriteHeight;
   private static ArrayList<MySubImage[][]> playerSprites;
   private final int NORMAL_SPRITE = 0;
   private final int NAKED_SPRITE = 1;
   private final int SAD_SPRITE = 2;

   public RenderArea(Game game, Area area, int levelIndex, int areaIndex) {
      this.game = game;
      this.area = area;
      this.rMap = new RenderMap1(area.getMapManager(), game.getImages(), levelIndex, areaIndex);
      this.rNPCs = new RenderNPCs(game, area.getNpcManager());
      this.loadSprites(game.getImages());
   }

   private void loadSprites(Images images) {
      playerSprites = new ArrayList<>();
      MySubImage[][] normalSprites = HelpMethods2.GetUnscaled2DAnimationArray(
            images.getExpImageSprite(Images.PLAYER_EXP_SPRITES, true),
            6, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
      MySubImage[][] nakedSprites = HelpMethods2.GetUnscaled2DAnimationArray(
            images.getExpImageSprite(Images.PLAYER_EXP_SPRITES_NAKED, true),
            5, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
      MySubImage[][] sadSprites = HelpMethods2.GetUnscaled2DAnimationArray(
            images.getExpImageSprite(Images.PLAYER_EXP_SPRITES_SAD, true),
            5, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
      playerSprites.add(normalSprites);
      playerSprites.add(nakedSprites);
      playerSprites.add(sadSprites);

      adjustPlayerImgSizes();
   }

   private void adjustPlayerImgSizes() {
      playerSpriteWidth = STANDARD_SPRITE_WIDTH * 3;
      playerSpriteHeight = STANDARD_SPRITE_HEIGHT * 3;
   }

   @Override
   public void draw(SpriteBatch sb) {
      int xLevelOffset = area.getMapManager().xLevelOffset;
      int yLevelOffset = area.getMapManager().yLevelOffset;

      // Maps
      rMap.drawLandscape(sb);
      rMap.drawBackground(sb);

      // Entities
      rNPCs.drawBgNpcs(sb, xLevelOffset, yLevelOffset);
      this.drawPlayer(sb, xLevelOffset, yLevelOffset);

      // Foreground
      rNPCs.drawFgNpcs(sb, xLevelOffset, yLevelOffset);
      rMap.drawForeground(sb);

      // Hitboxes
      if (Testing.drawHitboxes) {
         drawHitboxes(sb, xLevelOffset, yLevelOffset);
      }
   }

   public void drawPlayer(SpriteBatch sb, int xLevelOffset, int yLevelOffset) {
      PlayerExp player = area.getPlayer();
      if (player.visible) {
         // drawShadow(g, xLevelOffset, yLevelOffset);
         DrawUtils.drawSubImage(
               sb, playerSprites.get(PlayerExp.CURRENT_SPRITE_SHEET)[player.playerAction][player.aniIndex],
               (int) (player.hitbox.x - 113 - xLevelOffset),
               (int) (player.hitbox.y - 135 - yLevelOffset),
               playerSpriteWidth, playerSpriteHeight);
      }
   }

   private void drawHitboxes(SpriteBatch sb, int xLevelOffset, int yLevelOffset) {
      for (Rectangle2D.Float hitbox : area.getAllHitboxes()) {
         DrawUtils.fillRect(sb, MyColor.RED,
               (int) (hitbox.x - xLevelOffset),
               (int) (hitbox.y - yLevelOffset),
               (int) hitbox.width,
               (int) hitbox.height);
      }
   }

}
