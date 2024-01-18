package entities.flying;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import main.Game;
import projectiles.Explosion;
import utils.LoadSave;
import utils.Constants.Audio;

import static utils.HelpMethods2.GetEnemy;
import static utils.HelpMethods2.GetEnemyAnimations;
import static utils.Constants.Flying.TypeConstants.*;
import static utils.Constants.Flying.Sprites.*;

public class EnemyManager {
    PlayerFly player;
    private AudioPlayer audioPlayer;
    private ArrayList<Enemy> allEnemies;
    private ArrayList<Enemy> activeEnemiesOnScreen;
    private BufferedImage[][] targetAnimations;
    private BufferedImage[][] droneAnimations;
    private BufferedImage[][] smallShipAnimations;
    private BufferedImage[][] octadroneAnimations;
    private BufferedImage[][] tankdroneAnimations;
    private BufferedImage[][] blasterdroneAnimations;
    private BufferedImage[] explosionAnimation;
    private ArrayList<Explosion> explosions;
    private int collisionDmg = 10;
    private int teleportDmg = 100;
    private ArrayList<Integer> killedEnemies;           // Contains the enemyTypes

    public EnemyManager(PlayerFly player, AudioPlayer audioPlayer) {
        this.player = player;
        this.audioPlayer = audioPlayer;
        loadImgs();
        allEnemies = new ArrayList<>();
        activeEnemiesOnScreen = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.killedEnemies = new ArrayList<>();
    }

    private void loadImgs() {
        BufferedImage targetImg = LoadSave.getFlyImageSprite(LoadSave.TARGET_SPRITE);
        this.targetAnimations = GetEnemyAnimations(
            targetImg, TARGET_SPRITE_SIZE, TARGET_SPRITE_SIZE, 2, 4);
        
        BufferedImage droneImg = LoadSave.getFlyImageSprite(LoadSave.DRONE_SPRITE);
        this.droneAnimations = GetEnemyAnimations(
            droneImg, DRONE_SPRITE_SIZE, DRONE_SPRITE_SIZE, 2, 3);
        
        BufferedImage smallShipImg = LoadSave.getFlyImageSprite(LoadSave.SMALLSHIP_SPRITE);
        this.smallShipAnimations = GetEnemyAnimations(
            smallShipImg, SMALLSHIP_SPRITE_SIZE, SMALLSHIP_SPRITE_SIZE, 2, 4);
        
        BufferedImage octadroneImg = LoadSave.getFlyImageSprite(LoadSave.OCTADRONE_SPRITE);
        this.octadroneAnimations = GetEnemyAnimations(
            octadroneImg, OCTADRONE_SPRITE_SIZE, OCTADRONE_SPRITE_SIZE, 2, 4);
        
        BufferedImage tankdroneImg = LoadSave.getFlyImageSprite(LoadSave.TANKDRONE_SPRITE);
        this.tankdroneAnimations = GetEnemyAnimations(
            tankdroneImg, TANKDRONE_SPRITE_SIZE, TANKDRONE_SPRITE_SIZE, 2, 4);
        
        BufferedImage blasterdroneImg = LoadSave.getFlyImageSprite(LoadSave.BLASTERDRONE_SPRITE);
        this.blasterdroneAnimations = GetEnemyAnimations(
            blasterdroneImg, BLASTERDRONE_SPRITE_SIZE, BLASTERDRONE_SPRITE_SIZE, 2, 4);

        this.explosionAnimation = new BufferedImage[5];
        BufferedImage explosionImg = LoadSave.getFlyImageSprite(LoadSave.EXPLOSION);
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

        List<String> enemyData = LoadSave.getFlyLevelData(lvl);
        for (String line : enemyData) {
            String[] lineData = line.split(";");

            if (lineData[0].equals("target")) {
                int size = TARGET_SPRITE_SIZE * 3;
                allEnemies.add(GetEnemy(TARGET, lineData, size, size, targetAnimations));
            }
            else if (lineData[0].equals("drone")) {
                int width = 78;
                int height = 66;
                allEnemies.add(GetEnemy(DRONE, lineData, width, height, droneAnimations));
            }
            else if (lineData[0].equals("smallShip")) {
                int width = 60;
                int height = 30; 
                allEnemies.add(GetEnemy(SMALL_SHIP, lineData, width, height, smallShipAnimations));
            }
            else if (lineData[0].equals("octaDrone")) {
                int width = 80;
                int height = 80;
                allEnemies.add(GetEnemy(OCTADRONE, lineData, width, height, octadroneAnimations));
            }
            else if (lineData[0].equals("tankDrone")) {
                int width = 80;
                int height = 90;
                allEnemies.add(GetEnemy(TANKDRONE, lineData, width, height, tankdroneAnimations));
            }
            else if (lineData[0].equals("blasterDrone")) {
                int width = 60;
                int height = 90;
                allEnemies.add(GetEnemy(BLASTERDRONE, lineData, width, height, blasterdroneAnimations));
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
                if (player.teleportDamagesEnemy(enemy.getHitbox())) {
                    enemy.takeDamage(teleportDmg);
                    checkIfDead(enemy);
                }
                else if (player.collidesWithEnemy(enemy.getHitbox())) {   // Also pushes player in opposite direction
                    enemy.takeDamage(collisionDmg);
                    checkIfDead(enemy);
                }
            }
        }
    }

    private void checkIfDead(Enemy enemy) {
        if (enemy.isDead()) {
            audioPlayer.playSFX(Audio.SFX_SMALL_EXPLOSION);
            this.addExplosion(enemy.getHitbox());
            increaseKilledEnemies(enemy.getType());
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
    public void addExplosion(Rectangle2D.Float hb) {
        float x = (hb.x + hb.width/2) - ((EXPLOSION_SPRITE_SIZE * 3) / 2);
        float y = (hb.y + hb.height/2) - ((EXPLOSION_SPRITE_SIZE * 3) / 2);
        explosions.add(new Explosion((int) x, (int) y));
    }

    public void draw(Graphics g) {
        ArrayList<Enemy> copy = new ArrayList<>(activeEnemiesOnScreen);
        for (Enemy enemy : copy) {  
            enemy.draw(g);
            //enemy.drawHitbox(g);
        }
        for (Explosion ex : explosions) {   // ConcurrentModificationException
            g.drawImage(
                explosionAnimation[ex.getAniIndex()],
                (int) (ex.getX() * Game.SCALE),
                (int) (ex.getY() * Game.SCALE),
                (int) (EXPLOSION_SPRITE_SIZE * 3 * Game.SCALE),
                (int) (EXPLOSION_SPRITE_SIZE * 3 * Game.SCALE),
                null);
        }
    }

    public ArrayList<Enemy> getActiveEnemiesOnScreen() {
        return this.activeEnemiesOnScreen;
    }

    public void increaseKilledEnemies(int enemyType) {
        this.killedEnemies.add(enemyType);
        this.player.setKilledEnemies(killedEnemies.size());
    }

    public ArrayList<Integer> getKilledEnemies() {
        return this.killedEnemies;
    }

    /** Is used with the 'startAt()'-method */
    public void moveAllEnemies(int yOffset) {
        for (Enemy enemy : allEnemies) {
            enemy.getHitbox().y -= yOffset;
        }
    }

    public void resetTo(float y) {
        for (Enemy enemy : allEnemies) {
            enemy.resetTo(y);
        }
    }
}
