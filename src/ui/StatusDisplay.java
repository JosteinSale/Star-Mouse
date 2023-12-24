package ui;

import main.Game;
import utils.LoadSave;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class StatusDisplay {
    private BufferedImage bombImg;
    private BufferedImage enemyCounterImg;

    private Font font;
    private Color HPbgColor = new Color(97, 0, 15, 180);

    private int HP;
    private int maxHP = 100;
    private int bombs;
    private int killedEnemies = 0;

    private int statusX = Game.GAME_DEFAULT_WIDTH - 150;
    private int bombY = Game.GAME_DEFAULT_HEIGHT - 120;
    private int bombW;
    private int bombH;
    private int enemyCounterY = Game.GAME_DEFAULT_HEIGHT - 70;
    private int enemyCounterW;
    private int enemyCounterH;
    private int HPbarX = Game.GAME_DEFAULT_WIDTH - 180;
    private int HPbarY = Game.GAME_DEFAULT_HEIGHT - 50;
    private int HPbarMaxW = 300;             // Denne kan endres senere med en egen metode
    private int HPbarH = 10;
    private int HPbarCurW;

    public StatusDisplay() {
        this.bombImg = LoadSave.getFlyImageSprite(LoadSave.BOMB_SPRITE);
        this.enemyCounterImg = LoadSave.getFlyImageSprite(LoadSave.ENEMYCOUNTER_SPRITE);
        this.bombW = bombImg.getWidth() * 2;
        this.bombH = bombImg.getHeight() * 2;
        this.enemyCounterW = enemyCounterImg.getWidth() * 2;
        this.enemyCounterH = enemyCounterImg.getHeight() * 2;

        this.font = LoadSave.getInfoFont();
        this.HPbarCurW = (int) (((float) HP / maxHP) * HPbarMaxW);
    }

    public void draw(Graphics g) {
        // Images
        g.drawImage(
            bombImg, 
            (int) (statusX * Game.SCALE), (int) (bombY * Game.SCALE), 
            (int) (bombW * Game.SCALE), (int) (bombH * Game.SCALE), null);
        g.drawImage(
            enemyCounterImg, 
            (int) (statusX * Game.SCALE), (int) (enemyCounterY * Game.SCALE), 
            (int) (enemyCounterW * Game.SCALE), (int) (enemyCounterH * Game.SCALE), null);

        // Healthbar
        g.setColor(HPbgColor);
        g.fillRect(
            (int) ((HPbarX - HPbarMaxW)* Game.SCALE), (int) (HPbarY * Game.SCALE), 
            (int) (HPbarMaxW * Game.SCALE), (int) (HPbarH * Game.SCALE));
        g.setColor(Color.RED);
        g.fillRect(
            (int) ((HPbarX - HPbarCurW)* Game.SCALE), (int) (HPbarY * Game.SCALE), 
            (int) (HPbarCurW * Game.SCALE), (int) (HPbarH * Game.SCALE));

        // Text
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(
            "x " + Integer.toString(bombs), 
            (int) ((statusX + 60) * Game.SCALE), 
            (int) ((bombY + 30) * Game.SCALE));
        g.drawString(
            "x " + Integer.toString(killedEnemies), 
            (int) ((statusX + 60) * Game.SCALE), 
            (int) ((enemyCounterY + 30) * Game.SCALE));
    }

    public void setHP(int HP) {
        this.HP = HP;
        this.HPbarCurW = (int) (((float) this.HP / maxHP) * HPbarMaxW);
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
        // TODO - Adjust how long the bar is
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public void setKilledEnemies(int killedEnemies) {
        this.killedEnemies = killedEnemies;
    }

}