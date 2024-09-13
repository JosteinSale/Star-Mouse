package entities.flying;

public class ShipFlame {

    public int aniIndex = 0;
    private int aniTick = 0;
    private int aniTickPerFrame = 2;

    public void update() {
        aniTick++;
        if (aniTick > aniTickPerFrame) {
            aniTick = 0;
            aniIndex = (aniIndex + 1) % 2;
        }
    }

}
