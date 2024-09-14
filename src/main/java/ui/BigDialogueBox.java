package ui;

import static utils.Constants.UI.DIALOGUE_MAX_LETTERS;
import static utils.HelpMethods.GetCharacterIndex;

import java.util.ArrayList;
import java.util.Random;

import audio.AudioPlayer;
import main_classes.Game;

public class BigDialogueBox {
    private Game game;
    private AudioPlayer audioPlayer;
    public String name;
    public int characterIndex = 0;
    public int portraitIndex = 0;
    private int aniTick;
    private int aniTickPerFrame;
    public int currentLetter;
    public int currentLine = 0;
    private int voiceTick;
    private int voiceTickPerFrame;
    private boolean allLettersAppeared = false;
    private Random rand;

    private ArrayList<Integer> breakPoints;
    public ArrayList<String> formattedStrings;

    public BigDialogueBox(Game game) {
        this.game = game;
        this.audioPlayer = game.getAudioPlayer();
        this.rand = new Random();
        this.breakPoints = new ArrayList<>();
        this.formattedStrings = new ArrayList<>();
    }

    public void setDialogue(String name, int speed, String text, int portraitIndex) {
        this.game.getView().getRenderCutscene().setDialogue(name);
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
        this.portraitIndex = portraitIndex;
        this.setVoiceStuff();
    }

    // Regarding length limits for sentences:
    // There's a limit of 32 characters per sentence.
    // Though, a sentence may not end perfectly at 32 chars, causing us to 'loose'
    // those unused letters at the end of the sentence. Thus the limit is usually a
    // bit lower.
    // At most you can do 3 single words of length 32.
    // But if you approach 90, calculate how many letters you've lost before
    // running.
    private void formatStrings(String text) {
        String[] words = text.split(" ");
        int letterCount = 0;
        int breakCount = 0;
        for (String word : words) {
            letterCount += word.length();
            breakCount += word.length();
            if ((letterCount) > DIALOGUE_MAX_LETTERS) {
                breakCount -= (word.length() + 1); // Trekker fra et ord og et mellomrom
                breakPoints.add(breakCount);
                breakCount += word.length() + 1; // Legger til samme ord og mellomrom igjen

                letterCount = word.length(); // 'Nullsettes'
            }
            letterCount += 1; // +1 for mellomrom
            breakCount += 1;
        }
        breakPoints.add(text.length()); // Siste breakpoint

        int beginIndex = 0;
        for (Integer endIndex : breakPoints) {
            String line = text.substring(beginIndex, endIndex);
            formattedStrings.add(line);
            beginIndex += (endIndex + 1 - beginIndex);
        }
    }

    private void setVoiceStuff() {
        voiceTick = 0;
        if (aniTickPerFrame < 5) {
            voiceTickPerFrame = 5;
        } else {
            voiceTickPerFrame = aniTickPerFrame;
        }
    }

    public void update() {
        updateAnimations();
        updateVoices();
        checkIfDone();
    }

    private void checkIfDone() {
        allLettersAppeared = ((formattedStrings.size() == (currentLine + 1)) &&
                (currentLetter == (formattedStrings.get(currentLine).length()) - 1));
    }

    private void updateAnimations() {
        aniTick++;
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
            audioPlayer.playVoiceClip(name);
        } else if (voiceTick % voiceTickPerFrame == 0) {
            // In order for the voices to sound less 'machine gun'-like, we don't play the
            // voiceclip
            // on spaces, and also add a bit of randomization.
            if (!(currentCharIs(' ')) && randomizerRollsTrue()) {
                audioPlayer.playVoiceClip(name);
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

    public boolean allLettersAppeared() {
        return allLettersAppeared;
    }
}
