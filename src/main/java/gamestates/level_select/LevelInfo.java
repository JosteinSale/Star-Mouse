package gamestates.level_select;

public class LevelInfo {
    private String name;
    private int killCount;
    private int totalEnemies;
    private int killThreshold;
    private int nextInThisPath;
    private int nextInWorsePath;

    public LevelInfo(String name, int enemies, int threshold, int nextInPath, int nextInWorsePath) {
        this.name = name;
        this.killCount = 0;
        this.totalEnemies = enemies;
        this.killThreshold = threshold;
        this.nextInThisPath = nextInPath;
        this.nextInWorsePath = nextInWorsePath;
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

    /** If the new killCount is higher than the older, it sets the killCount to the given value */
    public void updateKillCount(int count) {
        if (count > killCount) {
            killCount = count;
        }
    }

    public int getNext(boolean hasEnoughKills) {
        if (hasEnoughKills) {
            return nextInThisPath;
        } else {
            return nextInWorsePath;
        }
    }
}
