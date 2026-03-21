package inputs;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input;
import java.util.HashMap;
import gamestates.Gamestate;
import main_classes.Game;
import utils.Singleton;

public class Inputs extends Singleton implements InputProcessor {
   private Game game;

   // Keyboard inpups
   public static boolean upIsPressed = false;
   public static boolean downIsPressed = false;
   public static boolean rightIsPressed = false;
   public static boolean leftIsPressed = false;
   public static boolean interactIsPressed = false;
   public static boolean teleportIsPressed = false;
   public static boolean bombIsPressed = false;
   public static boolean pauseIsPressed = false;
   public static boolean fullScreenIsPressed = false;

   // Keybdinding variants
   public static final String VARIANT_A = "A";
   public static final String VARIANT_B = "B";
   public static final String VARIANT_C = "C";
   public static String currentVariant = VARIANT_C;
   private static final HashMap<String, KeyBindingVariant> keyBindingVariants = new HashMap<>();

   // Container class for holding different keybindings
   public static class KeyBindingVariant {
      public int up;
      public int down;
      public int right;
      public int left;
      public int interact;
      public int shootBomb;
      public int teleport;
      public int pause;
      public int toggleFullscreen;
   }

   static {
      // Construct different keybinding variants
      KeyBindingVariant kbvA = new KeyBindingVariant();
      kbvA.up = Input.Keys.W;
      kbvA.down = Input.Keys.S;
      kbvA.right = Input.Keys.D;
      kbvA.left = Input.Keys.A;
      kbvA.interact = Input.Keys.SPACE;
      kbvA.shootBomb = Input.Keys.COMMA;
      kbvA.teleport = Input.Keys.M;
      kbvA.pause = Input.Keys.ENTER;
      kbvA.toggleFullscreen = Input.Keys.ESCAPE;

      KeyBindingVariant kbvB = new KeyBindingVariant();
      kbvB.up = Input.Keys.UP;
      kbvB.down = Input.Keys.DOWN;
      kbvB.right = Input.Keys.RIGHT;
      kbvB.left = Input.Keys.LEFT;
      kbvB.interact = Input.Keys.SPACE;
      kbvB.shootBomb = Input.Keys.CAPS_LOCK;
      kbvB.teleport = Input.Keys.SHIFT_LEFT;
      kbvB.pause = Input.Keys.ENTER;
      kbvB.toggleFullscreen = Input.Keys.ESCAPE;

      KeyBindingVariant kbvC = new KeyBindingVariant();
      kbvC.up = Input.Keys.UP;
      kbvC.down = Input.Keys.DOWN;
      kbvC.right = Input.Keys.RIGHT;
      kbvC.left = Input.Keys.LEFT;
      kbvC.interact = Input.Keys.SPACE;
      kbvC.shootBomb = Input.Keys.Z;
      kbvC.teleport = Input.Keys.C;
      kbvC.pause = Input.Keys.ENTER;
      kbvC.toggleFullscreen = Input.Keys.ESCAPE;

      keyBindingVariants.put(VARIANT_A, kbvA);
      keyBindingVariants.put(VARIANT_B, kbvB);
      keyBindingVariants.put(VARIANT_C, kbvC);
   }
   private static KeyBindingVariant kb = keyBindingVariants.get(currentVariant);

   public Inputs(Game game) {
      this.game = game;
   }

   public static void setNewKeyBinding(String newVariant) {
      currentVariant = newVariant;
      kb = keyBindingVariants.get(newVariant);
   }

   public KeyBindingVariant getKeyBinding(String variant) {
      return keyBindingVariants.get(variant);
   }

   public static KeyBindingVariant getCurrentKeyBinding() {
      return keyBindingVariants.get(currentVariant);
   }

   @Override
   public boolean keyDown(int key) {
      if (key == kb.up) {
         upIsPressed = true;
      }
      if (key == kb.down) {
         downIsPressed = true;
      }
      if (key == kb.right) {
         rightIsPressed = true;
      }
      if (key == kb.left) {
         leftIsPressed = true;
      }
      if (key == kb.interact) {
         interactIsPressed = true;
      }
      if (key == kb.teleport) {
         teleportIsPressed = true;
      }
      if (key == kb.shootBomb) {
         bombIsPressed = true;
      }
      if (key == kb.pause) {
         pauseIsPressed = true;
      }
      if (key == kb.toggleFullscreen) {
         fullScreenIsPressed = true;
      }
      if (Gamestate.state == Gamestate.LEVEL_EDITOR) {
         game.getLevelEditor().handleKeyboardInputs(key);
      }
      return false;
   }

   @Override
   public boolean keyUp(int keycode) {
      if (keycode == kb.up) {
         upIsPressed = false;
      }
      if (keycode == kb.down) {
         downIsPressed = false;
      }
      if (keycode == kb.right) {
         rightIsPressed = false;
      }
      if (keycode == kb.left) {
         leftIsPressed = false;
      }
      if (keycode == kb.interact) {
         interactIsPressed = false;
      }
      if (keycode == kb.teleport) {
         teleportIsPressed = false;
      }
      if (keycode == kb.shootBomb) {
         bombIsPressed = false;
      }
      if (keycode == kb.pause) {
         pauseIsPressed = false;
      }
      if (keycode == kb.toggleFullscreen) {
         fullScreenIsPressed = false;
      }
      return true;
   }

   @Override
   public boolean keyTyped(char character) {
      return false;
   }

   @Override
   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
      return false;
   }

   @Override
   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
      return false;
   }

   @Override
   public boolean touchDragged(int screenX, int screenY, int pointer) {
      return false;
   }

   @Override
   public boolean mouseMoved(int screenX, int screenY) {
      if (Gamestate.state == Gamestate.LEVEL_EDITOR) {
         game.getLevelEditor().mouseMoved(screenX, screenY);
      }
      return true;
   }

   @Override
   public boolean scrolled(float amountX, float amountY) {
      return false;
   }

}
