package gamestates.level_select;

public class LevelInfo {
    private String name;
    private int killCount;
    private int totalEnemies;
    private int killThreshold;

    public LevelInfo(String name, int totalEnemies, int killThresHold) {
        this.name = name;
        this.killCount = 0;
        this.totalEnemies = totalEnemies;
        this.killThreshold = killThresHold;
    }

    public String getName() {
        return name;
    }

    public int getKillCount() {
        return killCount;
    }

    public int getTotalEnemies() {
        return totalEnemies;
    }

    public int getKillThreshold() {
        return killThreshold;
    }
}
