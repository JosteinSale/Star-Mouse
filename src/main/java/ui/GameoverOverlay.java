package ui;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Gamestate;
import gamestates.flying.Flying;
import main_classes.Game;
import utils.ResourceLoader;
import utils.Constants.Audio;

/** Should be updated and drawn when the player dies. It does the following:
 *  1) play death animation at the player's last position,
 *  2) display menu options,
 *  3) wait for player to choose an option.
 */
public class GameoverOverlay {
   private Flying flying;
   private AudioPlayer audioPlayer;
   private Color bgColor = new Color(0, 0, 0, 140);
   private Font headerFont;
   private Font menuFont;

   private String[] menuOptions = {"Last Checkpoint", "Restart Level", "Main Menu" };
   private BufferedImage pointerImg;
   private BufferedImage[] deathAnimation;
   private int selectedIndex = 0;
   private static final int LAST_CHECKPOINT = 0;
   private static final int RESTART_LEVEL = 1;
   private static final int MAIN_MENU = 2;

   private float playerX;
   private float playerY;
   private int cursorX = 270;
   private int cursorMinY = 490;
   private int cursorMaxY = 630;
   private int cursorY = cursorMinY;
   private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 2;

   private boolean deathAnimationActive = true;
   private int aniTick = 0;
   private int aniIndex = 0;
   private int aniTickPerFrame = 4;

   public GameoverOverlay(Flying flying) {
      this.flying = flying;
      this.audioPlayer = flying.getGame().getAudioPlayer();
      loadImages();
      loadFonts();
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);

      BufferedImage deathImg = ResourceLoader.getFlyImageSprite(ResourceLoader.SHIP_DEATH_SPRITES);
      this.deathAnimation = new BufferedImage[26];
      for (int i = 0; i < deathAnimation.length; i++) {
         deathAnimation[i] = deathImg.getSubimage(
            i * 40, 0, 40, 40);
         }
   }

   private void loadFonts() {
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getNameFont();
   }

   public void setPlayerPos(float x, float y) {
      this.playerX = x - 35; // animation offset
      this.playerY = y - 35;
   }

   public void update() {
      if (deathAnimationActive) {
         updateAniTick();
      } else {
         handleKeyboardInputs();
      }
   }

   private void updateAniTick() {
      aniTick++;
      if (aniTick == aniTickPerFrame) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex == 30) {  // 26 images + 4 cycles of wait-time.
            this.deathAnimationActive = false;
         }
      }
   }

   private void handleKeyboardInputs() {
      if (flying.getGame().downIsPressed) {
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         flying.getGame().downIsPressed = false;
         goDown();
      } 
      else if (flying.getGame().upIsPressed) {
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         flying.getGame().upIsPressed = false;
         goUp();
      } 
      else if (flying.getGame().interactIsPressed) {
         flying.getGame().interactIsPressed = false;
         if (selectedIndex == RESTART_LEVEL) {
            this.cursorY = cursorMinY;
            flying.resetFlying();
            flying.resetLevel(false);
         }
         else if (selectedIndex == LAST_CHECKPOINT) {
            if (!flying.checkPointReached) {
               audioPlayer.playSFX(Audio.SFX_HURT);
            }
            else {
               this.cursorY = cursorMinY;
               flying.resetFlying();
               flying.resetLevel(true);
            }
         }
         else if (selectedIndex == MAIN_MENU) {
            // We do not need to reset the level, since the loadLevel-method will be called
            // when the player reenters flying.
            this.cursorY = cursorMinY;
            flying.resetFlying();
            flying.getGame().resetMainMenu();
            Gamestate.state = Gamestate.MAIN_MENU;
         } 
      }
   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 2) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 2;
         cursorY = cursorMaxY;
      }
   }

   public void draw(Graphics g) {
      if (deathAnimationActive) {
         drawDeathAnimation(g);
      }
      else {
         drawMenu(g);
      }
   }

   private void drawDeathAnimation(Graphics g) {
      if (aniIndex < deathAnimation.length) {   // Will draw nothing at index 26 - 30
         g.drawImage(
            deathAnimation[aniIndex], 
            (int) (playerX * Game.SCALE), (int) (playerY * Game.SCALE),
            (int) (120 * Game.SCALE),
            (int) (120 * Game.SCALE), null);
      }
   }

   private void drawMenu(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      // Background
      g.setColor(bgColor);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

      // Text
      g.setFont(headerFont);
      g.setColor(Color.WHITE);
      g.drawString("YOU DIED", (int) (400 * Game.SCALE), (int) (350 * Game.SCALE));

      for (int i = 0; i < menuOptions.length; i++) {
         setTextColor(i, g);
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE), (int) ((450 + i * menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
         DrawCenteredString(g2, menuOptions[i], rect, menuFont);
      }

      // Cursor
      g2.drawImage(
         pointerImg,
         (int) (cursorX * Game.SCALE), (int) ((cursorY - 30) * Game.SCALE),
         (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
   }

   private void setTextColor(int index, Graphics g) {
      if (index == LAST_CHECKPOINT && !this.flying.checkPointReached) {
         g.setColor(new Color(255, 255, 255, 130));
      }
      else {
         g.setColor(Color.WHITE);
      }
   }

   public void reset() {
      deathAnimationActive = true;
      aniTick = 0;
      aniIndex = 0;
      selectedIndex = 0;
   }
}
