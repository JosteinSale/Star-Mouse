package projectiles;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.flying.PlayerFly;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import main.Game;
import misc.ProgressValues;
import utils.Constants.Audio;
import utils.LoadSave;

import static utils.Constants.Flying.SpriteSizes.*;
import static utils.Constants.Flying.TypeConstants.DRONE;
import static utils.Constants.Flying.TypeConstants.FLAMEDRONE;
import static utils.Constants.Flying.TypeConstants.OCTADRONE;
import static utils.Constants.Flying.TypeConstants.REAPERDRONE;
import static utils.Constants.Flying.TypeConstants.WASPDRONE;
import static utils.Constants.Flying.TypeConstants.BLASTERDRONE;
import static utils.Constants.Flying.TypeConstants.BOMB_PROJECTILE;
import static utils.HelpMethods.IsSolid;

import static utils.Constants.Audio;

public class ProjectileHandler {
    private Game game;
    private ProgressValues progValues;
    private AudioPlayer audioPlayer;
    private PlayerFly player;
    private EnemyManager enemyManager;
    private ArrayList<Projectile> allProjectiles;   // projectiles on screen
    private ArrayList<Integer> projectilesToRemove;
    private ArrayList<ProjectileHit> projectileHits;
    private ArrayList<BombExplosion> bombExplosions;         
    private Rectangle2D.Float screenBox;

    private BufferedImage clImg;
    private BufferedImage[] plPrjctImgs;          // 0 = BLUE, 1 = GREEN
    private BufferedImage bombImg;
    private BufferedImage dronePrjctImg;
    private BufferedImage octadronePrjctImg;
    private BufferedImage reaperdronePrjctImg;
    private BufferedImage flamedronePrjctImg;
    private BufferedImage[] hitAnimation;
    private BufferedImage[] bombExplosionAnimation;

    private boolean powerUp = false;
    private int lazerShootTick = 0;
    private int lazerShootBuffer = 10;
    private int bombShootBuffer = 30;  
    private int bombShootTick = 0;
    private int explosionDamage = 300;
    private int nrOfBombs;
    private int nrOfBombsAtCheckpoint;

    private float fgSpeed;

    public ProjectileHandler(Game game, AudioPlayer audioPlayer, PlayerFly player, EnemyManager enemyManager) {
        this.game = game;
        this.progValues = game.getExploring().getProgressValues();
        this.nrOfBombs = progValues.getBombs();
        this.nrOfBombsAtCheckpoint = 0;
        this.audioPlayer = audioPlayer;
        this.player = player;
        this.enemyManager = enemyManager;
        loadImgs();
        this.allProjectiles = new ArrayList<>();
        this.projectilesToRemove = new ArrayList<>();
        this.projectileHits = new ArrayList<>();
        this.bombExplosions = new ArrayList<>();
        this.screenBox = new Rectangle2D.Float(0, 0, Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
    }

    private void loadImgs() {
        this.plPrjctImgs = new BufferedImage[2];
        this.plPrjctImgs[0] = LoadSave.getFlyImageSprite(LoadSave.PLAYER_PROJECTILE_BLUE);
        this.plPrjctImgs[1] = LoadSave.getFlyImageSprite(LoadSave.PLAYER_PROJECTILE_GREEN);
        this.bombImg = LoadSave.getFlyImageSprite(LoadSave.BOMB_SPRITE);
        this.dronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.DRONE_PROJECTILE);
        this.octadronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.OCTADRONE_PROJECTILE);
        this.reaperdronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.REAPERDRONE_PROJECTILE);
        this.flamedronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.FLAME_PROJECTILE);

        this.hitAnimation = new BufferedImage[4];
        BufferedImage hitImg = LoadSave.getFlyImageSprite(LoadSave.PROJECTILE_HIT);
        for (int i = 0; i < hitAnimation.length; i++) {
            hitAnimation[i] = hitImg.getSubimage(
                i * PRJT_HIT_SPRITE_SIZE, 0, PRJT_HIT_SPRITE_SIZE, PRJT_HIT_SPRITE_SIZE);
        }
        this.bombExplosionAnimation = new BufferedImage[11];
        BufferedImage explosionImg = LoadSave.getFlyImageSprite(LoadSave.BOMB_EXPLOSION_SPRITE);
        for (int i = 0; i < bombExplosionAnimation.length; i++) {
            bombExplosionAnimation[i] = explosionImg.getSubimage(
                i * BOMBEXPLOSION_SPRITE_WIDTH, 0, BOMBEXPLOSION_SPRITE_WIDTH, BOMBEXPLOSION_SPRITE_HEIGHT);
        }
    }

    public void update(float yLevelOffset, float xLevelOffset, float fgCurSpeed) {
        this.fgSpeed = fgCurSpeed;
        checkPlayerShoot();
        checkEnemeyShoot();
        updatePlayerShootTick();
        moveProjectiles();
        removeOffScreenProjectiles();
        checkProjectileCollisions(yLevelOffset, xLevelOffset);  //checks 1: enemies, 2: maps
        updateHits(fgCurSpeed);
        updateBombExplosions(fgCurSpeed);
    }

    private void checkPlayerShoot() {
        if (game.interactIsPressed && lazerShootTick == 0) {
            lazerShootTick = lazerShootBuffer;
            this.addPlayerProjectile(player.getHitbox().x, player.getHitbox().y);
            audioPlayer.playSFX(Audio.SFX_LAZER);
        }
        if (game.bombIsPressed && bombShootTick == 0 && nrOfBombs > 0) {
            nrOfBombs--;
            audioPlayer.playSFX(Audio.SFX_BOMBSHOOT);
            bombShootTick = bombShootBuffer;
            this.addBombProjectile(player.getHitbox().x, player.getHitbox().y);
            this.player.setBombs(nrOfBombs);
        }
    }

    private void addPlayerProjectile(float xPos, float yPos) {
        Rectangle2D.Float hitbox1 = new Rectangle2D.Float(
            xPos - 8, yPos - 30, PLAYER_PRJT_SPRITE_W, PLAYER_PRJT_SPRITE_H);
        Rectangle2D.Float hitbox2 = new Rectangle2D.Float(
            xPos + 43, yPos - 30, PLAYER_PRJT_SPRITE_W, PLAYER_PRJT_SPRITE_H);
        int imgSelection = 0;
        if (powerUp) {
            imgSelection = 1;
        }
        this.allProjectiles.add(
            new PlayerProjectile(hitbox1, powerUp, progValues.getLazerDmg(), plPrjctImgs[imgSelection]));
        this.allProjectiles.add(
            new PlayerProjectile(hitbox2, powerUp, progValues.getLazerDmg(), plPrjctImgs[imgSelection]));
    }

    private void addBombProjectile(float xPos, float yPos) {
        Rectangle2D.Float hitbox = new Rectangle2D.Float(
            xPos + player.getHitbox().width / 2 - BOMB_SPRITE_SIZE / 2, 
            yPos - 50,
            BOMB_SPRITE_SIZE, 
            BOMB_SPRITE_SIZE);
        this.allProjectiles.add(new BombProjectile(hitbox, bombImg));
    }

    private void checkEnemeyShoot() {
        for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
            if (enemy.canShoot()) {
                addEnemeyProjectile(enemy.getType(), enemy.getHitbox(), enemy.getDir());
                enemy.resetShootTick();
            }
        }
    } 

    private void addEnemeyProjectile(int type, Rectangle2D.Float hitbox, int dir) {
        if (type == DRONE) {
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                hitbox.x + 25, hitbox.y + 66, 32, 33);
            this.allProjectiles.add(new DroneProjectile(prjctHitbox, dronePrjctImg));
        }
        else if (type == BLASTERDRONE) {
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                hitbox.x + 15, hitbox.y + 90, 32, 33);
            this.allProjectiles.add(new DroneProjectile(prjctHitbox, dronePrjctImg));
        }
        else if (type == OCTADRONE) {
            double radius = hitbox.getWidth();
            for (int i = 0; i < 8; i++) {
                double angle = Math.toRadians(((double) i / 8) * 360d);
                double x = (Math.cos(angle) * radius) + hitbox.x + hitbox.width/3; 
                double y = (Math.sin(angle) * radius) + hitbox.y + hitbox.height/3;
                int xSpeed = (int) (Math.cos(angle) * 4);
                int ySpeed = (int) ((Math.sin(angle) * 4) + fgSpeed);
                Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                (float) x, (float) y, OCTADRONE_PRJT_SPRITE_SIZE - 3, OCTADRONE_PRJT_SPRITE_SIZE - 3);
                this.allProjectiles.add(new OctaProjectile(prjctHitbox, octadronePrjctImg, xSpeed, ySpeed));
            }
        }
        else if (type == REAPERDRONE) {
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                hitbox.x + 85, hitbox.y + 160, 300, 24);
            this.allProjectiles.add(new ReaperProjectile(prjctHitbox, reaperdronePrjctImg));
        }
        else if (type == FLAMEDRONE) {
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                (hitbox.x - 130), (hitbox.y + 160), 378,195);
            this.allProjectiles.add(new FlameProjectile(prjctHitbox, flamedronePrjctImg));
        }
        else if (type == WASPDRONE) {
            if (dir == 1) {  // Facing right
                int xSpeed = 3;
                int ySpeed = 4;
                Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                    hitbox.x + 75, hitbox.y + 95, 28, 28);
                this.allProjectiles.add(new OctaProjectile(prjctHitbox, octadronePrjctImg, xSpeed, ySpeed));
            }
            else {  // Facing left
                int xSpeed = -3;
                int ySpeed = 4;
                Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                    hitbox.x + 5, hitbox.y + 95, 28, 28);
                this.allProjectiles.add(new OctaProjectile(prjctHitbox, octadronePrjctImg, xSpeed, ySpeed));
            }
        }
    }

    private void updatePlayerShootTick() {
        lazerShootTick--;
        if (lazerShootTick < 0) {
            lazerShootTick = 0;
        }
        bombShootTick--;
        if (bombShootTick < 0) {
            bombShootTick = 0;
        }
    }

    private void moveProjectiles() {
        for (Projectile p : allProjectiles) {
            p.getHitbox().x += p.getXSpeed();
            p.getHitbox().y += p.getYSpeed();
        }
    }

    private void removeOffScreenProjectiles() {
        projectilesToRemove.clear();
        int index = 0;
        for (Projectile p : allProjectiles) {
            if (!p.getHitbox().intersects(screenBox)) {
                projectilesToRemove.add(index);
            }
            index += 1;
        }
        if (!projectilesToRemove.isEmpty()) {
            int indexAdjustment = 0;
            for (int i : projectilesToRemove) {
                i -= indexAdjustment;    // Trengs siden indeksene forskyves
                allProjectiles.remove(i);    // Tregt
                indexAdjustment += 1;
            }
        }
    }

    /** If a projectile collides with the map or with an enemy/player,
     *  it's set to inactive. */
    private void checkProjectileCollisions(float yLevelOffset, float xLevelOffset) {
        for (Projectile p : allProjectiles) {  
            if (p.isActive()) {
                if (p.getType() == BOMB_PROJECTILE) {
                    checkBombCollision(p, yLevelOffset, xLevelOffset);
                } else {
                    for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
                        if (!enemy.isDead()) {
                            if (p.getHitbox().intersects(enemy.getHitbox())) {
                                p.setActive(false);
                                enemy.takeDamage(p.getDamage());
                                enemyManager.checkIfDead(enemy);
                            }
                        }
                    }
                    if (p.getHitbox().intersects(player.getHitbox())) {
                        p.setActive(false);
                        player.takeShootDamage(p.getDamage());
                        audioPlayer.playSFX(Audio.SFX_HURT);
                        projectileHits.add(new ProjectileHit(
                                (int) (player.getHitbox().x - 10),
                                (int) (player.getHitbox().y),
                                1));   // Big hit
                        break;
                    }
                    p.updateCollisionPixels();
                    for (int[] cor : p.getCollisionPixels()) {
                        int xPos = cor[0] + (int) (xLevelOffset / 3);
                        int yPos = cor[1] - (int) (yLevelOffset / 3);
                        if (IsSolid(xPos, yPos, clImg)) {
                            p.setActive(false);
                            projectileHits.add(new ProjectileHit((cor[0] * 3) - 10, cor[1] * 3, 0));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void checkBombCollision(Projectile p, float yLevelOffset, float xLevelOffset) {
        for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
            if (p.getHitbox().intersects(enemy.getHitbox())) {
                p.setActive(false);
                addBombExplosion(p.getHitbox());
                audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
                return;
            }
        }
        p.updateCollisionPixels();
        for (int[] cor : p.getCollisionPixels()) {
            int xPos = cor[0] + (int) (xLevelOffset / 3);
            int yPos = cor[1] - (int) (yLevelOffset / 3);
            if (IsSolid(xPos, yPos, clImg)) {
                p.setActive(false);
                addBombExplosion(p.getHitbox());
                audioPlayer.playSFX(Audio.SFX_BIG_EXPLOSION);
                return;
            }
        }
    }

    private void addBombExplosion(Rectangle2D.Float prjctHb) {
        bombExplosions.add(new BombExplosion(
                    bombExplosionAnimation, (int) (prjctHb.x + 5), (int) (prjctHb.y + 5)));
    }

    private void updateBombExplosions(float fgSpeed) {
        int toRemove = 0;
        for (BombExplosion b : bombExplosions) {
            if (b.isDone()) {
                toRemove += 1;
            }
            else {
                b.update(fgSpeed);
                if (b.explosionHappens()) {
                    for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
                        enemy.takeDamage(explosionDamage);
                        if (enemy.isDead()) {
                            enemyManager.addSmallExplosion(enemy.getHitbox());
                            enemyManager.increaseKilledEnemies(enemy.getType());
                        }
                    }
                }
            }
        }
        while (toRemove > 0) {
            bombExplosions.remove(0);
            toRemove -= 1;
        }
    }

    private void updateHits(float fgCurSpeed) {
        int toRemove = 0;
        for (ProjectileHit ph : projectileHits) {
            ph.update();
            if (ph.isDone()) {
                toRemove += 1;
            }
        }
        while (toRemove > 0) {
            projectileHits.remove(0);
            toRemove -= 1;
        }
    }

    public void draw(Graphics g) {
        ArrayList<Projectile> copy = new ArrayList<>(allProjectiles);
        for (Projectile p : copy) {
            if (p.isActive()) {
                p.draw(g);
                //p.drawHitbox(g);
            }
        }
        for (ProjectileHit ph : projectileHits) {   // concurrentModificationException
            if (ph.getType() == 0) {         // Hitting the map
                g.drawImage(
                hitAnimation[ph.getAniIndex()],
                (int) (ph.getX() * Game.SCALE),
                (int) (ph.getY() * Game.SCALE),
                (int) (PRJT_HIT_SPRITE_SIZE * 3 * Game.SCALE),
                (int) (PRJT_HIT_SPRITE_SIZE * 3 * Game.SCALE),
                null);
            }
            else if (ph.getType() == 1) {      // Hitting player
                g.drawImage(
                hitAnimation[ph.getAniIndex()],
                (int) (ph.getX() * Game.SCALE),
                (int) (ph.getY() * Game.SCALE),
                (int) (PRJT_HIT_SPRITE_SIZE * 5 * Game.SCALE),
                (int) (PRJT_HIT_SPRITE_SIZE * 5 * Game.SCALE),
                null);
            }
        }
        ArrayList<BombExplosion> copy2 = new ArrayList<>(bombExplosions);
        for (BombExplosion b : copy2) {
            b.draw(g);
        }
    }

    public void resetShootTick() {
        this.lazerShootTick = 0;
    }

    public void setPowerup(boolean powerup) {
        this.powerUp = powerup;
    }

    public void addBombToInventory() {
        this.nrOfBombs++;
        this.player.setBombs(nrOfBombs);
    }

    public int getBombsAtEndOfLevel() {
        return this.nrOfBombs;
    }

    public void setBombs(int amount) {
        nrOfBombs = amount;
        player.setBombs(amount);
    }

    public void setClImg(BufferedImage clImg) {
        this.clImg = clImg;
    }

    public void checkPointReached() {
        this.nrOfBombsAtCheckpoint = nrOfBombs;
    }

    public void resetBombs(boolean toCheckPoint) {
        if (toCheckPoint) {
            this.nrOfBombs = nrOfBombsAtCheckpoint;
        }
        else {
            this.setBombs(game.getExploring().getProgressValues().getBombs());
        }
    }

    public void reset() {
        allProjectiles.clear();
        projectilesToRemove.clear();
        projectileHits.clear();
        bombExplosions.clear();
        powerUp = false;
        lazerShootTick = 0;
        bombShootTick = 0;
    }
}
