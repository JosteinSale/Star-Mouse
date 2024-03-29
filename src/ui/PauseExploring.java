package ui;

import static utils.Constants.UI.PAUSE_EXPLORING_WIDTH;
import static utils.Constants.UI.PAUSE_EXPLORING_HEIGHT;
import static utils.Constants.UI.ITEM_BOX_WIDTH;
import static utils.Constants.UI.ITEM_BOX_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import gamestates.Gamestate;
import gamestates.Statemethods;
import main.Game;
import misc.ProgressValues;
import utils.LoadSave;
import utils.Constants.Audio;

public class PauseExploring implements Statemethods {
    private Game game;
    private ProgressValues progValues;
    private AudioPlayer audioPlayer;
    private OptionsMenu optionsMenu;
    private Color bgColor = new Color(0, 0, 0, 230);
    private Font headerFont;
    private Font menuFont;
    private Font infoFont;
    private Font itemFont;
    private BufferedImage itemBoxImg;
    private BufferedImage itemSelectedImg;
    private String[] menuOptions = {"Continue", "Options", "Main Menu"};
    private BufferedImage pointerImg;
    private Rectangle itemInfoBox;     //  Alle Rectangle's er justert ift game.Scale
    private boolean active;
    private int selectedIndex = 8;     // Items = 0-7, menuOptions = 8-10
    
    private String[] valueNames = {"Credits: x", "Bombs: x"};
    private int[] statusValues = {0, 0};
    private ArrayList<InventoryItem> items;

    private int bgW;
    private int bgH;
    private int bgX;
    private int bgY;
    
    private int itemBoxW;
    private int itemBoxH;
    private int itemBoxX;
    private int itemBoxY;

    private int cursorMinY = 490;
    private int cursorMaxY = 620;
    private int cursorX = 505;
    private int cursorY = cursorMinY;
    private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 2;   
    
    public PauseExploring(Game game, ProgressValues progValues, AudioPlayer audioPlayer, OptionsMenu optionsMenu) {
        this.game = game;
        this.progValues = progValues;
        updateProgressValues();
        this.audioPlayer = audioPlayer;
        this.optionsMenu = optionsMenu;
        calcDrawValues();
        loadImages();
        loadFonts();
        this.items = new ArrayList<>();
    }

    public void updateProgressValues() {
        this.statusValues[0] = progValues.getCredits();
        this.statusValues[1] = progValues.getBombs();
    }

    private void calcDrawValues() {
        bgW = PAUSE_EXPLORING_WIDTH;
        bgH = PAUSE_EXPLORING_HEIGHT;
        bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
        bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));

        itemBoxW = ITEM_BOX_WIDTH;
        itemBoxH = ITEM_BOX_HEIGHT;
        itemBoxX = bgX + (bgW / 2) - 20;
        itemBoxY = bgY + 120;

        this.itemInfoBox = new Rectangle(
            (int) (170 * Game.SCALE), (int) (itemBoxY * Game.SCALE), 
            (int) (300 * Game.SCALE), (int) (itemBoxH * Game.SCALE));
    }

    private void loadImages() {
        this.itemBoxImg = LoadSave.getExpImageSprite(LoadSave.ITEM_BOX);
        this.pointerImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);
        this.itemSelectedImg = LoadSave.getExpImageSprite(LoadSave.ITEM_SELECTED);
    }

    private void loadFonts() {
        this.headerFont = LoadSave.getHeaderFont();
        this.menuFont = LoadSave.getNameFont();
        this.infoFont = LoadSave.getInfoFont();
        this.itemFont = LoadSave.getItemFont();
    }

    @Override
    public void update() {
        if (optionsMenu.isActive()) {
            optionsMenu.update();
        } 
        else {
            handleKeyBoardInputs();
        }
    }

    private void handleKeyBoardInputs() {
        if (game.upIsPressed) {
            game.upIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            moveCursorUp();
            takeIndexUp();
        }
        else if (game.downIsPressed) {
            game.downIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            moveCursorDown();
            takeIndexDown();
        }
        else if (game.rightIsPressed) {
            game.rightIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            takeIndexRight();
        }
        else if (game.leftIsPressed) {
            game.leftIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            takeIndexLeft();
        }
        else if (game.interactIsPressed) {
            game.interactIsPressed = false;
            if (selectedIndex == 8) {
                this.flipActive();
            }
            else if (selectedIndex == 9) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                optionsMenu.setActive(true);
            }
            else if (selectedIndex == 10) {
                this.flipActive();
                audioPlayer.stopAllLoops();
                game.resetMainMenu();
                // TODO - make resetMethod for Exploring.
                // TODO - make fadeOut. Make public reset-method that resets fade-boolean
                Gamestate.state = Gamestate.MAIN_MENU;
            }
        }
    }

    private void takeIndexLeft() {
        if ((selectedIndex == 0) || (selectedIndex == 4) || (selectedIndex >= 8)) {
            return;
        }
        else {
            selectedIndex -= 1;
        }
    }


    private void takeIndexRight() {
        if ((selectedIndex == 3) || (selectedIndex == 7) || (selectedIndex >= 8)) {
            return;
        }
        else {
            selectedIndex += 1;
        }
    }


    private void takeIndexDown() {
        if (increaseIndexRow()) {
            return;
        }
        else if (selectedIndex < 10) { 
            selectedIndex += 1;
        }
    }

    private boolean increaseIndexRow() {
        if (selectedIndex >= 8) {
            return false;
        }
        else if ((selectedIndex > 4 ) && (selectedIndex < 8)) {
            selectedIndex = 8;
            return true;
        }
        else {
            selectedIndex += 4;
            return true;
        }
    }


    private void takeIndexUp() {
        if (reduceIndexRow()) {
            return;
        }
        else if (selectedIndex > 7) {
            selectedIndex -= 1;
        }
    }

    private boolean reduceIndexRow() {
        if ((selectedIndex <= 3) || (selectedIndex >= 9)) {
            return false;
        }
        else {
            selectedIndex -= 4;
            return true;
        }
    }


    private void moveCursorUp() {
        if (selectedIndex > 8) {
            cursorY -= menuOptionsDiff;
            if (cursorY < cursorMinY) {
                cursorY = cursorMinY;
            }
        }
    }

    private void moveCursorDown() {
        if (selectedIndex >= 8) {
            cursorY += menuOptionsDiff;
            if (cursorY > cursorMaxY) {
                cursorY = cursorMaxY;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (optionsMenu.isActive()) {
            optionsMenu.draw(g);
        } 
        else {
            Graphics2D g2 = (Graphics2D) g;
            drawBackground(g2);
            drawItems(g2);
            drawText(g2);
            drawStatusValues(g2);
            drawPointer(g2);
        }
    }

    private void drawStatusValues(Graphics2D g2) {
        g2.setFont(infoFont);
        g2.setColor(Color.WHITE);
        for (int i = 0; i < valueNames.length; i++) {
            g2.drawString(
            valueNames[i] + Integer.toString(statusValues[i]), 
            (int) (170 * Game.SCALE), (int) ((480 + (i * 50)) * Game.SCALE));
        }
    }

    private void drawPointer(Graphics2D g2) {
        if (selectedIndex >= 8) {
            g2.drawImage(
            pointerImg, 
            (int) (cursorX * Game.SCALE), (int) ((cursorY - 30) * Game.SCALE), 
            (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
        }
    }


    private void drawText(Graphics2D g2) {
        g2.setColor(Color.WHITE);

        // Header
        g2.setFont(headerFont);
        g2.drawString("Inventory", (int) (380 * Game.SCALE), (int) (150 * Game.SCALE));

        // Menu options
        for (int i = 0; i < menuOptions.length; i++) {
            Rectangle rect = new Rectangle(
                (int) (600 * Game.SCALE), (int) ((450 + i * menuOptionsDiff) * Game.SCALE),
                (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
            DrawCenteredString(g2, menuOptions[i], rect, menuFont);
        }

        // Item Info
        g2.setFont(itemFont);
        if ((items.size() - 1) >= selectedIndex) {
            String name = items.get(selectedIndex).getName();
            g2.drawString(
                name,
                itemInfoBox.x + (int) (10 * Game.SCALE), 
                itemInfoBox.y + (int) (30 * Game.SCALE)
            );
            ArrayList<String> info = items.get(selectedIndex).getDescription();
            for (int i = 0; i < info.size(); i++) {
                g2.drawString(
                    info.get(i), 
                    itemInfoBox.x + (int) (10 * Game.SCALE), 
                    itemInfoBox.y + (int) (((80 + (i * 40))  * Game.SCALE)));
            }
        }
    }

    private void drawItems(Graphics2D g2) {
        g2.drawImage(
            itemBoxImg, 
            (int) (itemBoxX * Game.SCALE), (int) (itemBoxY * Game.SCALE),
            (int) (itemBoxW * Game.SCALE), (int) (itemBoxH * Game.SCALE), null);
        
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 4; i++) {
                int currentIndex = i + (j * 4);
                if ((items.size() - 1) >= currentIndex) {
                    g2.drawImage(
                        items.get(currentIndex).getImg(), 
                        (int) ((itemBoxX + 6 + (i * 96)) * Game.SCALE), 
                        (int) ((itemBoxY + 6 + (j * 96)) * Game.SCALE),
                        (int) (90 * Game.SCALE), (int) (90 * Game.SCALE), null);
                }
                // The selected item gets a white frame around it
                if (currentIndex == selectedIndex) {
                    g2.drawImage(
                        itemSelectedImg, 
                        (int) ((itemBoxX + (i * 96)) * Game.SCALE), 
                        (int) ((itemBoxY + (j * 96)) * Game.SCALE),
                        (int) (103 * Game.SCALE), (int) (103 * Game.SCALE), null);
                }
            }
        }
    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(bgColor);
        g2.fillRect(
            (int) (bgX * Game.SCALE), (int) (bgY * Game.SCALE), 
            (int) (bgW * Game.SCALE), (int) (bgH * Game.SCALE));
        g2.setColor(Color.WHITE);
        g2.drawRect(
            (int) ((bgX + 10) * Game.SCALE), (int) ((bgY + 10) * Game.SCALE), 
            (int) ((bgW - 20) * Game.SCALE), (int) ((bgH - 20) * Game.SCALE));

        g2.setColor(Color.DARK_GRAY.darker());
        g2.fill(itemInfoBox);
    }

    public void flipActive() {
        this.active = !active;
    }

    public boolean isActive() {
        return this.active;
    }

    public void addItem(InventoryItem item) {
        this.items.add(item);
    }
}
