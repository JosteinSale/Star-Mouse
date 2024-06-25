package entities.flying.enemies;

import static utils.Constants.Flying.HitboxConstants.*;
import static utils.Constants.Flying.SpriteSizes.*;
import static utils.Constants.Flying.TypeConstants.*;
import static utils.HelpMethods2.GetEnemy;
import static utils.HelpMethods2.GetEnemyAnimations;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import entities.flying.PlayerFly;
import main_classes.Game;
import projectiles.Explosion;
import utils.ResourceLoader;
import utils.Constants.Audio;

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
    private BufferedImage[][] reaperdroneAnimations;
    private BufferedImage[][] flamedroneAnimations;
    private BufferedImage[][] waspdroneAnimations;
    private BufferedImage[][] kamikazedroneAnimations;
    private BufferedImage[] explosionAnimation;
    private ArrayList<Explosion> explosions;
    private int collisionDmg = 10;
    private int teleportDmg = 80;
    private ArrayList<Integer> killedEnemies;           // Contains the enemyTypes
    private ArrayList<Integer> killedEnemiesAtCheckpoint;

    public EnemyManager(PlayerFly player, AudioPlayer audioPlayer) {
        this.player = player;
        this.audioPlayer = audioPlayer;
        loadImgs();
        allEnemies = new ArrayList<>();
        activeEnemiesOnScreen = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.killedEnemies = new ArrayList<>();
        this.killedEnemiesAtCheckpoint = new ArrayList<>();
    }

    private void loadImgs() {
        BufferedImage targetImg = ResourceLoader.getFlyImageSprite(ResourceLoader.TARGET_SPRITE);
        this.targetAnimations = GetEnemyAnimations(
            targetImg, TARGET_SPRITE_SIZE, TARGET_SPRITE_SIZE, 2, 4);
        
        BufferedImage droneImg = ResourceLoader.getFlyImageSprite(ResourceLoader.DRONE_SPRITE);
        this.droneAnimations = GetEnemyAnimations(
            droneImg, DRONE_SPRITE_SIZE, DRONE_SPRITE_SIZE, 2, 3);
        
        BufferedImage smallShipImg = ResourceLoader.getFlyImageSprite(ResourceLoader.SMALLSHIP_SPRITE);
        this.smallShipAnimations = GetEnemyAnimations(
            smallShipImg, SMALLSHIP_SPRITE_SIZE, SMALLSHIP_SPRITE_SIZE, 2, 4);
        
        BufferedImage octadroneImg = ResourceLoader.getFlyImageSprite(ResourceLoader.OCTADRONE_SPRITE);
        this.octadroneAnimations = GetEnemyAnimations(
            octadroneImg, OCTADRONE_SPRITE_SIZE, OCTADRONE_SPRITE_SIZE, 2, 4);
        
        BufferedImage tankdroneImg = ResourceLoader.getFlyImageSprite(ResourceLoader.TANKDRONE_SPRITE);
        this.tankdroneAnimations = GetEnemyAnimations(
            tankdroneImg, TANKDRONE_SPRITE_SIZE, TANKDRONE_SPRITE_SIZE, 2, 4);
        
        BufferedImage blasterdroneImg = ResourceLoader.getFlyImageSprite(ResourceLoader.BLASTERDRONE_SPRITE);
        this.blasterdroneAnimations = GetEnemyAnimations(
            blasterdroneImg, BLASTERDRONE_SPRITE_SIZE, BLASTERDRONE_SPRITE_SIZE, 2, 4);
        
        BufferedImage reaperdroneImg = ResourceLoader.getFlyImageSprite(ResourceLoader.REAPERDRONE_SPRITE);
        this.reaperdroneAnimations = GetEnemyAnimations(
            reaperdroneImg, REAPERDRONE_SPRITE_WIDTH, REAPERDRONE_SPRITE_HEIGHT, 2, 4);
        
        BufferedImage flamedroneImg = ResourceLoader.getFlyImageSprite(ResourceLoader.FLAMEDRONE_SPRITE);
        this.flamedroneAnimations = GetEnemyAnimations(
            flamedroneImg, FLAMEDRONE_SPRITE_WIDTH, FLAMEDRONE_SPRITE_HEIGHT, 3, 6);
        
        BufferedImage waspdroneImg = ResourceLoader.getFlyImageSprite(ResourceLoader.WASPDRONE_SPRITE);
        this.waspdroneAnimations = GetEnemyAnimations(
            waspdroneImg, WASPDRONE_SPRITE_SIZE, WASPDRONE_SPRITE_SIZE, 2, 4);
        
        BufferedImage kamikazeImg = ResourceLoader.getFlyImageSprite(ResourceLoader.KAMIKAZEDRONE_SPRITE);
        this.kamikazedroneAnimations = GetEnemyAnimations(
            kamikazeImg, KAMIKAZEDRONE_SPRITE_SIZE, KAMIKAZEDRONE_SPRITE_SIZE, 2, 4);

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

            if (lineData[0].equals("target")) {
                int size = TARGET_HITBOX_SIZE;
                allEnemies.add(GetEnemy(TARGET, lineData, size, size, targetAnimations));
            }
            else if (lineData[0].equals("drone")) {
                int width = DRONE_HITBOX_W;
                int height = DRONE_HITBOX_H;
                allEnemies.add(GetEnemy(DRONE, lineData, width, height, droneAnimations));
            }
            else if (lineData[0].equals("smallShip")) {
                int width = SMALLSHIP_HITBOX_W;
                int height = SMALLSHIP_HITBOX_H; 
                allEnemies.add(GetEnemy(SMALLSHIP, lineData, width, height, smallShipAnimations));
            }
            else if (lineData[0].equals("octaDrone")) {
                int width = OCTADRONE_HITBOX_SIZE;
                int height = OCTADRONE_HITBOX_SIZE;
                allEnemies.add(GetEnemy(OCTADRONE, lineData, width, height, octadroneAnimations));
            }
            else if (lineData[0].equals("tankDrone")) {
                int width = TANKDRONE_HITBOX_W;
                int height = TANKDRONE_HITBOX_H;
                allEnemies.add(GetEnemy(TANKDRONE, lineData, width, height, tankdroneAnimations));
            }
            else if (lineData[0].equals("blasterDrone")) {
                int width = BLASTERDRONE_HITBOX_W;
                int height = BLASTERDRONE_HITBOX_H;
                allEnemies.add(GetEnemy(BLASTERDRONE, lineData, width, height, blasterdroneAnimations));
            }
            else if (lineData[0].equals("reaperDrone")) {
                int width = REAPERDRONE_HITBOX_W;
                int height = REAPERDRONE_HITBOX_H;
                allEnemies.add(GetEnemy(REAPERDRONE, lineData, width, height, reaperdroneAnimations));
            }
            else if (lineData[0].equals("flameDrone")) {
                int width = FLAMEDRONE_HITBOX_SIZE;
                int height = FLAMEDRONE_HITBOX_SIZE;
                allEnemies.add(GetEnemy(FLAMEDRONE, lineData, width, height, flamedroneAnimations));
            }
            else if (lineData[0].equals("waspDrone")) {
                int width = WASPDRONE_HITBOX_SIZE;
                int height = WASPDRONE_HITBOX_SIZE;
                allEnemies.add(GetEnemy(WASPDRONE, lineData, width, height, waspdroneAnimations));
            }
            else if (lineData[0].equals("kamikazeDrone")) {
                int width = KAMIKAZEDRONE_HITBOX_SIZE;
                int height = KAMIKAZEDRONE_HITBOX_SIZE;
                KamikazeDrone kamikazeDrone = (KamikazeDrone) GetEnemy(KAMIKAZEDRONE, lineData, width, height, kamikazedroneAnimations);
                kamikazeDrone.setPlayer(this.player);
                allEnemies.add(kamikazeDrone);
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
            //enemy.drawHitbox(g);
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
        this.killedEnemies.add(enemyType);
        this.player.setKilledEnemies(killedEnemies.size() + killedEnemiesAtCheckpoint.size());
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
