package entities.bossmode.rudinger1;

import java.awt.Point;
import java.util.HashMap;

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

   // Different states for the eye animation
   public final static String IDLE = "IDLE";
   public final static String SHUT_DOWN = "SHUT_DOWN";
   public final static String BOOT_UP = "BOOT_UP";
   public final static String FLASHING = "FLASHING";
   public final static String SMALL_EYES = "SMALL_EYES";

   public ReaperEyes(String spriteSheet, int spriteW, int spriteH, int rows, int cols,
         HashMap<String, AnimationInfo> aniInfo, float xPos, float yPos, PlayerBoss player) {
      super(spriteSheet, aniInfo, IDLE, spriteW, spriteH, rows, cols, xPos, yPos);
      this.startX = xPos;
      this.startY = yPos;
      this.player = player;
      this.eyesWidth = spriteW * 3;
      this.eyesCenter = new Point((int) (xPos + (eyesWidth / 2)), (int) yPos);
      this.lookAtPlayer();
   }

   public void update() {
      if (aniAction.equals(IDLE) || aniAction.equals(SMALL_EYES)) {
         lookAtPlayer();
      }
      if (aniAction.equals(SHUT_DOWN)) {
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

   public void setAnimation(String aniAction) {
      this.aniAction = aniAction;
   }
}
