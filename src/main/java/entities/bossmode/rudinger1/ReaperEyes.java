package entities.bossmode.rudinger1;

import java.awt.Point;
import java.util.ArrayList;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.AnimationInfo;
import entities.bossmode.PlayerBoss;

/**
 * An animated component for use with Rudinger1.
 * It tracks the position of the Player and sets eye position accordingly.
 * It has several animations that can be activated.
 */
public class ReaperEyes extends AnimatedComponent {
   private PlayerBoss player;
   private Point eyesCenter;
   private int eyeMoveDistance = 25;
   private int eyesWidth;
   private float startX;
   private float startY;

   public ReaperEyes(String spriteSheet, int spriteW, int spriteH, int rows, int cols,
         ArrayList<AnimationInfo> aniInfo, float xPos, float yPos, PlayerBoss player) {
      super(spriteSheet, spriteW, spriteH, rows, cols, aniInfo, xPos, yPos);
      this.startX = xPos;
      this.startY = yPos;
      this.player = player;
      this.eyesWidth = spriteW * 3;
      this.eyesCenter = new Point((int) (xPos + (eyesWidth / 2)), (int) yPos);
      this.lookAtPlayer();
   }

   public void update() {
      if (aniAction == 0 || aniAction == 4) {
         lookAtPlayer();
      }
      if (aniAction == 1) {
         this.xPos = startX;
         this.yPos = startY + 10;
      }
      this.updateAnimations();
   }

   private void lookAtPlayer() {
      // Calculate the direction vector of the line
      double dx = player.getHitbox().getCenterX() - eyesCenter.getX();
      double dy = player.getHitbox().getCenterY() - eyesCenter.getY();

      // Calculate the length of the line
      double lineLength = Math.sqrt(dx * dx + dy * dy);

      // Normalize the direction vector
      dx /= lineLength;
      dy /= lineLength;

      // Calculate the new point coordinates
      double newX = eyesCenter.getX() + dx * eyeMoveDistance;
      double newY = eyesCenter.getY() + dy * eyeMoveDistance;

      this.xPos = (float) (newX - (eyesWidth / 2));
      this.yPos = (float) newY;
   }

   public void setAnimation(int aniAction) {
      this.aniAction = aniAction;
   }
}
