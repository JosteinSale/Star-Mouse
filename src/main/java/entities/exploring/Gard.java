package entities.exploring;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.Entity;
import main_classes.Game;
import utils.HelpMethods2;
import utils.ResourceLoader;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_WIDTH;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_HEIGHT;

public class Gard extends Entity implements NPC {
   private String name = "Gard";
   private Rectangle2D.Float triggerBox;
   private BufferedImage spriteSheet;
   private BufferedImage[][] animations;
   private int spriteWidth;
   private int spriteHeight;
   private int startCutscene = 0;
   private boolean inForeground;
   private boolean poseActive = false;

   // Actions
   private int action = 0;
   private final int STANDING = 0;
   private final int WALKING_RIGHT = 1;
   private final int WALKING_LEFT = 2;

   // Directions
   private int direction;
   private final int RIGHT = 0;
   private final int LEFT = 1;
   private final int UP = 2;
   private final int DOWN = 3;

   private int aniTick = 0;
   private int aniTickPerFrame = 8; // Antall ticks per gang animasjonen oppdateres
   private int aniIndex = 0;

   public Gard(Float hitbox, int direction, boolean inForeground) {
      super(hitbox);
      this.direction = direction;
      this.inForeground = inForeground;
      loadSprites();
      adjustImageSizes();
      makeTriggerBox();
   }

   private void makeTriggerBox() {
      this.triggerBox = new Rectangle2D.Float(
            hitbox.x - 8, hitbox.y, hitbox.width + 16, hitbox.height + 8);
   }

   private void loadSprites() {
      this.spriteSheet = ResourceLoader.getExpImageSprite(ResourceLoader.GARD_SPRITES);
      this.animations = HelpMethods2.GetAnimationArray(
            spriteSheet,
            3, 4,
            STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
   }

   private void adjustImageSizes() {
      spriteWidth = (int) (STANDARD_SPRITE_WIDTH * Game.SCALE * 3);
      spriteHeight = (int) (STANDARD_SPRITE_HEIGHT * Game.SCALE * 3);
   }

   @Override
   public void update() {
      updateAniTick();
   }

   private void updateAniTick() {
      if (poseActive) {
         return;
      }
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniIndex++;
         aniTick = 0;
      }
      if (aniIndex >= getSpriteAmount(action)) {
         aniIndex = 0;
      }
      if (action == STANDING) {
         aniIndex = direction;
      }
   }

   private int getSpriteAmount(int action) {
      switch (action) {
         case WALKING_LEFT, WALKING_RIGHT, STANDING:
            return 4;
         default:
            throw new IllegalArgumentException("No sprite amount for action " + action);
      }
   }

   public void adjustPos(float deltaX, float deltaY) {
      this.hitbox.x += deltaX;
      this.hitbox.y += deltaY;
      this.triggerBox.x += deltaX;
      this.triggerBox.y += deltaY;
   }

   @Override
   public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
      g.drawImage(
            animations[action][aniIndex],
            (int) ((hitbox.x - 80 - xLevelOffset) * Game.SCALE),
            (int) ((hitbox.y - 30 - yLevelOffset) * Game.SCALE),
            spriteWidth, spriteHeight,
            null);
   }

   @Override
   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   @Override
   public Rectangle2D.Float getTriggerBox() {
      return this.triggerBox;
   }

   public int getStartCutscene() {
      return this.startCutscene;
   }

   @Override
   public void setNewStartingCutscene(int startCutscene) {
      this.startCutscene = startCutscene;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void setDir(int dir) {
      this.direction = dir;
   }

   @Override
   public void setSprite(boolean poseActive, int colIndex, int rowIndex) {
      if (poseActive == true) {
         this.poseActive = true;
         this.action = rowIndex;
         this.aniIndex = colIndex;
      } else {
         this.poseActive = false;
         this.aniIndex = 0;
         this.aniTick = 0;
      }
   }

   @Override
   public boolean inForeground() {
      return this.inForeground;
   }

   public void setAction(int action) {
      this.action = action;
   }

   @Override
   public void flushImages() {
      this.spriteSheet.flush();
   }

}
