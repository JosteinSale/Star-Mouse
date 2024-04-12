package entities.flying;

import static utils.Constants.Flying.ActionConstants.*;
import static utils.Constants.Flying.SpriteSizes.SHIP_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.SHIP_SPRITE_WIDTH;
import static utils.HelpMethods.IsSolid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.Entity;
import entities.flying.enemies.Enemy;
import main_classes.Game;
import ui.StatusDisplay;
import utils.LoadSave;
import utils.Constants.Audio;

public class PlayerFly extends Entity implements ShootingPlayer {
    protected Game game;
    protected AudioPlayer audioPlayer;
    protected BufferedImage clImg;
    protected BufferedImage[][] animations;
    protected BufferedImage tpShadowImg;
    protected ShipFlame flame;
    protected StatusDisplay statusDisplay;
    protected Rectangle2D.Float teleportHitbox;

    protected int planeAction;
    protected float xSpeed = 0;
    protected float ySpeed = 0;
    protected float acceleration = 1.0f;
    protected float playerMaxSpeed = 8f;
    protected boolean visible = true;
    protected float[] collisionXs = new float[9];
    protected float[] collisionYs = new float[9];
    protected int edgeDist = 20;
    protected int pushDistance = 40;
    protected int teleportDistance = 250;
    protected int teleportKillWidth = 100;
    protected int teleportKillOffset;  // The distance between the players hitbox and the teleport hitbox
    protected int maxHP;
    protected int HP = 100;
    protected int collisionDmg = 10;
    protected int flipX = 1;      // 1 = høyre, -1 = venstre. Brukes i checkTeleportCollision

    protected int aniTick = 0;
    protected int aniTickPerFrame = 3;          // Antall ticks per gang animasjonen oppdateres
    protected int aniIndex = 0;

    protected int iFrames = 20;
    protected int iFrameCount = 0;
    protected int teleportBuffer = 0;
    protected int teleportCoolDown = 10;

    public PlayerFly(Game game, Float hitbox) {
        super(hitbox);
        this.game = game;
        this.maxHP = game.getExploring().getProgressValues().getMaxHP();
        this.audioPlayer = game.getAudioPlayer();
        this.tpShadowImg = LoadSave.getFlyImageSprite(LoadSave.TELEPORT_SHADOW);
        loadAnimations();
        updateCollisionPixels();
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

    protected void updateCollisionPixels() {
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

    public void update(float yLevelOffset, float xLevelOffset) {
        int prevAction = planeAction;
        handleKeyboardInputs();
        movePlayer();    
        checkMapCollision(yLevelOffset, xLevelOffset);
        if (planeAction != prevAction) {aniIndex = 0;}
        updateAniTick();
        flame.update();
        statusDisplay.update();
    }

    protected void handleKeyboardInputs() {

        // First: handles behaviour based on whether keys are pressed
        if ((planeAction == TAKING_COLLISION_DAMAGE)) {   // Player can't control the plane while 
            iFrameCount += 1;                             // taking collision damage.
            if (iFrameCount > iFrames) {
                planeAction = IDLE;
                iFrameCount = 0;
            }
        } 
        else {
            if (game.rightIsPressed && game.teleportIsPressed && (teleportBuffer == 0)) {
                planeAction = TELEPORTING_RIGHT;
                teleportBuffer = teleportCoolDown;
                flipX = 1;
                return;
            }
            else if (game.leftIsPressed && game.teleportIsPressed && (teleportBuffer == 0)) {
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

        // Second: handles behaviour based on whether keys are NOT pressed
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

    protected void movePlayer() {
        if ((planeAction == TELEPORTING_RIGHT) || (planeAction == TELEPORTING_LEFT)) {
            adjustPos(teleportDistance * flipX, 0);
            adjustTeleportHitbox();
            audioPlayer.playSFX(Audio.SFX_TELEPORT);
        }
        else {
            adjustPos(xSpeed, ySpeed);
        }
    }

    /** Moves the player hitbox, and prevents it from going off screen */
    protected void adjustPos(float deltaX, float deltaY) {
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

    // Remember, this is called AFTER player has teleported. Thus the hitbox should be 
    // positioned in relation to where the player WAS, just before they teleported.
    protected void adjustTeleportHitbox() {
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
                if (nrOfCollisions > 2) {  // failsafe
                    this.HP = 0;
                    takeCollisionDmg();
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
        // Checks map
        if (collidesWithMap(yLevelOffset, xLevelOffset)) {
            undoTeleportAndTakeDamage();
            while (!collidesWithMap(yLevelOffset, xLevelOffset)) {
                hitbox.x += (teleportDistance/10 * flipX);
            }
        }
        // Checks big enemies
        else {
            // This list will usually have 0, at most 2-3 enemies.
            ArrayList<Enemy> bigEnemies = game.getFlying().getBigEnemies();
            for (Enemy e : bigEnemies) {  
                if (hitbox.intersects(e.getHitbox())) {
                    undoTeleportAndTakeDamage();
                    e.takeDamage(10);
                    game.getFlying().checkIfDead(e);
                    while (!hitbox.intersects(e.getHitbox())) {
                        hitbox.x += (teleportDistance/10 * flipX);
                    }
                    hitbox.x -= (teleportDistance/10 * flipX);  
                    // We need to move player a bit more, because we don't use the collidesWithMap-
                    // method as a condition for the loop, and this method moves player if true.
                }
            }
        }
    }

    /**
    * Should be called if the player tried to teleport into something
    * that couldn't be teleported into. It resets the player to the previous
    * position, takes damage, and plays SFX.
    */
    protected void undoTeleportAndTakeDamage() {
        this.planeAction = TAKING_COLLISION_DAMAGE;
        hitbox.x -= (teleportDistance * flipX);
        updateCollisionPixels();
        takeCollisionDmg();
        audioPlayer.playSFX(Audio.SFX_COLLISION);
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

    /** Is called from EnemyManager 
     * Checks if the enemy overlaps the player.
     * If so, it checks collisionpixels (an extensive operation).
     * If a collision has occured, player takes damage and is pushed in opposite direction.
     * 
     * (This method could be improved: instead of making 9 new points for each enemy, 
     * make 9 points once, and then check each enemyhitbox that is close enough.
     * Make this method in enemyManager, call getPlayerPixels(), 
     * check those pixels for each enemy). 
     */
    public boolean collidesWithEnemy(Rectangle2D.Float enemyHitbox) {
        if (hitbox.intersects(enemyHitbox)) {
            for (int i = 0; i < 9; i++) {
                Point point = new Point((int) collisionXs[i], (int) collisionYs[i]);
                if (enemyHitbox.contains(point)) {
                    takeCollisionDmg();
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

    public boolean teleportDamagesEnemy(Rectangle2D.Float enemyHitbox) {
        if (planeAction == TELEPORTING_RIGHT || planeAction == TELEPORTING_LEFT) {
            if (teleportHitbox.intersects(enemyHitbox)) {
                return true;
            }
        }
        return false;
    }

    /*
     * The pixels of the player hitbox are enumerated as such:
     * 4 - 0 - 5
     * 1 - 6 - 2
     * 7 - 3 - 8
     */
    protected void pushInOppositeDirectionOf(int i, float pushDistance) {
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

    protected void updateAniTick() {
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

    public void draw(Graphics g) {
        if (visible) {
            // Teleport shadows
            if (game.teleportIsPressed) {
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
            g.setColor(Color.RED);
            this.drawHitbox(g, 0, 0);
            
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

    @Override
    public void takeShootDamage(int damage) {
        this.HP -= damage;
        this.aniTick = 0;
        this.aniIndex = 0;
        this.planeAction = TAKING_SHOOT_DAMAGE;
        this.statusDisplay.setHP(this.HP);
        this.statusDisplay.setBlinking(true);
        if (HP <= 0) {
            game.getFlying().killPlayer();
        }
    }

    protected void takeCollisionDmg() {
        HP -= collisionDmg;
        this.aniTick = 0;
        this.aniIndex = 0;
        this.resetSpeed();
        this.planeAction = TAKING_COLLISION_DAMAGE;
        this.statusDisplay.setHP(HP);
        this.statusDisplay.setBlinking(true);
        if (HP <= 0) {
            game.getFlying().killPlayer();
        }
    }

    @Override
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

    @Override
    public void setBombs(int nr) {
        this.statusDisplay.setBombs(nr);
    }

    public void setKilledEnemies(int nr) {
        this.statusDisplay.setKilledEnemies(nr);
    }
    
    public void setClImg(BufferedImage clImg) {
        this.clImg = clImg;
    }

    public void reset() {
        this.visible = true;
        this.aniIndex = 0;
        HP = maxHP;
        statusDisplay.setHP(this.HP);
        statusDisplay.setBlinking(false);
        statusDisplay.setKilledEnemies(0);
        hitbox.x = 500f;
        hitbox.y = 400f;
        updateCollisionPixels();
        planeAction = IDLE;
    }
}
