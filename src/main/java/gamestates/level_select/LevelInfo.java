package gamestates.level_select;

public class LevelInfo {
    private String name;
    private int killCount;
    private int totalEnemies;
    private int threshold;
    private int nextInThisPath;
    private int nextInWorsePath;

    /** Use this constructor if the level has a standard 'next-in-path'-level */
    public LevelInfo(String name, int enemies, int threshold, int nextInPath, int nextInWorsePath) {
        this.name = name;
        this.killCount = 0;
        this.totalEnemies = enemies;
        this.threshold = threshold;
        this.nextInThisPath = nextInPath;
        this.nextInWorsePath = nextInWorsePath;
    }

    /** Use this constructor if the level does NOT have a standard 'next-in-path'-level */
    public LevelInfo(String name, int enemies) {
        this.name = name;
        this.killCount = 0;
        this.totalEnemies = enemies;
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

    public int getThreshold() {
        return threshold;
    }

    /** If the new killCount is higher than the older, it sets the killCount to the given value */
    public void updateKillCount(int count) {
        if (count > killCount) {
            killCount = count;
        }
    }

    public int getNext(boolean hasEnoughKills) {
        if (nextInThisPath == 0 || nextInWorsePath == 0) {
            throw new IllegalArgumentException("This level doesn't have a standard 'next-in-path'-level");
        }
        if (hasEnoughKills) {
            return nextInThisPath;
        } else {
            return nextInWorsePath;
        }
    }
}
