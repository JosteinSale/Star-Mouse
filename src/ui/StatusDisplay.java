package ui;

import main.Game;
import utils.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class StatusDisplay {
    private BufferedImage bombImg;
    private BufferedImage enemyCounterImg;

    private int shipHealth;
    private int bombs;
    private int killedEnemies;

    private int bombX = Game.GAME_DEFAULT_WIDTH - 200;
    private int bombY = Game.GAME_DEFAULT_HEIGHT - 150;
    private int bombW;
    private int bombH;

    public StatusDisplay() {
        this.bombImg = LoadSave.getFlyImageSprite(LoadSave.BOMB_SPRITE);
        this.enemyCounterImg = LoadSave.getFlyImageSprite(LoadSave.ENEMYCOUNTER_SPRITE);
        this.bombW = bombImg.getWidth() * 3;
        this.bombH = bombImg.getHeight() * 3;
    }

    public void setHealth(int health) {
        this.shipHealth = health;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public void setKilledEnemies(int killedEnemies) {
        this.killedEnemies = killedEnemies;
    }

    public void draw(Graphics g) {
        g.drawImage(
            bombImg, 
            (int) (bombX * Game.SCALE), (int) (bombY * Game.SCALE), 
            (int) (bombW * Game.SCALE), (int) (bombH * Game.SCALE), null);
    }

}