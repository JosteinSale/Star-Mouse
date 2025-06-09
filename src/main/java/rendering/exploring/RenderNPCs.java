package rendering.exploring;

import entities.exploring.Gard;
import entities.exploring.NPC;
import entities.exploring.NpcManager;
import entities.exploring.Oliver;
import main_classes.Game;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.Render;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_HEIGHT;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_WIDTH;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderNPCs implements Render {
   private NpcManager npcManager;
   private MySubImage[] standardNpcSprites;
   private MyImage gardSprites;
   private MyImage oliverSprites;
   private MySubImage[][] gardAnimations;
   private MySubImage[][] oliverAnimations;
   private int drawWidth;
   private int drawHeight;

   public RenderNPCs(Game game, NpcManager npcManager) {
      this.npcManager = npcManager;
      this.drawWidth = STANDARD_SPRITE_WIDTH * 3;
      this.drawHeight = STANDARD_SPRITE_HEIGHT * 3;
      this.loadImages(game.getImages());
   }

   private void loadImages(Images images) {
      standardNpcSprites = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getExpImageSprite(Images.STANDARD_NPC_SPRITES, true),
            16, 90, 60);
      gardSprites = images.getExpImageSprite(
            Images.GARD_SPRITES, true);
      oliverSprites = images.getExpImageSprite(
            Images.OLIVER_SPRITES, true);
      gardAnimations = HelpMethods2.GetUnscaled2DAnimationArray(
            gardSprites, 3, 4,
            STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
      oliverAnimations = HelpMethods2.GetUnscaled2DAnimationArray(
            oliverSprites, 6, 4,
            STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
   }

   public void drawFgNpcs(SpriteBatch sb, int xLevelOffset, int yLevelOffset) {
      for (NPC npc : npcManager.allNpcs) {
         if (npc.inForeground()) {
            this.drawNpc(sb, npc, xLevelOffset, yLevelOffset);
         }
      }
   }

   public void drawBgNpcs(SpriteBatch sb, int xLevelOffset, int yLevelOffset) {
      for (NPC npc : npcManager.allNpcs) {
         if (!npc.inForeground()) {
            this.drawNpc(sb, npc, xLevelOffset, yLevelOffset);
         }
      }
   }

   private void drawNpc(SpriteBatch sb, NPC npc, int xLevelOffset, int yLevelOffset) {
      DrawUtils.drawSubImage(
            sb, this.getSprite(npc),
            (int) (npc.getHitbox().x - npc.getXDrawOffset() - xLevelOffset),
            (int) (npc.getHitbox().y - npc.getYDrawOffset() - yLevelOffset),
            drawWidth, drawHeight);
   }

   private MySubImage getSprite(NPC npc) {
      if (npc instanceof Oliver) {
         return oliverAnimations[npc.getAction()][npc.getAniIndex()];
      } else if (npc instanceof Gard) {
         return gardAnimations[npc.getAction()][npc.getAniIndex()];
      } else {
         return this.getStandardSprite(npc.getName());
      }
   }

   /** Returns the index in the sprite-array for the npc-name */
   private MySubImage getStandardSprite(String name) {
      int index = switch (name) {
         case "Shady pilot" -> 0;
         case "Lance" -> 1;
         case "Nina" -> 2;
         case "Charlotte" -> 3;
         case "Russel" -> 4;
         case "Emma" -> 5;
         case "Mechanic" -> 6;
         case "Frida" -> 7;
         case "Nathan" -> 8;
         case "Feno" -> 10;
         case "Lt.Red" -> 11;
         case "Skye" -> 12;
         case "Gard" -> 13;
         case "Zack" -> 14;
         case "Acolyte" -> 15;
         default -> throw new IllegalArgumentException(
               "No characterIndex available for '" + name + "'");
      };
      return standardNpcSprites[index];
   }

   public void drawHitboxes(SpriteBatch sb, int xLevelOffset, int yLevelOffset) {
      for (NPC npc : npcManager.allNpcs) {
         this.drawHitbox(sb, npc, xLevelOffset, yLevelOffset);
      }
   }

   private void drawHitbox(SpriteBatch sb, NPC npc, int xLevelOffset, int yLevelOffset) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException(
            "Unimplemented method 'drawHitbox'");
   }

   @Override
   public void draw(SpriteBatch sb) {
      // Use the drawFgNpcs- and drawBgNpcs-methods instead
   }

}
