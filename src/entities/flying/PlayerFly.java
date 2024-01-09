package entities.flying;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;

import static utils.Constants.Flying.ActionConstants.*;
import static utils.Constants.Flying.Sprites.SHIP_SPRITE_WIDTH;
import static utils.Constants.Flying.Sprites.SHIP_SPRITE_HEIGHT;
import static utils.HelpMethods.IsSolid;

import entities.Entity;
import main.Game;
import ui.StatusDisplay;
import utils.LoadSave;
import utils.Constants.Audio;

public class PlayerFly extends Entity {
    private Game game;
    private AudioPlayer audioPlayer;
    private BufferedImage clImg;
    private Font statusFont;
    private BufferedImage[][] animations;
    private BufferedImage tpShadowImg;
    private ShipFlame flame;
    private StatusDisplay statusDisplay;
    private Rectangle2D.Float teleportHitbox;

    private int planeAction;
    private float xSpeed = 0;
    private float ySpeed = 0;
    private float acceleration = 1.0f;
    private float playerMaxSpeed = 8f;
    private boolean visible = true;
    private float[] collisionXs = new float[9];
    private float[] collisionYs = new float[9];
    private int edgeDist = 20;
    private int pushDistance = 40;
    private int teleportDistance = 250;
    private int teleportKillWidth = 100;
    private int teleportKillOffset;
    private int maxHP = 100;
    private int HP = maxHP;
    private int collisionDmg = 10;
    private int flipX = 1;      // 1 = høyre, -1 = venstre. Brukes i checkTeleportCollision

    private int aniTick = 0;
    private int aniTickPerFrame = 3;          // Antall ticks per gang animasjonen oppdateres
    private int aniIndex = 0;

    private int iFrames = 20;
    private int iFrameCount = 0;
    private int teleportBuffer = 0;
    private int teleportCoolDown = 10;

    public PlayerFly(Game game, Float hitbox) {
        super(hitbox);
        this.game = game;
        this.audioPlayer = game.getAudioPlayer();
        this.tpShadowImg = LoadSave.getFlyImageSprite("teleport_shadow.png");
        loadAnimations();
        updateCollisionPixels();
        this.statusFont = LoadSave.getInfoFont();
        this.flame = new ShipFlame();
        this.teleportHitbox = new Rectangle2D.Float(
            hitbox.x, hitbox.y, teleportKillWidth, hitbox.height);
        this.teleportKillOffset = (int) (teleportDistance - hitbox.width - teleportKillWidth) / 2;
        this.statusDisplay = new StatusDisplay();
        this.statusDisplay.setMaxHP(maxHP);
        this.statusDisplay.setHP(maxHP);
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.getFlyImageSprite(LoadSave.SHIP_SPRITES);
        this.animations = new BufferedImage[7][6];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[0].length; i++) {
                animations[j][i] = img.getSubimage(
                    i * SHIP_SPRITE_WIDTH, 
                    j * SHIP_SPRITE_HEIGHT, SHIP_SPRITE_WIDTH, SHIP_SPRITE_HEIGHT);
            }
        }
    }

    private void updateCollisionPixels() {
        collisionXs[0] = hitbox.x + hitbox.width/2;
        collisionXs[1] = hitbox.x;
        collisionXs[2] = hitbox.x + hitbox.width;
        collisionXs[3] = hitbox.x + hitbox.width/2;
        collisionXs[4] = hitbox.x;
        collisionXs[5] = hitbox.x + hitbox.width;
        collisionXs[6] = hitbox.x + hitbox.width/2;
        collisionXs[7] = hitbox.x;
        collisionXs[8] = hitbox.x + hitbox.width;

        collisionYs[0] = hitbox.y;
        collisionYs[1] = hitbox.y + hitbox.height/2;
        collisionYs[2] = hitbox.y + hitbox.height/2;
        collisionYs[3] = hitbox.y + hitbox.height;
        collisionYs[4] = hitbox.y;
        collisionYs[5] = hitbox.y;
        collisionYs[6] = hitbox.y + hitbox.height/2;
        collisionYs[7] = hitbox.y + hitbox.height;
        collisionYs[8] = hitbox.y + hitbox.height;
    }

    public void keyPressed(KeyEvent e) {}

    public void KeyReleased(KeyEvent e) {}

    public void update(float yLevelOffset, float xLevelOffset) {
        int prevAction = planeAction;
        handleKeyboardInputs();
        adjustSpeedAndAction();
        movePlayer();    
        checkMapCollision(yLevelOffset, xLevelOffset);
        if (planeAction != prevAction) {aniIndex = 0;}
        updateAniTick();
        flame.update();
    }

    private void handleKeyboardInputs() {
    }

    private void adjustSpeedAndAction() {
        if ((planeAction == TAKING_COLLISION_DAMAGE)) {
            iFrameCount += 1;
            if (iFrameCount > iFrames) {
                planeAction = IDLE;
                iFrameCount = 0;
            }
        } 
        else {
            if (game.rightIsPressed && game.mIsPressed && (teleportBuffer == 0)) {
                planeAction = TELEPORTING_RIGHT;
                teleportBuffer = teleportCoolDown;
                flipX = 1;
                return;
            }
            else if (game.leftIsPressed && game.mIsPressed && (teleportBuffer == 0)) {
                planeAction = TELEPORTING_LEFT;
                teleportBuffer = teleportCoolDown;
                flipX = -1;
                return;
            }
            if (game.upIsPressed) {
                planeAction = IDLE;
                ySpeed -= acceleration;
                if (ySpeed < -playerMaxSpeed) {
                    ySpeed = -playerMaxSpeed;
                }
            } else if (game.downIsPressed) {
                planeAction = IDLE;
                ySpeed += acceleration;
                if (ySpeed > playerMaxSpeed) {
                    ySpeed = playerMaxSpeed;
                }
            }
            if (game.leftIsPressed) {
                planeAction = FLYING_LEFT;
                xSpeed -= acceleration;
                if (xSpeed < -playerMaxSpeed) {
                    xSpeed = -playerMaxSpeed;
                }
            }
            if (game.rightIsPressed) {
                planeAction = FLYING_RIGHT;
                xSpeed += acceleration;
                if (xSpeed > playerMaxSpeed) {
                    xSpeed = playerMaxSpeed;
                }
            }
        }


        if (!(game.leftIsPressed || game.rightIsPressed)) {
            if (xSpeed < 0) {xSpeed += acceleration/2;} 
            else if (xSpeed > 0) {xSpeed -= acceleration/2;}
            if ((Math.abs(xSpeed)) < 0.6) {           // OBS
                xSpeed = 0;
            }
            if ((planeAction != TAKING_COLLISION_DAMAGE) && (planeAction != TAKING_SHOOT_DAMAGE)) {
                planeAction = IDLE;
            }
        }
        if (!(game.upIsPressed || game.downIsPressed)) {
            if (ySpeed < 0) {ySpeed += acceleration/2;} 
            else if (ySpeed > 0) {ySpeed -= acceleration/2;}
            if ((Math.abs(ySpeed)) < 0.6) {
                ySpeed = 0;
            }
        }

        if (teleportBuffer > 3) {
            xSpeed = 0;
        }
    }

    private void movePlayer() {
        if ((planeAction == TELEPORTING_RIGHT) || (planeAction == TELEPORTING_LEFT)) {
            adjustPos(teleportDistance * flipX, 0);
            adjustTeleportHitbox();
            audioPlayer.playSFX(Audio.SFX_TELEPORT);
        }
        else {
            adjustPos(xSpeed, ySpeed);
        }
    }

    // Remember, this is called AFTER player has teleported. Thus the hitbox should be 
    // positioned where the player WAS, just before they teleported.
    private void adjustTeleportHitbox() {
        teleportHitbox.y = hitbox.y;
        if (flipX == 1) {
            teleportHitbox.x = hitbox.x - teleportKillOffset - teleportKillWidth;
        }
        else if (flipX == -1) {
            teleportHitbox.x = hitbox.x + hitbox.width + teleportKillOffset;
        }
    }

    private void checkMapCollision(float yLevelOffset, float xLevelOffset) {
        if ((planeAction == TELEPORTING_RIGHT) || (planeAction == TELEPORTING_LEFT)) {
            checkTeleportCollision(yLevelOffset, xLevelOffset);
        }
        else {
            int nrOfCollisions = 0;
            while (collidesWithMap(yLevelOffset, xLevelOffset)) {
                nrOfCollisions += 1;
                if (nrOfCollisions > 2) {
                    System.out.println("failSafe, player dies");
                    resetPlayer();
                    return;
                }
            }
            if (nrOfCollisions > 0) {
                takeCollisionDmg();
                audioPlayer.playSFX(Audio.SFX_COLLISION);
            }
        }
    }

    private void checkTeleportCollision(float yLevelOffset, float xLevelOffset) {
        if (collidesWithMap(yLevelOffset, xLevelOffset)) {
            this.planeAction = TAKING_COLLISION_DAMAGE;
            hitbox.x -= (teleportDistance * flipX);
            updateCollisionPixels();
            takeCollisionDmg();
            audioPlayer.playSFX(Audio.SFX_COLLISION);
            while (!collidesWithMap(yLevelOffset, xLevelOffset)) {
                hitbox.x += (teleportDistance/10 * flipX);
            }
        }
    }

    private boolean collidesWithMap(float yLevelOffset, float xLevelOffset) {
        updateCollisionPixels();
        for (int i = 0; i < 9; i++) {
            if (IsSolid(
                (int) (collisionXs[i] + xLevelOffset) / 3, 
                (int) (collisionYs[i] - yLevelOffset) / 3, 
                clImg)) {
                    if ((planeAction != TELEPORTING_RIGHT) && (planeAction != TELEPORTING_LEFT)) {
                        // Trengs fordi denne metoden kan kalles fra 2 forskjellige steder
                        pushInOppositeDirectionOf(i, pushDistance);
                    }
                    this.updateCollisionPixels();
                    return true;
            }
        }
        return false;
    }

    /** Checks if the enemy is close to player.
     * Possibly we could have that distance as a custom value stored within each enemy?
     * If enemy is close to player, it checks collisionpixels.
     * If a collision has occured, player takes damage and is pushed in opposite direction */
    public boolean collidesWithEnemy(Rectangle2D.Float enemyHitbox) {
        double dist = Math.pow(
            Math.pow((hitbox.x - enemyHitbox.x), 2) + Math.pow((hitbox.y - enemyHitbox.y), 2), 0.5f);
        if (dist < 100) {
            for (int i = 0; i < 9; i++) {
                Point point = new Point((int) collisionXs[i], (int) collisionYs[i]);
                if (enemyHitbox.contains(point)) {
                    if (planeAction != TAKING_COLLISION_DAMAGE) {
                        takeCollisionDmg();
                    }
                    audioPlayer.playSFX(Audio.SFX_COLLISION);
                    pushInOppositeDirectionOf(i, pushDistance);
                    this.updateCollisionPixels();
                    this.resetSpeed();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean teleportCollidesWithEnemy(Rectangle2D.Float enemyHitbox) {
        if (planeAction == TELEPORTING_RIGHT || planeAction == TELEPORTING_LEFT) {
            if (teleportHitbox.intersects(enemyHitbox)) {
                return true;
            }
        }
        return false;
    }

    private void pushInOppositeDirectionOf(int i, float pushDistance) {
        switch (i) {
            case 0 -> adjustPos(0, pushDistance * 1.5f);
            case 1 -> adjustPos(pushDistance, 0);
            case 2 -> adjustPos(-pushDistance, 0);
            case 3 -> adjustPos(0, -pushDistance);
            case 4 -> adjustPos(pushDistance, pushDistance);
            case 5 -> adjustPos(-pushDistance, pushDistance);
            // case 6 ->  do nothing
            case 7 -> adjustPos(pushDistance, -pushDistance);
            case 8 -> adjustPos(-pushDistance, -pushDistance);
        }
    }

    private void updateAniTick() {
        this.teleportBuffer -= 1;
        if (teleportBuffer < 0) {
            teleportBuffer = 0;
        }
        this.aniTick ++;
        if (aniTick >= aniTickPerFrame) {
            aniIndex ++;
            aniTick = 0;
            if (aniIndex == GetPlayerSpriteAmount(planeAction)) {
                aniIndex = GetPlayerSpriteAmount(planeAction) - 1;
            }
        }
    }

    private void adjustPos(float deltaX, float deltaY) {
        hitbox.x += deltaX;
        hitbox.y += deltaY;
        if (hitbox.x < edgeDist) {
            hitbox.x = edgeDist;
            xSpeed = 0;
        }
        if ((hitbox.x + hitbox.width + edgeDist) > Game.GAME_DEFAULT_WIDTH) {
            hitbox.x = Game.GAME_DEFAULT_WIDTH - hitbox.width - edgeDist;
            xSpeed = 0;
        }
        if (hitbox.y < edgeDist) {
            hitbox.y = edgeDist;
            ySpeed = 0;
        }
        if ((hitbox.y + hitbox.height + edgeDist) > Game.GAME_DEFAULT_HEIGHT) {
            hitbox.y = Game.GAME_DEFAULT_HEIGHT - hitbox.height - edgeDist;
            ySpeed = 0;
        }
    }

    private void resetPlayer() {
        hitbox.x = 500;
        hitbox.y = 350;
        HP = maxHP;
        updateCollisionPixels();
    }

    public void draw(Graphics g) {
        if (visible) {
            // Teleport shadows
            if (game.mIsPressed) {
                g.setColor(Color.LIGHT_GRAY);
                drawShadow(g, teleportDistance);
                drawShadow(g, -teleportDistance);
            }

            // Flame
            if (!game.downIsPressed) {
                flame.draw(g, hitbox.x + 2.5f, hitbox.y + hitbox.height);
            }

            // Player
            int actionIndex = planeAction;
            if ((teleportBuffer > 5) && (planeAction != TAKING_COLLISION_DAMAGE)) {
                actionIndex = TELEPORTING_RIGHT;}    
                // Gir oss et par ekstra frames med teleport-animation
                
            g.drawImage(
                animations[actionIndex][aniIndex],
                (int) ((hitbox.x - 20) * Game.SCALE), 
                (int) ((hitbox.y - 20) * Game.SCALE), 
                (int) (SHIP_SPRITE_WIDTH * 3 * Game.SCALE), 
                (int) (SHIP_SPRITE_HEIGHT * 3 * Game.SCALE), null);
            
            statusDisplay.draw(g);
            //this.drawHitbox(g, 0, 0);
            
            // Teleport hitbox
            /* 
            g.setColor(Color.RED);
            g.drawRect(
                (int) (teleportHitbox.x * Game.SCALE), 
                (int) (teleportHitbox.y * Game.SCALE), 
                (int) (teleportHitbox.width * Game.SCALE), 
                (int) (teleportHitbox.height * Game.SCALE));
                */
        }
    }

    private void drawShadow(Graphics g, int teleportDistance) {
        g.drawImage(
                tpShadowImg,
                (int) ((hitbox.x - 20 - teleportDistance) * Game.SCALE), 
                (int) ((hitbox.y - 20) * Game.SCALE), 
                (int) (SHIP_SPRITE_WIDTH * 3 * Game.SCALE), 
                (int) (SHIP_SPRITE_HEIGHT * 3 * Game.SCALE), null);
    }

    public void resetSpeed() {
        this.xSpeed = 0;
        this.ySpeed = 0;
    }

    public boolean takesCollisionDmg() {
        return (planeAction == TAKING_COLLISION_DAMAGE);
    }

    public void takeShootDamage(int damage) {
        this.HP -= damage;
        this.aniTick = 0;
        this.aniIndex = 0;
        this.planeAction = TAKING_SHOOT_DAMAGE;
        this.statusDisplay.setHP(this.HP);
    }

    private void takeCollisionDmg() {
        HP -= collisionDmg;
        this.aniTick = 0;
        this.aniIndex = 0;
        this.resetSpeed();
        this.planeAction = TAKING_COLLISION_DAMAGE;
        this.statusDisplay.setHP(HP);
    }

    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void increaseHealth(int health) {
        this.HP += health;
        if (HP > maxHP) {
            HP = maxHP;
        }
        this.statusDisplay.setHP(this.HP);
    }

    public void setBombs(int nr) {
        this.statusDisplay.setBombs(nr);
    }

    public void setKilledEnemies(int nr) {
        this.statusDisplay.setKilledEnemies(nr);
    }
    
    public void setClImg(BufferedImage clImg) {
        this.clImg = clImg;
    }

    public void resetToStartPos() {
        hitbox.x = 500f;
        hitbox.y = 400f;
    }
}
