package ui;

import static utils.Constants.UI.DIALOGUEBOX_HEIGHT;
import static utils.Constants.UI.DIALOGUEBOX_WIDTH;
import static utils.Constants.UI.DIALOGUE_MAX_LETTERS;
import static utils.Constants.UI.PORTRAIT_SIZE;
import static utils.HelpMethods.GetCharacterIndex;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import audio.AudioPlayer;
import main_classes.Game;
import utils.HelpMethods;
import utils.ResourceLoader;
import utils.Constants.Audio;

public class BigDialogueBox {
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
    private Random rand;

    private Font dialogueFont;
    private Font nameFont;
    private ArrayList<Integer> breakPoints;
    private ArrayList<String> formattedStrings;
    private int X = (int) ((Game.GAME_DEFAULT_WIDTH/2 -  DIALOGUEBOX_WIDTH/2) * Game.SCALE);
    private int Y = (int) (550 * Game.SCALE);

    public BigDialogueBox(Game game) {
        this.audioPlayer = game.getAudioPlayer();
        this.rand = new Random();
        dialogueBoxImg = ResourceLoader.getExpImageSprite(ResourceLoader.DIALOGUE_BOX);
        dialogueFont = ResourceLoader.getInfoFont();
        nameFont = ResourceLoader.getNameFont();
        this.breakPoints = new ArrayList<>();
        this.formattedStrings = new ArrayList<>();
        this.portraits = BigDialogueBox.getAllPortraits();
    }

    public static BufferedImage[][] getAllPortraits() {
        BufferedImage[][] portraits;
        int nrOfSpecialCharacters = 4;
        int nrOfNpcs = 17;
        int maxAmountOfPortraits = 15;
        int nrOfCharacters = nrOfSpecialCharacters + nrOfNpcs;
        portraits = new BufferedImage[nrOfCharacters][maxAmountOfPortraits];
        portraits[0] = getPortraits(ResourceLoader.MAX_PORTRAITS, 15, 0);
        portraits[1] = getPortraits(ResourceLoader.OLIVER_PORTRAITS, 9, 0);
        portraits[2] = getPortraits(ResourceLoader.LT_RED_PORTRAITS, 6, 0);
        portraits[3] = getPortraits(ResourceLoader.RUDINGER_PORTRAITS, 7, 0);
        for (int i = 0; i < nrOfNpcs; i++) {
            portraits[i + nrOfSpecialCharacters] = getPortraits(ResourceLoader.NPC_PORTRAITS, 4, i);
        }
        return portraits;
    }

    private static BufferedImage[] getPortraits(String portraitName, int length, int rowIndex) {
        BufferedImage img = ResourceLoader.getExpImageSprite(portraitName);
        BufferedImage[] portraits = new BufferedImage[length];
        for (int i = 0; i < portraits.length; i++) { 
            portraits[i] = img.getSubimage(
                i * PORTRAIT_SIZE, rowIndex * PORTRAIT_SIZE, 
                PORTRAIT_SIZE, PORTRAIT_SIZE);
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
        this.nameColor = HelpMethods.GetNameColor(name);
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
            case "Lt.Red" -> Audio.VOICECLIP_LTRED;
            case "Russel" -> Audio.VOICECLIP_RUSSEL;
            case "Emma" -> Audio.VOICECLIP_EMMA;
            case "Nathan" -> Audio.VOICECLIP_NATHAN;
            case "Frida" -> Audio.VOICECLIP_FRIDA;
            case "Skye" -> Audio.VOICECLIP_SKYE;
            case "Zack" -> Audio.VOICECLIP_ZACK;
            case "Gard" -> Audio.VOICECLIP_GARD;
            case "Feno" -> Audio.VOICECLIP_FENO;
            case "???", "Rudinger" -> Audio.VOICECLIP_RUDINGER;
            case "????" -> Audio.VOICECLIP_RAZE;
            case "Drone" -> Audio.VOICECLIP_DRONE;
            default -> Audio.VOICECLIP_SIGN;
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
        updateAnimations();
        updateVoices();
        checkIfDone();
    }

    private void checkIfDone() {
        allLettersAppeared = (
            (formattedStrings.size() == (currentLine + 1)) && 
            (currentLetter == (formattedStrings.get(currentLine).length()) - 1));
    }

    private void updateAnimations() {
        aniTick ++;
        if (aniTick >= aniTickPerFrame) {
            currentLetter += 1;
            aniTick = 0;
            if (currentLetter - formattedStrings.get(currentLine).length() == 0) {
                currentLine += 1;
                currentLetter = 0;
            }
        }
    }

    private void updateVoices() { 
        if (voiceTick == 0) { 
            // Always play on first syllable
            audioPlayer.playVoiceClip(voiceClipIndex);
        } 
        else if (voiceTick % voiceTickPerFrame == 0) {
            // In order for the voices to sound less 'machine gun'-like, we don't play the voiceclip
            // on spaces, and also add a bit of randomization.
            if (!(currentCharIs(' ')) && randomizerRollsTrue()) {
                audioPlayer.playVoiceClip(voiceClipIndex);
            }
        }
        voiceTick++;
    }

    // Rolls true with an 80% chance
    private boolean randomizerRollsTrue() {
        double i = rand.nextDouble();
        return i < 0.8;
    }

    public void forwardDialogue() {
        this.currentLine = formattedStrings.size() - 1;
        this.currentLetter = formattedStrings.get(currentLine).length() - 1;
        this.allLettersAppeared = true;
    }

    private boolean currentCharIs(char c) {
        return formattedStrings.get(currentLine).charAt(currentLetter) == c;
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
