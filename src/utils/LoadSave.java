package utils;

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

import main.Game;
import static utils.Constants.UI.FONT_SIZE_ITEM;
import static utils.Constants.UI.FONT_SIZE_MENU;
import static utils.Constants.UI.FONT_SIZE_INFO;
import static utils.Constants.UI.FONT_SIZE_NAME;
import static utils.Constants.UI.FONT_SIZE_HEADER;


public class LoadSave {
    // Menus
    public static final String BASIC_MOUSE = "BasicMouse.png";
    public static final String MAIN_MENU_TITLE = "main_menu_title.png";
    public static final String LEVEL_SELECT_BG = "level_select.png";
    public static final String LEVEL_SELECT_LAYOUT1 = "levelSel_layout1.png";
    public static final String LEVEL_SELECT_LAYOUT2 = "levelSel_layout2.png";
    public static final String LEVEL_SELECT_LAYOUT3 = "levelSel_layout3.png";
    
    // Sprites
    public static final String CURSOR_SPRITE_BLACK = "pointer_black.png";
    public static final String CURSOR_SPRITE_WHITE = "pointer_white.png";
    public static final String SLIDER_SPRITE = "slider.png";
    public static final String INFO_BOX = "infobox.png";
    public static final String DIALOGUE_BOX = "dialogue_box.png";
    public static final String ITEM_BOX = "itembox.png";
    public static final String ITEM_SELECTED = "item_selected.png";
    public static final String PRESENT_IMAGE = "item_present.png";
    public static final String MECHANIC_DISPLAY = "mechanic_background.png";
    public static final String NUMBER_DISPLAY = "number_display2.png";
    public static final String NUMBER_SELECT = "number_selected.png";
    public static final String KEY_SELECT = "key_selected.png";
    public static final String LEVEL_ICONS = "level_icons.png";
    public static final String LEVEL_SELECT_BOX = "level_select_box.png";

    public static final String PLAYER_EXP_SPRITES = "sprites_max.png";
    public static final String PLAYER_EXP_SPRITES_NAKED = "sprites_max_naked.png";
    public static final String OLIVER_SPRITES = "sprites_oliver_cadette.png";
    public static final String MAX_PORTRAITS = "portraits_max.png";
    public static final String OLIVER_PORTRAITS = "portraits_oliver_cadette.png";
    public static final String NPC_PORTRAITS = "portraits_npc.png";

    public static final String SHIP_SPRITES = "sprites_ship.png";
    public static final String FELLOWSHIP_SPRITES = "sprite_fellowShip.png";
    public static final String SHIP_FLAME_SPRITES = "sprites_shipFlame.png";
    public static final String SHIP_DEATH_SPRITES = "sprites_death.png";
    public static final String POWERUP_SPRITE = "sprite_powerup.png";
    public static final String REPAIR_SPRITE = "sprite_repair.png";
    public static final String ENEMYCOUNTER_SPRITE = "sprite_counter3.png";
    public static final String BOMB_PICKUP_SPRITE = "sprite_bombPickup.png";
    public static final String BOMB_SPRITE = "sprite_bomb.png";
    public static final String BOMB_EXPLOSION_SPRITE = "sprite_bombExplosion.png";
    public static final String PLAYER_PROJECTILE_BLUE = "player_prjct1.png";
    public static final String PLAYER_PROJECTILE_GREEN = "player_prjct2.png";
    public static final String DRONE_PROJECTILE = "drone_projectile.png";
    public static final String OCTADRONE_PROJECTILE = "octaDrone_projectile.png";
    public static final String REAPERDRONE_PROJECTILE = "reaper_projectile.png";
    public static final String PROJECTILE_HIT = "sprites_projectileHit.png";
    public static final String EXPLOSION = "sprites_explosion.png";
    public static final String TARGET_SPRITE = "sprites_target.png";
    public static final String DRONE_SPRITE = "sprites_drone.png";
    public static final String SMALLSHIP_SPRITE = "sprites_smallShip.png";
    public static final String OCTADRONE_SPRITE = "sprites_octaDrone.png";
    public static final String TANKDRONE_SPRITE = "sprites_tankDrone.png";
    public static final String BLASTERDRONE_SPRITE = "sprites_blasterDrone.png";
    public static final String REAPERDRONE_SPRITE = "sprites_reaperDrone.png";
    public static final String FLAMEDRONE_SPRITE = "sprites_flameDrone.png";
    public static final String WASPDRONE_SPRITE = "sprites_waspDrone.png";
    public static final String KAMIKAZEDRONE_SPRITE = "sprites_kamikazeDrone.png";
    public static final String FLAME_PROJECTILE = "sprites_droneFlame.png";

    public static final String SMALL_ENTITY_SPRITES = "sprites_all.png";

    // Fonts
    public static final String MAIN_FONT = "DTM-Mono.otf";

    // Methods
    public static BufferedImage getExpImageLandscape(String fileName) {
        fileName = "/resources/exploring/images/landscapes/" + fileName;
        return getImage(fileName);
    }

    
    public static BufferedImage getExpImageBackground(String fileName) {
        fileName = "/resources/exploring/images/backgrounds/" + fileName;
        return getImage(fileName);
    }

    public static BufferedImage getFlyImageBackground(String fileName) {
        fileName = "/resources/flying/images/backgrounds/" + fileName;
        return getImage(fileName);
    }

    public static BufferedImage getExpImageSprite(String fileName) {
        fileName = "/resources/exploring/images/sprites/" + fileName;
        return getImage(fileName);
    }

    public static BufferedImage getFlyImageSprite(String fileName) {
        fileName = "/resources/flying/images/sprites/" + fileName;
        return getImage(fileName);
    }

    public static BufferedImage getExpImageCollision(String fileName) {
        fileName = "/resources/exploring/images/collision/" + fileName;
        return getImage(fileName);
    }

    public static BufferedImage getFlyImageCollision(String fileName) {
        fileName = "/resources/flying/images/collision/" + fileName;
        return getImage(fileName);
    }

    public static BufferedImage getExpImageForeground(String fileName) {
        fileName = "/resources/exploring/images/foregrounds/" + fileName;
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

    public static Font getItemFont() {
        String fileName = "/resources/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_ITEM * Game.SCALE;
        return getFont(fileName, size);
    }

    public static Font getMenuFont() {
        String fileName = "/resources/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_MENU * Game.SCALE;
        return getFont(fileName, size);
    }

    public static Font getInfoFont() {
        String fileName = "/resources/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_INFO * Game.SCALE;
        return getFont(fileName, size);
    }

    public static Font getNameFont() {
        String fileName = "/resources/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_NAME * Game.SCALE;
        return getFont(fileName, size);
    }

    public static Font getHeaderFont() {
        String fileName = "/resources/exploring/fonts/" + MAIN_FONT;
        float size = FONT_SIZE_HEADER * Game.SCALE;
        return getFont(fileName, size);
    }

    private static Font getFont(String fileName, float size) {
        Font font = null;
        InputStream is = LoadSave.class.getResourceAsStream(fileName);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        return font;
    }

    public static ArrayList<List<String>> getExpCutsceneData(Integer level) {
        File filePath = new File(System.getProperty("user.dir") + 
            "/src/resources/exploring/cutscenes/level" + Integer.toString(level));
        return getExpCsvData(filePath);
    }

    public static List<String> getFlyCutsceneData(Integer level) {
        File filePath = new File(System.getProperty("user.dir") + 
            "/src/resources/flying/cutscenes/level" + Integer.toString(level) + ".csv");
        return getFlyCsvData(filePath);
    }

    /** Reads from the flying-leveldata folder for a specific level. 
     * In this folder, it locates a csv-file for a specific level.
     * It reads this csv-file, and returns a list with enemy-data as strings.
     */
    public static List<String> getFlyLevelData(Integer level) {
        File filePath = new File(System.getProperty("user.dir") + 
            "/src/resources/flying/leveldata/level" + Integer.toString(level) + ".csv");
        return getFlyCsvData(filePath);
    }

    /** Reads from the exploring-leveldata folder for a specific level. 
     * Finds all csv-files in that folder. Each is for an area.
     * Returns a 2D-list, where the outer layer represents each area,
     *  and the inner layer represents each object/element in that area.
     */
    public static ArrayList<List<String>> getExpLevelData(Integer level) {
        File filePath = new File(System.getProperty("user.dir") + 
            "/src/resources/exploring/leveldata/level" + Integer.toString(level));
        return getExpCsvData(filePath);
    }

    private static ArrayList<List<String>> getExpCsvData(File filePath) {
        ArrayList<List<String>> levelData = new ArrayList<>();
        File[] allFiles = filePath.listFiles();
        for (File file : allFiles) {
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

    private static List<String> getFlyCsvData(File filePath) {
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
