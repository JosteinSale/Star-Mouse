package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Flying;
import gamestates.Gamestate;

import static utils.HelpMethods.DrawCenteredString;

import main.Game;
import utils.Constants.Audio;
import utils.LoadSave;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

/** Should be set to active when the player dies. It does the following:
 *  1) play death animation at the player's last position,
 *  2) display menu options.
 */
public class GameoverOverlay {
   private Flying flying;
   private AudioPlayer audioPlayer;
   private Color bgColor = new Color(0, 0, 0, 140);
   private Font headerFont;
   private Font menuFont;

   private String[] menuOptions = { "Restart Level", "Main Menu" };
   private BufferedImage pointerImg;
   private BufferedImage[] deathAnimation;
   private int selectedIndex = 0;
   private static final int RESTART_LEVEL = 0;
   private static final int MAIN_MENU = 0;

   private int cursorX = 320;
   private int cursorMinY = 490;
   private int cursorMaxY = 620;
   private int cursorY = cursorMinY;
   private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 2;

   private boolean active = false;
   private boolean deathAnimationActive = true;
   private int aniTick = 0;
   private int aniIndex = 0;
   private int aniTickPerFrame = 5;

   public GameoverOverlay(Flying flying) {
      this.flying = flying;
      this.audioPlayer = flying.getGame().getAudioPlayer();
      loadImages();
      loadFonts();
   }

   private void loadImages() {
      this.pointerImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);

      BufferedImage deathImg = LoadSave.getFlyImageSprite(LoadSave.SHIP_DEATH_SPRITES);
      this.deathAnimation = new BufferedImage[17];
      for (int i = 0; i < deathAnimation.length; i++) {
         deathAnimation[i] = deathImg.getSubimage(
            i * 40, 0, 40, 40);
         }
   }

   private void loadFonts() {
      this.headerFont = LoadSave.getHeaderFont();
      this.menuFont = LoadSave.getNameFont();
   }

   public void update() {
      if (deathAnimationActive) {
         updateAniTick();
      } else {
         handleKeyboardInputs();
      }
   }

   private void updateAniTick() {
      
   }

   private void handleKeyboardInputs() {
      if (flying.getGame().downIsPressed) {
         flying.getGame().downIsPressed = false;
         goDown();
      } else if (flying.getGame().upIsPressed) {
         flying.getGame().upIsPressed = false;
         goUp();
      } else if (flying.getGame().interactIsPressed) {
         flying.getGame().interactIsPressed = false;
         if (selectedIndex == RESTART_LEVEL) {
            // TODO - flying.reset(), this.active = false;
         } else if (selectedIndex == MAIN_MENU) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            
         } else if (selectedIndex == 2) {
            audioPlayer.stopAllLoops();
            // TODO - call flying.exitFlying() - This resets all non-level-specific
            // variables.
            // Level-specific objects are reset and loaded in the load-methods.
            Gamestate.state = Gamestate.MAIN_MENU;
         }
      }
   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 1) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 1;
         cursorY = cursorMaxY;
      }
   }

   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      // Background
      g.setColor(bgColor);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

         // Text
         g.setFont(headerFont);
         g.setColor(Color.WHITE);
         g.drawString("PAUSE", (int) (450 * Game.SCALE), (int) (350 * Game.SCALE));

         for (int i = 0; i < menuOptions.length; i++) {
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
}
