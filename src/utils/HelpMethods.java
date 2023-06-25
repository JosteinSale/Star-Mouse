package utils;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class HelpMethods {
    
    private static boolean IsSolid(
        int pixelX, int pixelY, BufferedImage collisionImg) {
        Color c = new Color(collisionImg.getRGB(pixelX, pixelY));
        if (c.getRed() > 0 && c.getRed() < 100) {
            return true;
        }
        else {
            return false;
        }
    }

    // Sjekker de fire hjørnene til hitboksen
    public static boolean CanWalkHere(Rectangle2D.Float hitbox, float deltaX, float deltaY, BufferedImage collisionImg) {
        float newX1 = (hitbox.x + deltaX) / 3;
        float newY1 = (hitbox.y + deltaY) / 3;
        float newX2 = (hitbox.x + hitbox.width + deltaX) / 3;
        float newY2 = (hitbox.y + hitbox.height + deltaY) / 3;
        if (
            !IsSolid((int)newX1, (int)newY1, collisionImg) && 
            !IsSolid((int)newX2, (int)newY1, collisionImg) &&
            !IsSolid((int)newX1, (int)newY2, collisionImg) && 
            !IsSolid((int)newX2, (int)newY2, collisionImg)) { 
                return true;
            }
        else {
            return false;
        }
    }
}
