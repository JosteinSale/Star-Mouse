package inputs;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input;
import java.util.HashMap;
import gamestates.Gamestate;
import main_classes.Game;
import utils.Singleton;

public class KeyboardInputs extends Singleton implements InputProcessor {
   private Game game;

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

   public static final String VARIANT_A = "A";
   public static final String VARIANT_B = "B";
   public static final String VARIANT_C = "C";
   public static String currentVariant = VARIANT_C;
   private static final HashMap<String, KeyBindingVariant> keyBindingVariants = new HashMap<>();

   static {
      // populate the static map once
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

   public KeyboardInputs(Game game) {
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
   public boolean keyDown(int keycode) {
      if (keycode == kb.up) {
         game.upIsPressed = true;
      }
      if (keycode == kb.down) {
         game.downIsPressed = true;
      }
      if (keycode == kb.right) {
         game.rightIsPressed = true;
      }
      if (keycode == kb.left) {
         game.leftIsPressed = true;
      }
      if (keycode == kb.interact) {
         game.interactIsPressed = true;
      }
      if (keycode == kb.teleport) {
         game.teleportIsPressed = true;
      }
      if (keycode == kb.shootBomb) {
         game.bombIsPressed = true;
      }
      if (keycode == kb.pause) {
         game.pauseIsPressed = true;
      }
      if (keycode == kb.toggleFullscreen) {
         game.fullScreenIsPressed = true;
      }
      if (Gamestate.state == Gamestate.LEVEL_EDITOR) {
         // game.getLevelEditor().handleKeyboardInputs(keycode); // TODO
      }
      return false;
   }

   @Override
   public boolean keyUp(int keycode) {
      if (keycode == kb.up) {
         game.upIsPressed = false;
      }
      if (keycode == kb.down) {
         game.downIsPressed = false;
      }
      if (keycode == kb.right) {
         game.rightIsPressed = false;
      }
      if (keycode == kb.left) {
         game.leftIsPressed = false;
      }
      if (keycode == kb.interact) {
         game.interactIsPressed = false;
      }
      if (keycode == kb.teleport) {
         game.teleportIsPressed = false;
      }
      if (keycode == kb.shootBomb) {
         game.bombIsPressed = false;
      }
      if (keycode == kb.pause) {
         game.pauseIsPressed = false;
      }
      if (keycode == kb.toggleFullscreen) {
         game.fullScreenIsPressed = false;
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
      return false;
   }

   @Override
   public boolean scrolled(float amountX, float amountY) {
      return false;
   }

}
