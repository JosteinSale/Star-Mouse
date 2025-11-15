package inputs;

import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;

import com.badlogic.gdx.Input;

import gamestates.Gamestate;
import main_classes.Game;
import utils.Singleton;

public class KeyboardInputs extends Singleton implements InputProcessor {
   private Game game;
   public int up;
   public int down;
   public int right;
   public int left;
   public int interact;
   public int shootBomb;
   public int teleport;
   public int pause;
   public int toggleFullscreen;

   public HashMap<String, KeyBindingVariant> keyBindingVariants;
   public static final String VARIANT_A = "A";
   public static final String VARIANT_B = "B";
   public static final String VARIANT_C = "C";
   public static String currentVariant = VARIANT_C;

   public KeyboardInputs(Game game) {
      this.game = game;
      this.constructKeyBindingVariants();
      this.setNewKeyBinding(currentVariant);
   }

   private void constructKeyBindingVariants() {
      // Variant A:
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

      // Variant B:
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

      // Variant C:
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

      // Construct the hashmap
      keyBindingVariants = new HashMap<>();
      keyBindingVariants.put(VARIANT_A, kbvA);
      keyBindingVariants.put(VARIANT_B, kbvB);
      keyBindingVariants.put(VARIANT_C, kbvC);
   }

   public void setNewKeyBinding(String variant) {
      currentVariant = variant;
      KeyBindingVariant kbv = keyBindingVariants.get(variant);
      this.up = kbv.up;
      this.down = kbv.down;
      this.right = kbv.right;
      this.left = kbv.left;
      this.interact = kbv.interact;
      this.shootBomb = kbv.shootBomb;
      this.teleport = kbv.teleport;
      this.pause = kbv.pause;
      this.toggleFullscreen = kbv.toggleFullscreen;
   }

   public KeyBindingVariant getKeyBindingVariant(String variant) {
      return keyBindingVariants.get(variant);
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == up) {
         game.upIsPressed = true;
      }
      if (keycode == down) {
         game.downIsPressed = true;
      }
      if (keycode == right) {
         game.rightIsPressed = true;
      }
      if (keycode == left) {
         game.leftIsPressed = true;
      }
      if (keycode == interact) {
         game.interactIsPressed = true;
      }
      if (keycode == teleport) {
         game.teleportIsPressed = true;
      }
      if (keycode == shootBomb) {
         game.bombIsPressed = true;
      }
      if (keycode == pause) {
         game.pauseIsPressed = true;
      }
      if (keycode == toggleFullscreen) {
         game.fullScreenIsPressed = true;
      }
      if (Gamestate.state == Gamestate.LEVEL_EDITOR) {
         // game.getLevelEditor().handleKeyboardInputs(keycode); // TODO
      }

      return false;
   }

   @Override
   public boolean keyUp(int keycode) {
      if (keycode == up) {
         game.upIsPressed = false;
      }
      if (keycode == down) {
         game.downIsPressed = false;
      }
      if (keycode == right) {
         game.rightIsPressed = false;
      }
      if (keycode == left) {
         game.leftIsPressed = false;
      }
      if (keycode == interact) {
         game.interactIsPressed = false;
      }
      if (keycode == teleport) {
         game.teleportIsPressed = false;
      }
      if (keycode == shootBomb) {
         game.bombIsPressed = false;
      }
      if (keycode == pause) {
         game.pauseIsPressed = false;
      }
      if (keycode == Input.Keys.ESCAPE) {
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

   // Container class for holding different keybindings
   public class KeyBindingVariant {
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
}
