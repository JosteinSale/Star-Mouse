package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import utils.LoadSave;
import static utils.Constants.UI.DIALOGUEBOX_WIDTH;
import static utils.Constants.UI.DIALOGUEBOX_HEIGHT;
import static utils.Constants.UI.DIALOGUE_MAX_LETTERS;
import static utils.Constants.UI.PORTRAIT_SIZE;
import static utils.HelpMethods.GetCharacterIndex;

public class DialogueBox2 {
    private BufferedImage dialogueBoxImg;
    private BufferedImage[][] portraits;

    private String name;
    private Color nameColor;
    private int characterIndex = 0;
    private int portraitIndex = 0;
    private int aniTick;
    private int tickPerFrame;
    private int currentLetter = 0;
    private int totalLetters;

    private Font dialogueFont;
    private Font nameFont;
    private String dialogue;
    private int X = (int) (40 * Game.SCALE);
    private int Y = (int) (600 * Game.SCALE);

    public DialogueBox2() {
        dialogueBoxImg = LoadSave.getExpImageSprite(LoadSave.DIALOGUE_BOX);
        dialogueFont = LoadSave.getInfoFont();
        nameFont = LoadSave.getNameFont();
        loadAllPortraits();
    }

    /** We can change this to pilote-portraits later */
    private void loadAllPortraits() {
        portraits = new BufferedImage[3][8];
        portraits[0] = getPortraits(LoadSave.MAX_PORTRAITS, 8);
        portraits[1] = getPortraits(LoadSave.OLIVER_PORTRAITS, 6);
        portraits[2] = getPortraits(LoadSave.NPC_PORTRAITS1, 5);
    }

    private BufferedImage[] getPortraits(String portraitName, int length) {
        BufferedImage img = LoadSave.getExpImageSprite(portraitName);
        BufferedImage[] portraits = new BufferedImage[length];
        for (int i = 0; i < portraits.length; i++) { 
            portraits[i] = img.getSubimage(
                i * PORTRAIT_SIZE, 0, PORTRAIT_SIZE, PORTRAIT_SIZE);
        }
        return portraits;
    }

    public void setDialogue(String name, int speed, String text, int portraitIndex) {
        this.totalLetters = text.length();
        this.currentLetter = 0;
        this.aniTick = 0;
        this.dialogue = text;
        this.name = name;
        this.tickPerFrame = speed;
        this.characterIndex = GetCharacterIndex(name);
        this.nameColor = getNameColor(name);
        this.portraitIndex = portraitIndex;
    }


    private Color getNameColor(String name) {
        Color color = switch(name) {
            case "Max" -> Color.LIGHT_GRAY;
            case "Oliver" -> new Color(206, 191, 132);
            case "Lance" -> Color.LIGHT_GRAY;
            case "Charlotte" -> Color.GREEN.darker();
            case "Nina" -> Color.PINK;
            case "Shady pilot" -> Color.ORANGE;
            default -> throw new IllegalArgumentException(
                "No nameColor available for '" + name + "'");
        };
        return color;
    }

    public void update() {
        aniTick ++;
        if (aniTick >= tickPerFrame) {
            currentLetter += 1;
            aniTick = 0;
        }
    }

    public void forwardDialogue() {
        this.currentLetter = totalLetters;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(X, Y, (int)(850 * Game.SCALE), (int) (120 * Game.SCALE));
        //g.setColor(Color.BLACK);
        //g.drawRect(X, Y, (int)(800 * Game.SCALE), (int) (100 * Game.SCALE));
        
        g.drawImage(
            portraits[characterIndex][portraitIndex], 
            X + (int)(5 * Game.SCALE), Y + (int)(5 * Game.SCALE), 
            (int) (110 * Game.SCALE), (int) (110 * Game.SCALE), null);

        g.setColor(nameColor);
        g.setFont(nameFont);   // Er allerede justert ift Game.SCALE
        g.drawString(
            name,
            X + (int) (130 * Game.SCALE), 
            Y + (int) (40 * Game.SCALE));

        g.setColor(Color.WHITE);
        g.setFont(dialogueFont);

        drawText(g);
    }

    private void drawText(Graphics g) {
        drawPartialSentence(g, dialogue, currentLetter,
            X + (int) (130 * Game.SCALE), Y + (int) (80 * Game.SCALE));
    }

    private void drawPartialSentence(Graphics g, String s, int currentLetter, int x, int y) {
        g.drawString(
            s.substring(0, currentLetter), x, y);
    }

    public boolean allLettersAppeared() {
        return (currentLetter == totalLetters);
    }
}
