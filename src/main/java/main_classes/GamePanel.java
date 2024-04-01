package main_classes;

import static main_classes.Game.GAME_HEIGHT;
import static main_classes.Game.GAME_WIDTH;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyboardInputs;

// Det faktiske bildet. Lytter også etter keyboardInputs.
public class GamePanel extends JPanel {  
    private Game game;
    private KeyboardInputs keyboardInputs;

    public GamePanel(Game game) {
        this.game = game;
        this.keyboardInputs = new KeyboardInputs(this, game);
        addKeyListener(keyboardInputs);
        setPanelSize(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);   // Denne linjen bør være i denne klassen
        this.setDoubleBuffered(true);
    }

    private void setPanelSize(int screen_width, int screen_height) {              // Notat 1
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        this.setMinimumSize(size);
        this.setPreferredSize(size);
    }

    public Game getGame() {
        return this.game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public KeyboardInputs getKeyboardInputs() {
        return this.keyboardInputs;
    }

}

// 1. Å sette størrelsen inni JPanel istedenfor JFrame, sikrer at selve spillvinduet
//   blir riktig størrelse (blir ikke påvirket av borders etc)
