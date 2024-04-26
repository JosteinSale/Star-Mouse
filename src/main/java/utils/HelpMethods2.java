package utils;

import static utils.Constants.Flying.DrawOffsetConstants.*;
import static utils.Constants.Flying.SpriteSizes.*;
import static utils.Constants.Flying.TypeConstants.*;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.flying.enemies.BlasterDrone;
import entities.flying.enemies.Drone;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.FlameDrone;
import entities.flying.enemies.KamikazeDrone;
import entities.flying.enemies.OctaDrone;
import entities.flying.enemies.ReaperDrone;
import entities.flying.enemies.SmallShip;
import entities.flying.enemies.TankDrone;
import entities.flying.enemies.Target;
import entities.flying.enemies.WaspDrone;
import entities.flying.pickupItems.Bomb;
import entities.flying.pickupItems.PickupItem;
import entities.flying.pickupItems.Powerup;
import entities.flying.pickupItems.Repair;

public class HelpMethods2 {
    
    public static Enemy GetEnemy(int type, String[] lineData, int width, int height, BufferedImage[][] animations) {
        float x = Float.parseFloat(lineData[1]);
        float y = Float.parseFloat(lineData[2]);
        Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, width, height);
        if (type == TARGET) {
            Target target = new Target(hitbox, animations);
            return target;
        }
        else if (type == DRONE) {
            int shootTimer = Integer.parseInt(lineData[4]);
            Drone drone = new Drone(hitbox, animations, shootTimer);
            return drone;
        }
        else if (type == SMALLSHIP) {
            int dir = Integer.parseInt(lineData[3]);
            SmallShip ship = new SmallShip(hitbox, animations, dir);
            return ship;
        }
        else if (type == OCTADRONE) {
            int shootTimer = Integer.parseInt(lineData[4]);
            OctaDrone octaDrone = new OctaDrone(hitbox, animations, shootTimer);
            return octaDrone;
        }
        else if (type == TANKDRONE) {
            TankDrone tankDrone = new TankDrone(hitbox, animations);
            return tankDrone;
        }
        else if (type == BLASTERDRONE) {
            BlasterDrone blasterDrone = new BlasterDrone(hitbox, animations);
            return blasterDrone;
        }
        else if (type == REAPERDRONE) {
            int shootTimer = Integer.parseInt(lineData[4]);
            ReaperDrone reaperDrone = new ReaperDrone(hitbox, animations, shootTimer);
            return reaperDrone;
        }
        else if (type == FLAMEDRONE) {
            FlameDrone flameDrone = new FlameDrone(hitbox, animations);
            return flameDrone;
        }
        else if (type == WASPDRONE) {
            int dir = Integer.parseInt(lineData[3]);
            int shootTimer = Integer.parseInt(lineData[4]);
            WaspDrone waspDrone = new WaspDrone(hitbox, animations, dir, shootTimer);
            return waspDrone;
        }
        else if (type == KAMIKAZEDRONE) {
            KamikazeDrone kamikazeDrone = new KamikazeDrone(hitbox, animations);
            return kamikazeDrone;
        }
        return null;
    }

    public static BufferedImage[][] GetEnemyAnimations(
        BufferedImage img, int spriteW, int spriteH, int rows, int cols) {
            BufferedImage[][] animations = new BufferedImage[rows][cols];
            for (int j = 0; j < rows; j++) {
                for (int i = 0; i < cols; i++) {
                    animations[j][i] = img.getSubimage(
                        i * spriteW, j * spriteH, spriteW, spriteH);
                }
            }
        return animations;
    }

    public static PickupItem GetPickupItem(String[] lineData, int width, int height, int type) {
        float x = Float.parseFloat(lineData[1]);
        float y = Float.parseFloat(lineData[2]);
        Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, width, height);
        if (type == POWERUP) {
            Powerup powerup = new Powerup(hitbox);
            return powerup;
        }
        else if (type == REPAIR) {
            Repair repair = new Repair(hitbox);
            return repair;
        }
        else {
            Bomb bomb = new Bomb(hitbox);
            return bomb;
        }
    }

    public static int[][] GetEntitySpriteSizes() {
        int width = 0;
        int height = 1;
        int[][] entitySpriteSizes = new int[14][2];

        entitySpriteSizes[DELETE][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[DELETE][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[POWERUP][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[POWERUP][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[REPAIR][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[REPAIR][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[BOMB][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[BOMB][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[TARGET][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[TARGET][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[DRONE][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[DRONE][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[SMALLSHIP][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[SMALLSHIP][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[OCTADRONE][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[OCTADRONE][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[TANKDRONE][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[TANKDRONE][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[BLASTERDRONE][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[BLASTERDRONE][height] = SMALL_SPRITES_SIZE;

        entitySpriteSizes[REAPERDRONE][width] = REAPERDRONE_SPRITE_WIDTH;
        entitySpriteSizes[REAPERDRONE][height] = REAPERDRONE_SPRITE_HEIGHT;

        entitySpriteSizes[FLAMEDRONE][width] = FLAMEDRONE_SPRITE_WIDTH;
        entitySpriteSizes[FLAMEDRONE][height] = FLAMEDRONE_SPRITE_HEIGHT;

        entitySpriteSizes[WASPDRONE][width] = WASPDRONE_SPRITE_SIZE;
        entitySpriteSizes[WASPDRONE][height] = WASPDRONE_SPRITE_SIZE;

        entitySpriteSizes[KAMIKAZEDRONE][width] = SMALL_SPRITES_SIZE;
        entitySpriteSizes[KAMIKAZEDRONE][height] = SMALL_SPRITES_SIZE;

        return entitySpriteSizes;
    }

    public static int[][] GetEntityDrawOffsets() {
        int X = 0;
        int Y = 1;
        int[][] entityDrawOffsets = new int[14][2];

        entityDrawOffsets[DELETE][X] = 0;
        entityDrawOffsets[DELETE][Y] = 0;

        entityDrawOffsets[POWERUP][X] = POWERUP_OFFSET_X;
        entityDrawOffsets[POWERUP][Y] = POWERUP_OFFSET_Y;

        entityDrawOffsets[REPAIR][X] = REPAIR_OFFSET;
        entityDrawOffsets[REPAIR][Y] = REPAIR_OFFSET;

        entityDrawOffsets[BOMB][X] = BOMB_OFFSET_X;
        entityDrawOffsets[BOMB][Y] = BOMB_OFFSET_Y;

        entityDrawOffsets[TARGET][X] = TARGET_OFFSET;
        entityDrawOffsets[TARGET][Y] = TARGET_OFFSET;

        entityDrawOffsets[DRONE][X] = DRONE_OFFSET_X;
        entityDrawOffsets[DRONE][Y] = DRONE_OFFSET_Y;

        entityDrawOffsets[SMALLSHIP][X] = SMALLSHIP_OFFSET_X;
        entityDrawOffsets[SMALLSHIP][Y] = SMALLSHIP_OFFSET_Y;

        entityDrawOffsets[OCTADRONE][X] = OCTADRONE_OFFSET;
        entityDrawOffsets[OCTADRONE][Y] = OCTADRONE_OFFSET;

        entityDrawOffsets[BLASTERDRONE][X] = BLASTERDRONE_OFFSET_X;
        entityDrawOffsets[BLASTERDRONE][Y] = BLASTERDRONE_OFFSET_Y;

        entityDrawOffsets[TANKDRONE][X] = TANKDRONE_OFFSET_X;
        entityDrawOffsets[TANKDRONE][Y] = TANKDRONE_OFFSET_Y;

        entityDrawOffsets[REAPERDRONE][X] = REAPERDRONE_OFFSET_X;
        entityDrawOffsets[REAPERDRONE][Y] = REAPERDRONE_OFFSET_X;

        entityDrawOffsets[FLAMEDRONE][X] = FLAMEDRONE_OFFSET_X;
        entityDrawOffsets[FLAMEDRONE][Y] = FLAMEDRONE_OFFSET_Y;

        entityDrawOffsets[WASPDRONE][X] = WASPDRONE_OFFSET;
        entityDrawOffsets[WASPDRONE][Y] = WASPDRONE_OFFSET;

        entityDrawOffsets[KAMIKAZEDRONE][X] = KAMIKAZEDRONE_OFFSET;
        entityDrawOffsets[KAMIKAZEDRONE][Y] = KAMIKAZEDRONE_OFFSET;

        return entityDrawOffsets;
    }

    /** Returns an unscaled animation array */
    public static BufferedImage[][] GetAnimationArray(
            BufferedImage img, int aniRows, int aniCols, int spriteW, int spriteH) {
        BufferedImage[][] animations = new BufferedImage[aniRows][aniCols];
        for (int r = 0; r < aniRows; r++) {
            for (int c = 0; c < aniCols; c++) {
                animations[r][c] = img.getSubimage(
                        c * spriteW,
                        r * spriteH, spriteW, spriteH);
            }
        }
        return animations;
    }
}
