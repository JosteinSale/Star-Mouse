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

public class DialogueBox {
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
    private ArrayList<Integer> breakPoints;
    private ArrayList<String> formattedStrings;
    private int X = (int) ((Game.GAME_DEFAULT_WIDTH/2 -  DIALOGUEBOX_WIDTH/2) * Game.SCALE);
    private int Y = (int) (550 * Game.SCALE);

    public DialogueBox() {
        dialogueBoxImg = LoadSave.getExpImageSprite(LoadSave.DIALOGUE_BOX);
        dialogueFont = LoadSave.getInfoFont();
        nameFont = LoadSave.getNameFont();
        this.breakPoints = new ArrayList<>();
        this.formattedStrings = new ArrayList<>();
        loadAllPortraits();
    }

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
        this.breakPoints.clear();
        this.formattedStrings.clear();
        this.currentLetter = 0;
        this.aniTick = 0;
        this.name = name;
        this.tickPerFrame = speed;
        formatStrings(text);
        this.characterIndex = GetCharacterIndex(name);
        this.nameColor = getNameColor(name);
        this.portraitIndex = portraitIndex;
    }

    // Regarding length limits for sentences:
    // There's a limit of 32 characters per sentence.
    // Though, a sentence may not end perfectly at 32 words, causing us to 'loose'
    // those unused letters at the end of the sentence. Thus the limit is usually a bit lower.
    // At most you can do 3 single words of length 32.
    // But if you approach 90, calculate how many letters you've lost before running.
    private void formatStrings(String text) {
        String[] words = text.split(" ");
        int letterCount = 0;
        int breakCount = 0;
        for (String word : words) {
            letterCount += word.length();
            breakCount += word.length();
            if ((letterCount) > DIALOGUE_MAX_LETTERS) {
                breakCount -= (word.length() + 1);   // Trekker fra et ord og et mellomrom
                breakPoints.add(breakCount);
                breakCount += word.length() + 1;     // Legger til samme ord og mellomrom igjen

                letterCount = word.length();         // 'Nullsettes'
                totalLetters -= 1;                   // Totalt har vi fjernet et mellomrom fra text
            }
            letterCount += 1;  // +1 for mellomrom
            breakCount += 1;
        }
        breakPoints.add(text.length());              // Siste breakpoint
    
        int beginIndex = 0;
        for (Integer endIndex : breakPoints) {
            String line = text.substring(beginIndex, endIndex);
            formattedStrings.add(line);
            beginIndex += (endIndex + 1 - beginIndex); 
        }
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
        g.drawImage(
            dialogueBoxImg, X, Y, 
            (int) (DIALOGUEBOX_WIDTH * Game.SCALE), 
            (int) (DIALOGUEBOX_HEIGHT * Game.SCALE), null);
        
        g.drawImage(
            portraits[characterIndex][portraitIndex], 
            X + (int)(12 * Game.SCALE), Y + (int)(12 * Game.SCALE), 
            (int) (PORTRAIT_SIZE * 3 * Game.SCALE), (int) (PORTRAIT_SIZE * 3 * Game.SCALE), null);

        g.setColor(nameColor);
        g.setFont(nameFont);   // Er allerede justert ift Game.SCALE
        g.drawString(
            name,
            X + (int) (200 * Game.SCALE), 
            Y + (int) (60 * Game.SCALE));

        g.setColor(Color.WHITE);
        g.setFont(dialogueFont);

        drawText(g);
    }

    private void drawText(Graphics g) {
        int sum1 = formattedStrings.get(0).length();
        int sum2 = 0; 
        if (formattedStrings.size() > 1) {
            sum2 = sum1 + formattedStrings.get(1).length();
        }

        if (currentLetter <= sum1) {
            drawPartialSentence(g, formattedStrings.get(0), currentLetter,
            X + (int) (200 * Game.SCALE), Y + (int) (100 * Game.SCALE));
        }
        else if (currentLetter <= sum2) {
            g.drawString(formattedStrings.get(0), 
                X + (int) (200 * Game.SCALE), Y + (int) (100 * Game.SCALE));
            drawPartialSentence(g, formattedStrings.get(1), 
                currentLetter - sum1,
                X + (int) (200 * Game.SCALE), Y + (int) (135 * Game.SCALE));
        }
        else {
            g.drawString(formattedStrings.get(0), 
                X + (int) (200 * Game.SCALE), Y + (int) (100 * Game.SCALE));
            g.drawString(formattedStrings.get(1), 
                X + (int) (200 * Game.SCALE), Y + (int) (135 * Game.SCALE));
            drawPartialSentence(g, formattedStrings.get(2), 
                currentLetter - sum2,
                X + (int) (200 * Game.SCALE), Y + (int) (170 * Game.SCALE));
        }
    }

    private void drawPartialSentence(Graphics g, String s, int currentLetter, int x, int y) {
        g.drawString(
            s.substring(0, currentLetter), x, y);
    }

    public boolean allLettersAppeared() {
        return (currentLetter == totalLetters);
    }
}
