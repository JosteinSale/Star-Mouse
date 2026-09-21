package projectiles;

public class Explosion {
   private int x;
   private int y;
   private int aniTick = 0;
   private int aniIndex = 0;
   private int aniTickPerFrame = 5;
   private int size;
   private int type;
   boolean done = false;

   // Explosion types
   public static final int SMALL = 0;
   public static final int BIG = 1;
   public static final int MINE = 2;

   public Explosion(int type, int x, int y, int size) {
      this.x = x;
      this.y = y;
      this.size = size;
      this.type = type;
   }

   public void update(float fgCurSpeed) {
      this.y += fgCurSpeed;
      this.aniTick++;
      if (aniTick > aniTickPerFrame) {
         aniIndex++;
         aniTick = 0;
         if (aniIndex == amountOfSpritesInAnimation()) {
            done = true;
            aniIndex--;
         }
      }
   }

   private int amountOfSpritesInAnimation() {
      switch (type) {
         case SMALL, BIG:
            return 5;
         case MINE:
            return 6;
         default:
            throw new IllegalArgumentException("No sprite amount defined for explosion type: " + type);
      }
   }

   public int getAniIndex() {
      return this.aniIndex;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public boolean isDone() {
      return this.done;
   }

   public float getSize() {
      return this.size;
   }

   public int getType() {
      return this.type;
   }
}
