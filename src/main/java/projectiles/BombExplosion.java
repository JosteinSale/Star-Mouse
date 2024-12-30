package projectiles;

public class BombExplosion {
    public int x;
    public int y;
    private int aniTick = 0;
    public int aniIndex = 0;
    private int aniTickPerFrame = 5;
    boolean done = false; // Is true when animation is over

    public BombExplosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(float fgCurSpeed) {
        this.y += fgCurSpeed;
        this.aniTick++;
        if (aniTick > aniTickPerFrame) {
            aniIndex++;
            aniTick = 0;
            if (aniIndex > 10) {
                done = true;
                aniIndex = 10;
            }
        }
    }

    public boolean isDone() {
        return this.done;
    }

    public boolean explosionHappens() {
        return (aniIndex == 6) && (aniTick == 0);
    }
}
