package projectiles;

import entities.CollisionPixels;
import entities.Dimensions;

import static entities.CollisionPixels.CollisionAt;

import static projectiles.ProjectileFactory.TypeConstants.OCTA_PROJECTILE;

import java.awt.geom.Rectangle2D;

public class OctaProjectile extends BaseProjectile {

    public OctaProjectile(Dimensions dimensions, int xSpeed, int ySpeed) {
        super(dimensions, OCTA_PROJECTILE, 10, xSpeed, ySpeed);
        this.collisionPixels = new CollisionPixels(this, CollisionAt.CENTER);
    }
}
