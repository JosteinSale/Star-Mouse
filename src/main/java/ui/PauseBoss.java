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
import gamestates.BossMode;
import gamestates.Gamestate;
import main_classes.Game;
import utils.LoadSave;
import utils.Constants.Audio;

public class PauseBoss {
   private Game game;
   private AudioPlayer audioPlayer;
   private BossMode bossMode;
   private OptionsMenu optionsMenu;
   private Color bgColor = new Color(0, 0, 0, 140);
   private Font headerFont;
   private Font menuFont;

   private final int CONTINUE = 0;
   private final int OPTIONS = 1;
   private final int MAIN_MENU = 2;
   private final int SKIP_BOSS = 3;
   private String[] menuOptions = { "Continue", "Options", "Main Menu", "Skip Boss"};
   private BufferedImage pointerImg;
   private int selectedIndex = 0;

   private int cursorX = 320;
   private int cursorMinY = 350;
   private int cursorMaxY = 550;
   private int cursorY = cursorMinY;
   private int menuOptionsDiff = (cursorMaxY - cursorMinY) / 3;

   public PauseBoss(Game game, BossMode bossMode, OptionsMenu optionsMenu) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      this.bossMode = bossMode;
      this.optionsMenu = optionsMenu;
      loadImages();
      loadFonts();
   }

   private void loadImages() {
      this.pointerImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);
   }

   private void loadFonts() {
      this.headerFont = LoadSave.getHeaderFont();
      this.menuFont = LoadSave.getNameFont();
   }

   public void update() {
      if (optionsMenu.isActive()) {
         optionsMenu.update();
      } else {
         handleKeyboardInputs();
      }
   }

   private void handleKeyboardInputs() {
      if (game.downIsPressed) {
         game.downIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         goDown();
      } else if (game.upIsPressed) {
         game.upIsPressed = false;
         audioPlayer.playSFX(Audio.SFX_CURSOR);
         goUp();
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;

         if (selectedIndex == CONTINUE) {
            this.bossMode.flipPause();
            this.audioPlayer.flipAudioOnOff();
         } else if (selectedIndex == OPTIONS) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            optionsMenu.setActive(true);
         } else if (selectedIndex == MAIN_MENU) {
            audioPlayer.stopAllLoops();
            bossMode.resetBossMode();
            game.resetMainMenu();
            Gamestate.state = Gamestate.MAIN_MENU;
         } else if (selectedIndex == SKIP_BOSS) {
            bossMode.flipPause();
            audioPlayer.flipAudioOnOff();
            bossMode.skipBoss();
         }
      }
   }

   private void goDown() {
      this.cursorY += menuOptionsDiff;
      this.selectedIndex++;
      if (selectedIndex > 3) {
         selectedIndex = 0;
         cursorY = cursorMinY;
      }
   }

   private void goUp() {
      this.cursorY -= menuOptionsDiff;
      this.selectedIndex--;
      if (selectedIndex < 0) {
         selectedIndex = 3;
         cursorY = cursorMaxY;
      }
   }

   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      // Background
      g.setColor(bgColor);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

      if (optionsMenu.isActive()) {
         optionsMenu.draw(g);
      } else {
         // Text
         g.setFont(headerFont);
         g.setColor(Color.WHITE);
         g.drawString("PAUSE", (int) (450 * Game.SCALE), (int) (200 * Game.SCALE));

         for (int i = 0; i < menuOptions.length; i++) {
            Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE), (int) ((cursorMinY - 40 + i * menuOptionsDiff) * Game.SCALE),
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

}
