package data_storage;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

// OBS: This class was written by chatGPT

/** Handles loading and saving of data */
public class DataStorage {

   private static final ObjectMapper objectMapper = new ObjectMapper();
   private static final String FILE_NAME = "starMouseData.json";

   private static String getAppDataDirectory() {
      String appDataDirectory;

      String os = System.getProperty("os.name").toLowerCase();
      if (os.contains("win")) {
         appDataDirectory = System.getenv("APPDATA");
      } else if (os.contains("mac")) {
         appDataDirectory = System.getProperty("user.home") + "/Library/Application Support";
      } else {
         appDataDirectory = System.getProperty("user.home");
      }

      return appDataDirectory + "/StarMouse";
   }

   private static File getDataFile() {
      String appDataDirectory = getAppDataDirectory();
      File directory = new File(appDataDirectory);

      if (!directory.exists()) {
         directory.mkdirs();
      }

      return new File(directory, FILE_NAME);
   }

   public static void saveData(SaveData data) {
      try {
         File file = getDataFile();
         objectMapper.writeValue(file, data);
         System.out.println("Data saved to: " + file.getAbsolutePath());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static SaveData loadData() {
      try {
         File file = getDataFile();
         if (file.exists()) {
            System.out.println("Data loaded from: " + file.getAbsolutePath());
            return objectMapper.readValue(file, SaveData.class);
         } else {
            System.out.println("No data file found. A new file will be created at: " + file.getAbsolutePath());
            return null;
         }
      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }
   }
}
