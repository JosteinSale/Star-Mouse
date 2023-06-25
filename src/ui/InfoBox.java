package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import utils.LoadSave;
import static utils.Constants.UI.*;

public class InfoBox {
    private Font infoFont;
    private String text;                 // Bør ikke være lengre enn 85 bokstaver
    private ArrayList<String> formattedString;
    private BufferedImage infoBoxImg;
    private int X = (int) ((Game.GAME_DEFAULT_WIDTH/2 -  INFOBOX_WIDTH/2) * Game.SCALE);
    private int Y = (int) (580 * Game.SCALE);

    public InfoBox(String text) {
        infoBoxImg = LoadSave.getImageSprite(LoadSave.INFO_BOX);
        infoFont = LoadSave.getInfoFont();
        this.text = text;
        this.formattedString = new ArrayList<>();
        formatString();
    }

    private void formatString() {
        String[] words = text.split(" ");
        int letterCount = 0;
        String line = "";
        for (String word : words) {
            if ((letterCount + word.length()) > INFOBOX_MAX_LETTERS) {
                formattedString.add(line);
                line = word + " ";
                letterCount = 0;
            } else {
                line += (word + " ");
                letterCount += word.length();
                letterCount += 1;  // for mellomrom
            }
        }
        formattedString.add(line);
    }

    public void draw(Graphics g) {
        g.drawImage(
            infoBoxImg, X, Y, 
            (int) (INFOBOX_WIDTH * Game.SCALE), 
            (int) (INFOBOX_HEIGHT * Game.SCALE), null);

        g.setColor(Color.BLACK);
        g.setFont(infoFont);   // Er allerede justert ift Game.SCALE
        for (int i = 0; i < formattedString.size(); i++) {
            g.drawString(
                formattedString.get(i), 
                X + (int) (50 * Game.SCALE), 
                Y + (int) (45 * Game.SCALE) + (int)((i * 40) * Game.SCALE));
        }
    }
}
