package entities.flying.enemies;

import static entities.flying.EntityFactory.TypeConstants.BURNING_FRAGMENT;
import static entities.flying.EntityFactory.TypeConstants.SMALL_ASTEROID;
import static utils.Constants.Flying.SpriteSizes.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import entities.flying.EntityFactory;
import entities.flying.PlayerFly;
import main_classes.Game;
import projectiles.Explosion;
import utils.ResourceLoader;
import utils.Constants.Audio;

public class EnemyManager {
    PlayerFly player;
    private EntityFactory entityFactory;
    private AudioPlayer audioPlayer;
    private ArrayList<Enemy> allEnemies;
    private ArrayList<Enemy> activeEnemiesOnScreen;
    private BufferedImage[] explosionAnimation;
    private ArrayList<Explosion> explosions;
    private int collisionDmg = 10;
    private int teleportDmg = 80;
    private ArrayList<Integer> killedEnemies;           // Contains the enemyTypes
    private ArrayList<Integer> killedEnemiesAtCheckpoint;

    public EnemyManager(PlayerFly player, EntityFactory entityFactory, AudioPlayer audioPlayer) {
        this.player = player;
        this.audioPlayer = audioPlayer;
        this.entityFactory = entityFactory;
        this.loadImgs();
        allEnemies = new ArrayList<>();
        activeEnemiesOnScreen = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.killedEnemies = new ArrayList<>();
        this.killedEnemiesAtCheckpoint = new ArrayList<>();
    }

    private void loadImgs() {
        this.explosionAnimation = new BufferedImage[5];
        BufferedImage explosionImg = ResourceLoader.getFlyImageSprite(ResourceLoader.EXPLOSION);
        for (int i = 0; i < explosionAnimation.length; i++) {
            explosionAnimation[i] = explosionImg.getSubimage(
                i * EXPLOSION_SPRITE_SIZE, 0, EXPLOSION_SPRITE_SIZE, EXPLOSION_SPRITE_SIZE);
        }
    }

    public void loadEnemiesForLvl(int lvl) {
        allEnemies.clear();
        activeEnemiesOnScreen.clear();
        explosions.clear();
        killedEnemies.clear();
        killedEnemiesAtCheckpoint.clear();

        List<String> enemyData = ResourceLoader.getFlyLevelData(lvl);
        for (String line : enemyData) {
            String[] lineData = line.split(";");
            String entryName = lineData[0];
            if (entityFactory.isEnemyRegistered(entryName)) {
                this.allEnemies.add(entityFactory.GetNewEnemy(lineData));
            }
        }
    }

    public void update(float levelYSpeed) {
        updateExplosions(levelYSpeed);
        activeEnemiesOnScreen.clear();
        for (Enemy enemy : allEnemies) {
            enemy.update(levelYSpeed);
            if (enemy.isOnScreen() && !enemy.isDead()) {
                activeEnemiesOnScreen.add(enemy);
                if (enemy.isSmall() && player.teleportDamagesEnemy(enemy.getHitbox())) {
                    enemy.takeShootDamage(teleportDmg);
                    checkIfDead(enemy);
                }
                else if (player.collidesWithEnemy(enemy.getHitbox())) {   // Also pushes player in opposite direction
                    enemy.takeCollisionDamage(collisionDmg);
                    checkIfDead(enemy);
                }
            }
        }
    }

    /** Can be called from this object, and the ProjectileHandler.
     * Checks if the enemy is dead, and if so, plays SFX, increases killed enemies 
     * and adds explosion.
     * @param enemy
    */
    public void checkIfDead(Enemy enemy) {
        if (enemy.isDead()) {
            audioPlayer.playSFX(Audio.SFX_SMALL_EXPLOSION);
            increaseKilledEnemies(enemy.getType());
            if (enemy.isSmall()) {
                this.addSmallExplosion(enemy.getHitbox());
            }
            else {
                this.addBigExplosion(enemy.getHitbox());
            }
        }
    }

    private void updateExplosions(float levelYSpeed) {
        int toRemove = 0;
        for (Explosion ex : explosions) {
            ex.update(levelYSpeed);
            if (ex.isDone()) {
                toRemove += 1;
            }
        }
        while (toRemove > 0) {
            explosions.remove(0);
            toRemove -= 1;
        }
    }

    /** Makes an explosion centered in the enemy's hitbox */
    public void addSmallExplosion(Rectangle2D.Float hb) {
        int size = EXPLOSION_SPRITE_SIZE * 3;
        float x = (hb.x + hb.width/2) - (size / 2);
        float y = (hb.y + hb.height/2) - (size / 2);
        explosions.add(new Explosion((int) x, (int) y, size));
    }

    /** Makes a big explosion centered in the enemy's hitbox */
    private void addBigExplosion(Float hb) {
        int size = EXPLOSION_SPRITE_SIZE * 8;
        float x = (hb.x + hb.width/2) - (size / 2);
        float y = (hb.y + hb.height/2) - (size / 2);
        explosions.add(new Explosion((int) x, (int) y, size));
    }

    public void draw(Graphics g) {
        ArrayList<Enemy> copy = new ArrayList<>(activeEnemiesOnScreen);
        for (Enemy enemy : copy) {  
            enemy.draw(g);
            enemy.drawHitbox(g);
        }
        for (Explosion ex : explosions) {   // ConcurrentModificationException
            g.drawImage(
            explosionAnimation[ex.getAniIndex()],
            (int) (ex.getX() * Game.SCALE),
            (int) (ex.getY() * Game.SCALE),
            (int) (ex.getSize() * Game.SCALE),
            (int) (ex.getSize() * Game.SCALE),
            null);
            
        }
    }

    public ArrayList<Enemy> getActiveEnemiesOnScreen() {
        return this.activeEnemiesOnScreen;
    }

    public void increaseKilledEnemies(int enemyType) {
        if (doesEnemyCount(enemyType)) {
            this.killedEnemies.add(enemyType);
            this.player.setKilledEnemies(killedEnemies.size() + killedEnemiesAtCheckpoint.size());
        }
    }

    /** Returns true if the enemy counts in terms of the killCount */
    private boolean doesEnemyCount(int enemyType) {
        return (
            enemyType != SMALL_ASTEROID && 
            enemyType != BURNING_FRAGMENT
            );
    }

    public void decreaseKilledEnemies(int enemyType) {
        if (killedEnemies.size() > 0) {
            this.killedEnemies.remove(killedEnemies.size() - 1);
        }
        this.player.setKilledEnemies(killedEnemies.size() + killedEnemiesAtCheckpoint.size());
     }
    
    public ArrayList<Integer> getFinalKilledEnemies() {
        return this.killedEnemies;
    }

    /** Should only be called once at the end of the level */
    public void levelFinished() {
        this.killedEnemies.addAll(killedEnemiesAtCheckpoint);
    }

    public ArrayList<Integer> getEnemiesKilledAtCheckpoint() {
        return this.killedEnemiesAtCheckpoint;
    }

    /** Should be called from Flying when the checkpoint has been reached */
    public void checkPointReached() {
        this.killedEnemiesAtCheckpoint.addAll(killedEnemies);
        killedEnemies.clear();
    }

    /** Is used with the 'startAt()'-method */
    public void moveAllEnemies(int yOffset) {
        for (Enemy enemy : allEnemies) {
            enemy.getHitbox().y -= yOffset;
        }
    }

    public void resetEnemiesTo(float y, boolean toCheckPoint) {
        if (!toCheckPoint) {
            this.killedEnemiesAtCheckpoint.clear();
        }
        this.killedEnemies.clear();
        this.explosions.clear();
        for (Enemy enemy : allEnemies) {
            enemy.resetTo(y);
            // It's a tiny bit unecessary to do it on all enemies in case we're going
            // to a checkpoint, but it saves us a bit of troublesome coding
        }
    }

    /** Is needed in the player-object, to check teleport collision with big enemies */
    public ArrayList<Enemy> getBigEnemies() {
        ArrayList<Enemy> bigEnemies = new ArrayList<>();
        for (Enemy e : activeEnemiesOnScreen) {
            if (!e.isSmall()) {
                bigEnemies.add(e);
            }
        }
        return bigEnemies;
    }
}
