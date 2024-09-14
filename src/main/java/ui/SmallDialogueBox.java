package ui;

import static utils.HelpMethods.GetCharacterIndex;

import main_classes.Game;

public class SmallDialogueBox {
    private Game game;
    public String name;
    public int characterIndex = 0;
    public int portraitIndex = 0;
    private int aniTick;
    private int tickPerFrame;
    public int currentLetter = 0;
    private int totalLetters;
    public String text;

    public SmallDialogueBox(Game game) {
        this.game = game;
    }

    public void setDialogue(String name, int speed, String text, int portraitIndex) {
        this.game.getView().getRenderCutscene().setDialogue(name);
        this.totalLetters = text.length();
        this.currentLetter = 0;
        this.aniTick = 0;
        this.text = text;
        this.name = name;
        this.tickPerFrame = speed;
        this.characterIndex = GetCharacterIndex(name);
        this.portraitIndex = portraitIndex;
    }

    public void update() {
        aniTick++;
        if (aniTick >= tickPerFrame) {
            currentLetter += 1;
            aniTick = 0;
        }
    }

    public void forwardDialogue() {
        this.currentLetter = totalLetters;
    }

    public boolean allLettersAppeared() {
        return (currentLetter == totalLetters);
    }
}
