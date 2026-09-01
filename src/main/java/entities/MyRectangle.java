package entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import utils.PolygonUtils;

import java.awt.geom.Point2D;

/**
 * Represents a rectangle for use in math calculations in the game.
 * Provides methods for simple collision checks, getting and setting positions.
 * Can also be rotated.
 */
public class MyRectangle {
   private final Polygon hitbox;
   protected int hitboxWidth;
   protected int hitboxHeight;
   protected double rotationRadians = 0.0;

   public MyRectangle(float x, float y, int width, int height) {
      this.hitbox = PolygonUtils.newSquareHitboxPolygon(x, y, width, height);
      this.hitboxWidth = width;
      this.hitboxHeight = height;
      this.hitbox.setOrigin(hitboxWidth / 2f, hitboxHeight / 2f);
   }

   public MyRectangle(Dimensions dim) {
      this.hitbox = PolygonUtils.newSquareHitboxPolygon(
            dim.x(), dim.y(),
            dim.width(), dim.height());
      this.hitboxWidth = dim.width();
      this.hitboxHeight = dim.height();
      this.hitbox.setOrigin(hitboxWidth / 2f, hitboxHeight / 2f);
   }

   public void move(float deltaX, float deltaY) {
      hitbox.translate(deltaX, deltaY);
   }

   public void setX(float x) {
      hitbox.setPosition(x, y());
   }

   public void setY(float y) {
      hitbox.setPosition(x(), y);
   }

   public void setPosition(float x, float y) {
      hitbox.setPosition(x, y);
   }

   public float x() {
      return hitbox.getX();
   }

   public float y() {
      return hitbox.getY();
   }

   public int width() {
      return hitboxWidth;
   }

   public int height() {
      return hitboxHeight;
   }

   public float centerX() {
      return hitbox.getX() + hitboxWidth / 2f;
   }

   public float centerY() {
      return hitbox.getY() + hitboxHeight / 2f;
   }

   public double getRotationRadians() {
      return rotationRadians;
   }

   public void rotate(double deltaRadians) {
      this.rotationRadians = (this.rotationRadians + deltaRadians) % (Math.PI * 2);
      hitbox.setRotation((float) (rotationRadians * MathUtils.radiansToDegrees));
   }

   protected void setRotation(double newRotationRadians) {
      this.rotationRadians = newRotationRadians % (Math.PI * 2);
      hitbox.setRotation((float) (rotationRadians * MathUtils.radiansToDegrees));
   }

   public boolean contains(float x, float y) {
      return hitbox.contains(x, y);
   }

   public boolean contains(Point2D point) {
      return hitbox.contains((float) point.getX(), (float) point.getY());
   }

   public boolean intersects(MyRectangle other) {
      return PolygonUtils.polygonsIntersect(this.hitbox, other.hitbox);
   }

   /**
    * Returns the LibGDX implementation of the Polygon.
    * NOTE: only use if direct interaction with the implementation is necessary.
    */
   public Polygon getPolygon() {
      return hitbox;
   }
}
