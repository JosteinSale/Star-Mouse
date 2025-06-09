package utils;

import static utils.Constants.UI.FONT_SIZE_HEADER;
import static utils.Constants.UI.FONT_SIZE_INFO;
import static utils.Constants.UI.FONT_SIZE_ITEM;
import static utils.Constants.UI.FONT_SIZE_MENU;
import static utils.Constants.UI.FONT_SIZE_NAME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import main_classes.Game;
import rendering.MyImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ResourceLoader {
   public static final String MAIN_FONT = "exploring/fonts/DTM-Mono.otf";
   private static String absolutePath = Constants.ABSOLUTE_PATH;

   public static MyImage getImage(String fileName) {
      FileHandle fileHandle = Gdx.files.internal(fileName);
      Texture texture = new Texture(fileHandle);
      return new MyImage(texture);
   }

   public static BufferedImage getCollisionImage(String fileName) {
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
      return image;
   }

   private static BitmapFont generateFont(String filePath, float size) {
      FileHandle fontFile = Gdx.files.internal(filePath);
      FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
      FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
      parameter.size = (int) (size * Game.SCALE);
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

   public static ArrayList<List<String>> getExpCutsceneData(Integer level) {
      FileHandle dir = Gdx.files.internal(absolutePath + "exploring/cutscenes/level" + level);
      return readAllCsvFilesInFolder(dir);
   }

   public static List<String> getFlyCutsceneData(Integer level) {
      FileHandle file = Gdx.files.internal(absolutePath + "flying/cutscenes/level" + level + ".csv");
      return readCsvFile(file);
   }

   public static List<String> getBossCutsceneData(Integer bossNr) {
      FileHandle file = Gdx.files.internal(absolutePath + "bossMode/cutscenes/boss" + bossNr + ".csv");
      return readCsvFile(file);
   }

   public static List<String> getFlyLevelData(Integer level) {
      FileHandle file = Gdx.files.internal(absolutePath + "flying/leveldata/level" + level + ".csv");
      return readCsvFile(file);
   }

   public static ArrayList<List<String>> getExpLevelData(Integer level) {
      FileHandle dir = Gdx.files.internal(absolutePath + "exploring/leveldata/level" + level);
      return readAllCsvFilesInFolder(dir);
   }

   public static List<String> getCinematicData(String fileName) {
      FileHandle file = Gdx.files.internal(absolutePath + "cinematic/cutscenes/" + fileName);
      return readCsvFile(file);
   }

   private static ArrayList<List<String>> readAllCsvFilesInFolder(FileHandle folder) {
      ArrayList<List<String>> levelData = new ArrayList<>();
      for (FileHandle file : folder.list()) {
         if (file.extension().equals("csv")) {
            levelData.add(readCsvFile(file));
         }
      }
      return levelData;
   }

   private static List<String> readCsvFile(FileHandle file) {
      List<String> lines = new ArrayList<>();
      Scanner scanner = new Scanner(file.readString());
      while (scanner.hasNextLine()) {
         lines.add(scanner.nextLine());
      }
      scanner.close();
      return lines;
   }
}
