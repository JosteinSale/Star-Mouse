package ui;

import static utils.Constants.UI.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main_classes.Game;
import utils.ResourceLoader;

public class InfoBox {
    private Font infoFont;
    private ArrayList<String> formattedStrings;
    private BufferedImage infoBoxImg;
    private int X = (int) ((Game.GAME_DEFAULT_WIDTH/2 -  INFOBOX_WIDTH/2) * Game.SCALE);
    private int Y = (int) (580 * Game.SCALE);

    public InfoBox() {
        infoBoxImg = ResourceLoader.getExpImageSprite(ResourceLoader.INFO_BOX);
        infoFont = ResourceLoader.getInfoFont();
        this.formattedStrings = new ArrayList<>();
    }

    public void setText(String text) {
        this.formattedStrings = formatString(text);
    }

    private ArrayList<String> formatString(String text) {
        ArrayList<String> formattedString = new ArrayList<>();
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
        return formattedString;
    }

    public void draw(Graphics g) {
        g.drawImage(
            infoBoxImg, X, Y, 
            (int) (INFOBOX_WIDTH * Game.SCALE), 
            (int) (INFOBOX_HEIGHT * Game.SCALE), null);

        g.setColor(Color.BLACK);
        g.setFont(infoFont);   // Er allerede justert ift Game.SCALE
        for (int i = 0; i < formattedStrings.size(); i++) {
            g.drawString(
                formattedStrings.get(i), 
                X + (int) (60 * Game.SCALE), 
                Y + (int) (60 * Game.SCALE) + (int)((i * 40) * Game.SCALE));
        }
    }
}
