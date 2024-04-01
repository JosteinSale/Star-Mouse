package projectiles;

public class ProjectileHit {
    private int x;
    private int y;
    private int type;
    private int aniTick = 0;
    private int aniIndex = 0;
    private int aniTickPerFrame = 2;
    boolean done = false;

    public ProjectileHit(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        this.aniTick++;
        if (aniTick > aniTickPerFrame) {
            aniIndex ++;
            aniTick = 0;
            if (aniIndex > 3) {
                done = true;
                aniIndex = 3;
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

    public int getType() {
        return this.type;
    }
}
