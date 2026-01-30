package utils;

import entities.MyCollisionImage;
import rendering.MyImage;

/**
 * This class will keep all images in the game.
 */
public class Images extends Singleton {
   private ResourceContainer<MyImage> myImages;
   private ResourceContainer<MyCollisionImage> myCollisionImages;
   public static MyImage pixel = ResourceLoader.getImage("miscImages/pixel.png");

   // Menus
   public static final String BASIC_MOUSE = "BasicMouse.png";
   public static final String MAIN_MENU_TITLE = "main_menu_title.png";
   public static final String LEVEL_SELECT_BG = "level_select.png";
   public static final String LEVEL_SELECT_LAYOUT1 = "levelSel_Layout1.png";
   public static final String LEVEL_SELECT_LAYOUT2 = "levelSel_Layout2.png";
   public static final String LEVEL_SELECT_LAYOUT3 = "levelSel_Layout3.png";

   // Sprites - UI
   public static final String CURSOR_SPRITE_BLACK = "pointer_black.png";
   public static final String CURSOR_SPRITE_WHITE = "pointer_white.png";
   public static final String SLIDER_SPRITE = "slider.png";
   public static final String INFO_BOX = "infobox.png";
   public static final String DIALOGUE_BOX = "dialogue_box.png";
   public static final String ITEM_BOX = "itembox.png";
   public static final String ITEM_SELECTED = "item_selected.png";
   public static final String PRESENT_IMAGE = "item_present.png";
   public static final String MECHANIC_DISPLAY = "mechanic_background.png";
   public static final String NUMBER_DISPLAY = "number_display2.png";
   public static final String NUMBER_SELECT = "number_selected.png";
   public static final String LEVEL_ICONS = "level_icons.png";
   public static final String LEVEL_SELECT_BOX = "level_select_box.png";

   // Sprites - Exploring
   public static final String PLAYER_EXP_SPRITES = "sprites_max.png";
   public static final String PLAYER_EXP_SPRITES_NAKED = "sprites_max_naked.png";
   public static final String PLAYER_EXP_SPRITES_SAD = "sprites_max_sad.png";
   public static final String OLIVER_SPRITES = "sprites_oliver_cadette.png";
   public static final String GARD_SPRITES = "sprites_gard.png";
   public static final String MAX_PORTRAITS = "portraits_max.png";
   public static final String OLIVER_PORTRAITS = "portraits_oliver_cadette.png";
   public static final String LT_RED_PORTRAITS = "portraits_lt_red.png";
   public static final String RUDINGER_PORTRAITS = "portraits_rudinger.png";
   public static final String NPC_PORTRAITS = "portraits_npc.png";
   public static final String STANDARD_NPC_SPRITES = "sprites_npcs.png";

   // Sprites - Flying
   public static final String SHIP_SPRITES = "sprites_ship.png";
   public static final String TELEPORT_SHADOW = "teleport_shadow.png";
   public static final String FELLOWSHIP_SPRITES = "sprite_fellowShip.png";
   public static final String SHIP_FLAME_SPRITES = "sprites_shipFlame.png";
   public static final String SHIP_DEATH_SPRITES = "sprites_death.png";
   public static final String DELETE_SPRITE = "sprite_delete.png";
   public static final String POWERUP_SPRITE = "sprite_powerup.png";
   public static final String REPAIR_SPRITE = "sprite_repair.png";
   public static final String ENEMYCOUNTER_SPRITE = "sprite_counter3.png";
   public static final String BOMB_PICKUP_SPRITE = "sprite_bombPickup.png";
   public static final String BOMB_SPRITE = "sprite_bomb.png";
   public static final String BOMB_EXPLOSION_SPRITE = "sprite_bombExplosion.png";
   public static final String PLAYER_PROJECTILE_BLUE = "projectile_player1.png";
   public static final String PLAYER_PROJECTILE_GREEN = "projectile_player2.png";
   public static final String DRONE_PROJECTILE = "projectile_drone.png";
   public static final String OCTADRONE_PROJECTILE = "projectile_octadrone.png";
   public static final String REAPERDRONE_PROJECTILE = "projectile_reaper.png";
   public static final String PROJECTILE_HIT = "sprites_projectileHit.png";
   public static final String EXPLOSION = "sprites_explosion.png";
   public static final String TARGET_SPRITE = "sprites_target.png";
   public static final String DRONE_SPRITE = "sprites_drone.png";
   public static final String SMALLSHIP_SPRITE = "sprites_smallShip.png";
   public static final String OCTADRONE_SPRITE = "sprites_octaDrone.png";
   public static final String TANKDRONE_SPRITE = "sprites_tankDrone.png";
   public static final String BLASTERDRONE_SPRITE = "sprites_blasterDrone.png";
   public static final String REAPERDRONE_SPRITE = "sprites_reaperDrone.png";
   public static final String FLAMEDRONE_SPRITE = "sprites_flameDrone.png";
   public static final String WASPDRONE_SPRITE = "sprites_waspDrone.png";
   public static final String KAMIKAZEDRONE_SPRITE = "sprites_kamikazeDrone.png";
   public static final String SMALL_ASTEROID_SPRITE = "sprites_smallAsteroid.png";
   public static final String BIG_ASTEROID_SPRITE = "sprites_bigAsteroid.png";
   public static final String BURNING_FRAGMENT_SPRITE = "sprites_burningFragment.png";
   public static final String FLAME_PROJECTILE = "sprites_droneFlame.png";
   public static final String SHIP_SMOKE_POINT = "ship_smoke_point.png";
   public static final String BLUE_GLOW_SMALL = "glow_blue_small.png";
   public static final String GREEN_GLOW_SMALL = "glow_green_small.png";
   public static final String ORANGE_GLOW_BIG = "glow_orange_big.png";
   public static final String REAPER_GLOW = "glow_reaper.png";
   public static final String WHITE_GLOW_BIG = "glow_white_big.png";

   // Sprites - BossMode
   public static final String ROTATING_LAZER_SPRITE = "rotatingLazer.png";
   public static final String LAZER_CHARGE_SPRITE1 = "lazerCharge.png";
   public static final String LAZER_CHARGE_SPRITE2 = "lazerCharge2.png";
   public static final String ENERGY_BALL_SPRITE = "energyBall.png";
   public static final String HEATSEEKING_LAZER_SPRITE = "heatSeekingLazer.png";
   public static final String MACHINE_HEART_SPRITE = "machineHeart.png";
   public static final String BOSS_PROJECTILE1 = "projectile_boss1.png";
   public static final String EMPTY_IMAGE = "nonImg.png"; // An empty image

   // Sprites - LevelEditor
   public static final String SMALL_ENTITY_SPRITES = "sprites_all.png";

   public Images() {
      this.myImages = new ResourceContainer<>(s -> ResourceLoader.getImage(s));
      this.myCollisionImages = new ResourceContainer<>(s -> ResourceLoader.getCollisionImage(s));
   }

   private MyImage getImage(String imageName, boolean keepInMemory) {
      return myImages.getResource(imageName, keepInMemory);
   }

   private MyCollisionImage getCollisionImage(String imageName) {
      return myCollisionImages.getResource(imageName, false);
   }

   public void flush() {
      myImages.flush();
      myCollisionImages.flush();
   }

   public MyImage getBossSprite(String fileName) {
      fileName = "bossMode/sprites/" + fileName;
      return this.getImage(fileName, false);
   }

   public MyImage getCutsceneImage(String fileName, boolean keepInMemory) {
      fileName = "cutsceneImages/" + fileName;
      return this.getImage(fileName, keepInMemory);
   }

   public MyImage getBossBackground(String fileName) {
      fileName = "bossMode/backgrounds/" + fileName;
      return this.getImage(fileName, false);
   }

   public MyImage getExpImageLandscape(String fileName) {
      fileName = "exploring/images/landscapes/" + fileName;
      return this.getImage(fileName, false);
   }

   public MyImage getExpImageBackground(String fileName) {
      fileName = "exploring/images/backgrounds/" + fileName;
      return this.getImage(fileName, false);
   }

   public MyImage getFlyImageBackground(String fileName) {
      fileName = "flying/images/backgrounds/" + fileName;
      return this.getImage(fileName, false);
   }

   public MyImage getFlyImageForeground(String fileName) {
      fileName = "flying/images/collision/" + fileName;
      return this.getImage(fileName, false);
   }

   public MyCollisionImage getExpImageCollision(String fileName) {
      fileName = "exploring/images/collision/" + fileName;
      return this.getCollisionImage(fileName);
   }

   public MyCollisionImage getFlyImageCollision(String fileName) {
      fileName = "flying/images/collision/" + fileName;
      return this.getCollisionImage(fileName);
   }

   public MyCollisionImage getBossCollisionImg(String fileName) {
      fileName = "bossMode/collision/" + fileName;
      return this.getCollisionImage(fileName);
   }

   public MyImage getExpImageForeground(String fileName) {
      fileName = "exploring/images/foregrounds/" + fileName;
      return this.getImage(fileName, false);
   }

   public MyImage getMiscImage(String fileName, boolean keepInMemory) {
      fileName = "miscImages/" + fileName;
      return this.getImage(fileName, keepInMemory);
   }

   public MyImage getExpImageSprite(String fileName, boolean keepInMemory) {
      fileName = "exploring/images/sprites/" + fileName;
      return this.getImage(fileName, keepInMemory);
   }

   public MyImage getFlyImageSprite(String fileName, boolean keepInMemory) {
      fileName = "flying/images/sprites/" + fileName;
      return this.getImage(fileName, keepInMemory);
   }

}
