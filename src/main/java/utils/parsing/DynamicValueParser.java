package utils.parsing;

import com.badlogic.gdx.Input;

import inputs.KeyboardInputs;
import inputs.KeyboardInputs.KeyBindingVariant;

public class DynamicValueParser {

   /**
    * Words contained within '{}' will be replaced by dynamic values.
    * Remember to have a space after such words.
    * Currently supports:
    * -Keybindings
    * 
    * @param text
    * @return
    */
   public static String InsertDynamicValues(String text) {
      String[] words = text.split(" ");
      StringBuilder sb = new StringBuilder();
      for (String w : words) {
         if (w.startsWith("{")) {
            if (!w.contains("}")) {
               throw new IllegalStateException("Did you forget a '}'? Text: " + text);
            }
            String inner = w.substring(1, w.indexOf("}")); // remove '{' and '}''
            sb.append(parseWord(inner));
            sb.append(findTrailingChars(w));
         } else {
            sb.append(w);
         }
         sb.append(" ");
      }
      return sb.toString();
   }

   /** Adds any trailing characters after the '}'. Commas, periods, etc... */
   private static String findTrailingChars(String w) {
      return w.substring(w.indexOf("}") + 1, w.length());
   }

   private static String parseWord(String w) {
      KeyBindingVariant kb = KeyboardInputs.getCurrentKeyBinding();
      String parsedW = switch (w) {
         case "up" -> Input.Keys.toString(kb.up).toUpperCase();
         case "down" -> Input.Keys.toString(kb.down).toUpperCase();
         case "left" -> Input.Keys.toString(kb.left).toUpperCase();
         case "right" -> Input.Keys.toString(kb.right).toUpperCase();
         case "interact" -> Input.Keys.toString(kb.interact).toUpperCase();
         case "shootBomb" -> Input.Keys.toString(kb.shootBomb).toUpperCase();
         case "pause" -> Input.Keys.toString(kb.pause).toUpperCase();
         case "toggleFullscreen" -> Input.Keys.toString(kb.toggleFullscreen).toUpperCase();
         default -> throw new IllegalArgumentException("Wasn't able to parse word: " + w);
      };
      return parsedW;
   }
}
