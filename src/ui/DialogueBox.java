package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import main.Game;
import utils.LoadSave;
import utils.Constants.Audio;

import static utils.Constants.UI.DIALOGUEBOX_WIDTH;
import static utils.Constants.UI.DIALOGUEBOX_HEIGHT;
import static utils.Constants.UI.DIALOGUE_MAX_LETTERS;
import static utils.Constants.UI.PORTRAIT_SIZE;
import static utils.HelpMethods.GetCharacterIndex;

public class DialogueBox {
    private AudioPlayer audioPlayer;
    private BufferedImage dialogueBoxImg;
    private BufferedImage[][] portraits;

    private String name;
    private Color nameColor;
    private int characterIndex = 0;
    private int portraitIndex = 0;
    private int voiceClipIndex;
    private int aniTick;
    private int aniTickPerFrame;
    private int currentLetter;
    private int currentLine = 0;
    private int voiceTick;
    private int voiceTickPerFrame;
    private boolean allLettersAppeared = false;

    private Font dialogueFont;
    private Font nameFont;
    private ArrayList<Integer> breakPoints;
    private ArrayList<String> formattedStrings;
    private int X = (int) ((Game.GAME_DEFAULT_WIDTH/2 -  DIALOGUEBOX_WIDTH/2) * Game.SCALE);
    private int Y = (int) (550 * Game.SCALE);

    public DialogueBox(Game game) {
        this.audioPlayer = game.getAudioPlayer();
        dialogueBoxImg = LoadSave.getExpImageSprite(LoadSave.DIALOGUE_BOX);
        dialogueFont = LoadSave.getInfoFont();
        nameFont = LoadSave.getNameFont();
        this.breakPoints = new ArrayList<>();
        this.formattedStrings = new ArrayList<>();
        loadAllPortraits();
    }

    private void loadAllPortraits() {
        portraits = new BufferedImage[3][12];
        portraits[0] = getPortraits(LoadSave.MAX_PORTRAITS, 11);
        portraits[1] = getPortraits(LoadSave.OLIVER_PORTRAITS, 9);
        portraits[2] = getPortraits(LoadSave.NPC_PORTRAITS1, 12);
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
        this.allLettersAppeared = false;
        this.breakPoints.clear();
        this.formattedStrings.clear();
        this.currentLetter = 0;
        this.currentLine = 0;
        this.aniTick = 0;
        this.name = name;
        this.aniTickPerFrame = speed;
        formatStrings(text);
        this.characterIndex = GetCharacterIndex(name);
        this.nameColor = getNameColor(name);
        this.portraitIndex = portraitIndex;
        this.setVoiceStuff();
    }

    // Regarding length limits for sentences:
    // There's a limit of 32 characters per sentence.
    // Though, a sentence may not end perfectly at 32 chars, causing us to 'loose'
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
            case "Speaker" -> Color.RED;
            case "Sign" -> Color.WHITE;
            case "Mechanic" -> Color.BLUE.brighter();
            default -> throw new IllegalArgumentException(
                "No nameColor available for '" + name + "'");
        };
        return color;
    }

    private void setVoiceStuff() {
        voiceClipIndex = switch(name) {
            case "Max" -> Audio.VOICECLIP_MAX;
            case "Oliver" -> Audio.VOICECLIP_OLIVER;
            case "Lance" -> Audio.VOICECLIP_LANCE;
            case "Charlotte" -> Audio.VOICECLIP_CHARLOTTE;
            case "Nina" -> Audio.VOICECLIP_NINA;
            case "Shady pilot", "Mechanic" -> Audio.VOICECLIP_SHADYPILOT;
            case "Speaker" -> Audio.VOICECLIP_SPEAKER;
            case "Sign" -> Audio.VOICECLIP_SIGN;
            default -> 0;
            };

        voiceTick = 0;
        if (aniTickPerFrame < 5) {
            voiceTickPerFrame = 5;
        }
        else {
            voiceTickPerFrame = aniTickPerFrame;
        }  
    }

    public void update() {
        updateTicks();
        checkIfDone();
    }

    private void checkIfDone() {
        allLettersAppeared = (
            (formattedStrings.size() == (currentLine + 1)) && 
            (currentLetter == (formattedStrings.get(currentLine).length()) - 1));
    }

    private void updateTicks() {
        aniTick ++;
        if (aniTick >= aniTickPerFrame) {
            currentLetter += 1;
            aniTick = 0;
            if (currentLetter - formattedStrings.get(currentLine).length() == 0) {
                currentLine += 1;
                currentLetter = 0;
            }
        }
        if (voiceTick % voiceTickPerFrame == 0) {
            if (!(formattedStrings.get(currentLine).charAt(currentLetter) == ' ')) {
                audioPlayer.playVoiceClip(voiceClipIndex);
            }
        }
        voiceTick++;
    }

    public void forwardDialogue() {
        this.currentLine = formattedStrings.size() - 1;
        this.currentLetter = formattedStrings.get(currentLine).length() - 1;
        this.allLettersAppeared = true;
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
        for (int i = 0; i < (currentLine + 1); i++) {
            int endLetter = formattedStrings.get(i).length();
            if (i == currentLine) {
                endLetter = currentLetter + 1;
            }
            drawPartialSentence(g, formattedStrings.get(i), 
                endLetter,
                X + (int) (200 * Game.SCALE), Y + (int) ((i * 35 + 100) * Game.SCALE));
        }
    }

    // Vi kunne forbedret kjøretiden ved å bruke stringBuilder.
    private void drawPartialSentence(Graphics g, String s, int currentLetter, int x, int y) {
        g.drawString(
            s.substring(0, currentLetter), x, y);
    }

    public boolean allLettersAppeared() {
        return allLettersAppeared;
    }
}
