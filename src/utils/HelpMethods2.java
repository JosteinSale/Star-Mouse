package utils;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.exploring.AutomaticTrigger;
import entities.flying.enemies.BlasterDrone;
import entities.flying.enemies.Drone;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.FlameDrone;
import entities.flying.enemies.OctaDrone;
import entities.flying.enemies.ReaperDrone;
import entities.flying.enemies.SmallShip;
import entities.flying.enemies.TankDrone;
import entities.flying.enemies.Target;
import entities.flying.pickupItems.Bomb;
import entities.flying.pickupItems.PickupItem;
import entities.flying.pickupItems.Powerup;
import entities.flying.pickupItems.Repair;

import static utils.Constants.Flying.TypeConstants.*;
import static utils.HelpMethods.CreateHitbox;

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
        else if (type == SMALL_SHIP) {
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

    
}
