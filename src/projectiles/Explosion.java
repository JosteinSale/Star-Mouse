package projectiles;

public class Explosion {
    private int x;
    private int y;
    private int aniTick = 0;
    private int aniIndex = 0;
    private int aniTickPerFrame = 5;
    boolean done = false;
    boolean isSmall;

    public Explosion(int x, int y, boolean isSmall) {
        this.x = x;
        this.y = y;
        this.isSmall = isSmall;
    }

    public void update(float fgCurSpeed) {
        this.y += fgCurSpeed;
        this.aniTick++;
        if (aniTick > aniTickPerFrame) {
            aniIndex ++;
            aniTick = 0;
            if (aniIndex > 4) {
                done = true;
                aniIndex = 4;
            }
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

    public boolean isSmall() {
        return this.isSmall;
    }
}
