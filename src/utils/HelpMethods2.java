package utils;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.exploring.AutomaticTrigger;
import entities.flying.Bomb;
import entities.flying.Drone;
import entities.flying.Enemy;
import entities.flying.OctaDrone;
import entities.flying.PickupItem;
import entities.flying.Powerup;
import entities.flying.Repair;
import entities.flying.SmallShip;
import entities.flying.Target;
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
            Drone drone = new Drone(hitbox, animations);
            return drone;
        }
        else if (type == SMALL_SHIP) {
            int direction = 1;
            if (lineData[3].equals("left")) {
                direction = -1;
            }
            SmallShip ship = new SmallShip(hitbox, animations, direction);
            return ship;
        }
        else if (type == OCTADRONE) {
            int shootTimer = Integer.parseInt(lineData[3]);
            OctaDrone octaDrone = new OctaDrone(hitbox, animations, shootTimer);
            return octaDrone;
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

    public static PickupItem GetPickupItem(String[] lineData, int type) {
        Rectangle2D.Float hitbox = CreateHitbox(lineData);
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
