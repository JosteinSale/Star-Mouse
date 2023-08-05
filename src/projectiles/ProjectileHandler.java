package projectiles;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.flying.Enemy;
import entities.flying.EnemyManager;
import entities.flying.PlayerFly;
import main.Game;
import utils.LoadSave;

import static utils.Constants.Flying.Sprites.*;
import static utils.Constants.Flying.TypeConstants.DRONE;
import static utils.Constants.Flying.TypeConstants.OCTADRONE;
import static utils.Constants.Flying.TypeConstants.BOMB_PROJECTILE;
import static utils.HelpMethods.IsSolid;

public class ProjectileHandler {
    private PlayerFly player;
    private EnemyManager enemyManager;
    private ArrayList<Projectile> allProjectiles;
    private ArrayList<Integer> projectilesToRemove;
    private ArrayList<ProjectileHit> projectileHits;
    private Rectangle2D.Float screenBox;

    private BufferedImage clImg;
    private BufferedImage[] plPrjctImgs;          // 0 = BLUE, 1 = GREEN
    private BufferedImage bombImg;
    private BufferedImage dronePrjctImg;
    private BufferedImage octadronePrjctImg;
    private BufferedImage[] hitAnimation;

    private boolean powerUp = false;
    private boolean spaceIsPressed;
    private boolean bIsPressed;
    private int shootTick = 0;
    private int shootBuffer = 10;
    private int shootDamage = 10;
    private int nrOfBombs = 10;

    private float fgSpeed;

    public ProjectileHandler(PlayerFly player, EnemyManager enemyManager, BufferedImage clImage) {
        this.player = player;
        this.enemyManager = enemyManager;
        loadImgs();
        this.clImg = clImage;
        this.allProjectiles = new ArrayList<>();
        this.projectilesToRemove = new ArrayList<>();
        this.projectileHits = new ArrayList<>();
        this.screenBox = new Rectangle2D.Float(0, 0, Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
    }

    private void loadImgs() {
        this.plPrjctImgs = new BufferedImage[2];
        this.plPrjctImgs[0] = LoadSave.getFlyImageSprite(LoadSave.PLAYER_PROJECTILE_BLUE);
        this.plPrjctImgs[1] = LoadSave.getFlyImageSprite(LoadSave.PLAYER_PROJECTILE_GREEN);
        this.bombImg = LoadSave.getFlyImageSprite(LoadSave.BOMB_SPRITE);
        this.dronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.DRONE_PROJECTILE);
        this.octadronePrjctImg = LoadSave.getFlyImageSprite(LoadSave.OCTADRONE_PROJECTILE);

        this.hitAnimation = new BufferedImage[4];
        BufferedImage hitImg = LoadSave.getFlyImageSprite(LoadSave.PROJECTILE_HIT);
        for (int i = 0; i < hitAnimation.length; i++) {
            hitAnimation[i] = hitImg.getSubimage(
                i * PRJT_HIT_SPRITE_SIZE, 0, PRJT_HIT_SPRITE_SIZE, PRJT_HIT_SPRITE_SIZE);
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
    }

    private void checkPlayerShoot() {
        // Kan også sjekke skyting av bomber her
        if (spaceIsPressed && shootTick == 0) {
            shootTick = shootBuffer;
            this.addPlayerProjectile(player.getHitbox().x, player.getHitbox().y);
        }
        if (bIsPressed && shootTick == 0) {
            shootTick = shootBuffer;
            this.addBombProjectile(player.getHitbox().x, player.getHitbox().y);
        }
    }

    private void addPlayerProjectile(float xPos, float yPos) {
        Rectangle2D.Float hitbox1 = new Rectangle2D.Float(xPos - 8, yPos - 30, PLAYER_PRJT_SPRITE_W, PLAYER_PRJT_SPRITE_H);
        Rectangle2D.Float hitbox2 = new Rectangle2D.Float(xPos + 43, yPos - 30, PLAYER_PRJT_SPRITE_W, PLAYER_PRJT_SPRITE_H);
        int imgSelection = 0;
        if (powerUp) {
            imgSelection = 1;
        }
        this.allProjectiles.add(new PlayerProjectile(hitbox1, powerUp, plPrjctImgs[imgSelection]));
        this.allProjectiles.add(new PlayerProjectile(hitbox2, powerUp, plPrjctImgs[imgSelection]));
    }

    private void addBombProjectile(float xPos, float yPos) {
        nrOfBombs--;
        if (nrOfBombs < 0) {
            nrOfBombs = 0;
        }
        if (nrOfBombs > 0) {
            Rectangle2D.Float hitbox = new Rectangle2D.Float(
                xPos + player.getHitbox().width/2 - BOMB_SPRITE_SIZE/2, yPos - 50, 
                BOMB_SPRITE_SIZE, BOMB_SPRITE_SIZE);
            this.allProjectiles.add(new BombProjectile(hitbox, bombImg));
        }
    }

    private void checkEnemeyShoot() {
        for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
            if (enemy.canShoot()) {
                addEnemeyProjectile(enemy.getType(), enemy.getHitbox());
                enemy.resetShootTick();
            }
        }
    }

    private void addEnemeyProjectile(int type, Rectangle2D.Float hitbox) {
        if (type == DRONE) {
            Rectangle2D.Float prjctHitbox = new Rectangle2D.Float(
                hitbox.x + 25, hitbox.y + 66, 32, 33);
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
    }

    private void updatePlayerShootTick() {
        shootTick--;
        if (shootTick < 0) {
            shootTick = 0;
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
                allProjectiles.remove(i);  
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
                    checkBombCollision();
                } else {
                    for (Enemy enemy : enemyManager.getActiveEnemiesOnScreen()) {
                        if (p.getHitbox().intersects(enemy.getHitbox())) {
                            p.setActive(false);
                            enemy.takeDamage(p.getDamage());
                            if (enemy.isDead()) {
                                enemyManager.addExplosion(enemy.getHitbox());
                            }
                            break;
                        }
                    }
                    if (p.getHitbox().intersects(player.getHitbox())) {
                        p.setActive(false);
                        player.takeShootDamage(p.getDamage());
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

    private void checkBombCollision() {

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
        for (Projectile p : allProjectiles) {
            if (p.isActive()) {
                p.draw(g);
                p.drawHitbox(g);
            }
        }
        for (ProjectileHit ph : projectileHits) {
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
    }

    public void setSpacePressed(boolean pressed) {
        this.spaceIsPressed = pressed;
    }

    public void setBPressed(boolean pressed) {
        this.bIsPressed = pressed;
    }

    public void resetShootTick() {
        this.shootTick = 0;
    }

    public void setPowerup(boolean powerup) {
        this.powerUp = powerup;
    }

    public void addBombToInventory() {
        this.nrOfBombs++;
    }
}
