package utils;

import static utils.Constants.UI.FONT_SIZE_HEADER;
import static utils.Constants.UI.FONT_SIZE_INFO;
import static utils.Constants.UI.FONT_SIZE_ITEM;
import static utils.Constants.UI.FONT_SIZE_MENU;
import static utils.Constants.UI.FONT_SIZE_NAME;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import main_classes.Game;
import rendering.MyImage;

public class ResourceLoader {
    // Fonts
    public static final String MAIN_FONT = "DTM-Mono.otf";

    public static MyImage getImage(String fileName) {
        BufferedImage image = null;
        InputStream is = ResourceLoader.class.getResourceAsStream(fileName);
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
        return new MyImage(image);
    }

    public static Font getItemFont() {
        String fileName = "/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_ITEM * Game.SCALE;
        return getFont(fileName, size);
    }

    public static Font getMenuFont() {
        String fileName = "/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_MENU * Game.SCALE;
        return getFont(fileName, size);
    }

    public static Font getInfoFont() {
        String fileName = "/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_INFO * Game.SCALE;
        return getFont(fileName, size);
    }

    public static Font getNameFont() {
        String fileName = "/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_NAME * Game.SCALE;
        return getFont(fileName, size);
    }

    public static Font getHeaderFont() {
        String fileName = "/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_HEADER * Game.SCALE;
        return getFont(fileName, size);
    }

    private static Font getFont(String fileName, float size) {
        Font font = null;
        InputStream is = ResourceLoader.class.getResourceAsStream(fileName);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        return font;
    }

    public static ArrayList<List<String>> getExpCutsceneData(Integer level) {
        File filePath = new File(System.getProperty("user.dir") +
                "/src/main/resources/exploring/cutscenes/level" + Integer.toString(level));
        return readAllCsvFilesInFolder(filePath);
    }

    public static List<String> getFlyCutsceneData(Integer level) {
        File filePath = new File(System.getProperty("user.dir") +
                "/src/main/resources/flying/cutscenes/level" + Integer.toString(level) + ".csv");
        return readCsvFile(filePath);
    }

    public static List<String> getBossCutsceneData(Integer bossNr) {
        File filePath = new File(System.getProperty("user.dir") +
                "/src/main/resources/bossMode/cutscenes/boss" + Integer.toString(bossNr) + ".csv");
        return readCsvFile(filePath);
    }

    /**
     * Reads from the flying-leveldata folder for a specific level.
     * In this folder, it locates a csv-file for a specific level.
     * It reads this csv-file, and returns a list with enemy-data as strings.
     */
    public static List<String> getFlyLevelData(Integer level) {
        File filePath = new File(System.getProperty("user.dir") +
                "/src/main/resources/flying/leveldata/level" + Integer.toString(level) + ".csv");
        return readCsvFile(filePath);
    }

    /**
     * Reads from the exploring-leveldata folder for a specific level.
     * Finds all csv-files in that folder. Each is for an area.
     * Returns a 2D-list, where the outer layer represents each area,
     * and the inner layer represents each object/element in that area.
     */
    public static ArrayList<List<String>> getExpLevelData(Integer level) {
        File filePath = new File(System.getProperty("user.dir") +
                "/src/main/resources/exploring/leveldata/level" + Integer.toString(level));
        return readAllCsvFilesInFolder(filePath);
    }

    public static List<String> getCinematicData(String fileName) {
        File filePath = new File(System.getProperty("user.dir") +
                "/src/main/resources/cinematic/cutscenes/" + fileName);
        return readCsvFile(filePath);
    }

    private static ArrayList<List<String>> readAllCsvFilesInFolder(File filePath) {
        ArrayList<List<String>> levelData = new ArrayList<>();
        File[] allFiles = filePath.listFiles();
        for (File file : allFiles) {
            if (!file.getName().endsWith(".csv")) {
                continue;
            }
            List<String> areaData = new ArrayList<>();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            areaData = reader.lines().toList();
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            levelData.add(areaData);
        }
        return levelData;
    }

    private static List<String> readCsvFile(File filePath) {
        List<String> flyData = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        flyData = reader.lines().toList();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flyData;
    }
}
