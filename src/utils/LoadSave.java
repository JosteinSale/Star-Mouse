package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import main.Game;


public class LoadSave {
    // Backgrounds
    public static final String START_SCREEN_BG = "start_screen.png";
    public static final String MAIN_MENU_BG = "main_menu.png";
    public static final String LEVEL_SELECT_BG = "level_select.png";
    public static final String LEVEL1_AREA1_BG = "level1_area1.png";

    // Sprites
    public static final String CURSOR_SPRITE = "pointer.png";
    public static final String INFO_BOX = "infobox.png";

    // Collision
    public static final String LEVEL1_AREA1_CL = "level1_area1.png";

    // Fonts
    public static final String INFOBOX_FONT = "DTM-Mono.otf";

    
    public static BufferedImage getImageBackground(String fileName) {
        fileName = "/resources/images/backgrounds/" + fileName;
        return getImage(fileName);
    }

    public static BufferedImage getImageSprite(String fileName) {
        fileName = "/resources/images/sprites/" + fileName;
        return getImage(fileName);
    }

    public static BufferedImage getImageCollision(String fileName) {
        fileName = "/resources/images/collision/" + fileName;
        return getImage(fileName);
    }

    private static BufferedImage getImage(String fileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream(fileName);
        try {
            image = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public static Font getInfoFont() {
        String fileName = "/resources/fonts/" + INFOBOX_FONT;
        float size = 24f * Game.SCALE;
        return getFont(fileName, size);
    }

    private static Font getFont(String fileName, float size) {
        Font font = null;
        InputStream is = LoadSave.class.getResourceAsStream(fileName);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return font;
    }
}
