package utils.parsing;

import static audio.MusicPlayer.SONG_MAP;
import static audio.MusicPlayer.AMBIENCE_MAP;
import utils.Constants.Audio;

public class AudioParser {

   /**
    * Checks if the sfx-string matches any standard names. Else it
    * tries to parse the string to an integer.
    */
   public static Integer ParseSFX(String string) {
      Integer index = switch (string) {
         case "infoBox" -> Audio.SFX_INFOBOX;
         case "pickup" -> Audio.SFX_INVENTORY_PICKUP;
         case "success" -> Audio.SFX_SUCCESS;
         default -> Integer.parseInt(string);
      };
      return index;
   }

   public static String ParseSongId(String songId) {
      if (!SONG_MAP.containsKey(songId)) {
         throw new IllegalArgumentException("No song loaded for songId: " + songId);
      }
      return SONG_MAP.get(songId);
   }

   public static String ParseAmbienceId(String ambienceId) {
      if (!AMBIENCE_MAP.containsKey(ambienceId)) {
         throw new IllegalArgumentException("No ambience loaded for ambienceId: " + ambienceId);
      }
      return AMBIENCE_MAP.get(ambienceId);
   }

}
