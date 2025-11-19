package utils;

import static utils.Constants.UI.FONT_SIZE_HEADER;
import static utils.Constants.UI.FONT_SIZE_INFO;
import static utils.Constants.UI.FONT_SIZE_ITEM;
import static utils.Constants.UI.FONT_SIZE_MENU;
import static utils.Constants.UI.FONT_SIZE_NAME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import entities.MyCollisionImage;
import rendering.MyImage;

import java.util.ArrayList;
import java.util.List;

public class ResourceLoader {
   public static final String MAIN_FONT = "exploring/fonts/DTM-Mono.otf";

   public static MyImage getImage(String fileName) {
      FileHandle fileHandle = Gdx.files.internal(fileName);
      Texture texture = new Texture(fileHandle);
      return new MyImage(texture);
   }

   public static MyCollisionImage getCollisionImage(String fileName) {
      Pixmap clImg = new Pixmap(Gdx.files.internal(fileName));
      return new MyCollisionImage(clImg);
   }

   public static Music getSong(String string) {
      Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + string));
      return music;
   }

   public static Sound getSound(String string) {
      Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/" + string));
      return sound;
   }

   private static BitmapFont generateFont(String filePath, float size) {
      FileHandle fontFile = Gdx.files.internal(filePath);
      FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
      FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
      parameter.size = (int) (size);
      parameter.color = Color.WHITE;
      BitmapFont font = generator.generateFont(parameter);
      font.getData().scaleY = -1f; // Flip font vertically
      generator.dispose();
      return font;
   }

   public static BitmapFont getItemFont() {
      return generateFont(MAIN_FONT, FONT_SIZE_ITEM);
   }

   public static BitmapFont getMenuFont() {
      return generateFont(MAIN_FONT, FONT_SIZE_MENU);
   }

   public static BitmapFont getInfoFont() {
      return generateFont(MAIN_FONT, FONT_SIZE_INFO);
   }

   public static BitmapFont getNameFont() {
      return generateFont(MAIN_FONT, FONT_SIZE_NAME);
   }

   public static BitmapFont getHeaderFont() {
      return generateFont(MAIN_FONT, FONT_SIZE_HEADER);
   }

   public static List<String> getFlyCutsceneData(Integer level) {
      FileHandle file = Gdx.files.internal("flying/cutscenes/level" + level + ".csv");
      return readCsvFile(file);
   }

   public static List<String> getBossCutsceneData(Integer bossNr) {
      FileHandle file = Gdx.files.internal("bossMode/cutscenes/boss" + bossNr + ".csv");
      return readCsvFile(file);
   }

   public static List<String> getFlyLevelData(Integer level) {
      FileHandle file = Gdx.files.internal("flying/leveldata/level" + level + ".csv");
      return readCsvFile(file);
   }

   public static ArrayList<List<String>> getExpLevelData(Integer lvl) {
      return readAllCsvFilesInFolder("exploring/leveldata/", lvl);
   }

   public static ArrayList<List<String>> getExpCutsceneData(Integer lvl) {
      return readAllCsvFilesInFolder("exploring/cutscenes/", lvl);
   }

   /*
    * This method might seem a bit stupid, but it fixes a peculiar issue:
    * When the game is exported as a jar, the way folders and files are located
    * changes. This makes it complicated to list all files in a folder, apparently.
    * This method ensures that we can get the exact name of the file to read
    * (which works better with LibGDX).
    */
   private static ArrayList<List<String>> readAllCsvFilesInFolder(String folderName, Integer lvl) {
      ArrayList<List<String>> allCsvData = new ArrayList<>();
      for (int i = 1; i <= getNumberOfAreasInLevel(lvl); i++) {
         String fileName = folderName + "level" + lvl + "/level" + lvl + "_area" + i + ".csv";
         FileHandle file = Gdx.files.internal(fileName);
         allCsvData.add(readCsvFile(file));
      }
      return allCsvData;
   }

   public static List<String> getCinematicData(String fileName) {
      FileHandle file = Gdx.files.internal("cinematic/cutscenes/" + fileName);
      return readCsvFile(file);
   }

   private static List<String> readCsvFile(FileHandle file) {
      String content = file.readString("UTF-8");
      String[] lines = content.split("\\R");
      return new ArrayList<>(java.util.Arrays.asList(lines));
   }

   private static int getNumberOfAreasInLevel(Integer level) {
      int nrOfLevels = switch (level) {
         case 1 -> 5;
         case 2 -> 3;
         case 3 -> 2;
         case 4 -> 1;
         case 5 -> 3;
         case 6 -> 1;
         case 7 -> 1;
         case 8 -> 1;
         case 9 -> 1;
         case 10 -> 1;
         case 11 -> 1;
         case 12 -> 1;
         case 13 -> 1;
         default -> throw new IllegalArgumentException(
               "Amount of levels not specified for level: " + level);
      };
      return nrOfLevels;
   }

}
