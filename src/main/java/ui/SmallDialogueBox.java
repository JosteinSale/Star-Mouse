package ui;

import static utils.HelpMethods.GetCharacterIndex;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main_classes.Game;
import utils.HelpMethods;
import utils.ResourceLoader;

public class SmallDialogueBox {
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

    public SmallDialogueBox() {
        dialogueFont = ResourceLoader.getInfoFont();
        nameFont = ResourceLoader.getNameFont();
        this.portraits = BigDialogueBox.getAllPortraits();
    }

    public void setDialogue(String name, int speed, String text, int portraitIndex) {
        this.totalLetters = text.length();
        this.currentLetter = 0;
        this.aniTick = 0;
        this.dialogue = text;
        this.name = name;
        this.tickPerFrame = speed;
        this.characterIndex = GetCharacterIndex(name);
        this.nameColor = HelpMethods.GetNameColor(name);
        this.portraitIndex = portraitIndex;
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
