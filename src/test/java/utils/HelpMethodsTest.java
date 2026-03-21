package utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.Pixmap;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import entities.MyCollisionImage;

public class HelpMethodsTest {

   @Test
   public void testIsSolid() {
      Pixmap pixmap = mock(Pixmap.class);
      MyCollisionImage imgMock = new MyCollisionImage(pixmap);

      int color1 = 0x00000000; // Red is 0 -> should collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color1);
      assertTrue(HelpMethods.IsSolid(0, 0, imgMock));

      int color2 = 0x63000000; // Red is 99 -> should collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color2);
      assertTrue(HelpMethods.IsSolid(0, 0, imgMock));

      int color3 = 0x64000000; // Red is 100 (just at the threshold) -> should not collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color3);
      assertFalse(HelpMethods.IsSolid(0, 0, imgMock));

      int color4 = 0x00FFFFFF; // Other colors don't affect the result.
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color4);
      assertTrue(HelpMethods.IsSolid(0, 0, imgMock));

   }

   @Test
   public void testChopStringIntoLines() {
      String input = "This is a test string that should be chopped into multiple lines based on the specified maximum line width.";
      int maxLineWidth = 20;
      String[] expected = {
            "This is a test",
            "string that should",
            "be chopped into",
            "multiple lines based",
            "on the specified",
            "maximum line width. "
      };
      ArrayList<String> actual = HelpMethods.ChopStringIntoLines(input, maxLineWidth);
      assertEquals(expected.length, actual.size());
      for (int i = 0; i < expected.length; i++) {
         assertEquals(expected[i], actual.get(i));
      }
   }

   @Test
   public void testCollidesWithMap() {
      // Setup
      Pixmap pixmap = mock(Pixmap.class);
      MyCollisionImage imgMock = new MyCollisionImage(pixmap);
      Point pixel = new Point(10, 10);
      int solidColor = 0x00000000; // Red is 0 -> should collide
      int nonSolidColor = 0x64000000; // Red is 100 -> should not collide
      int hbSize = 30;

      // Simulate that all pixels are non-solid -> should not collide
      Rectangle2D.Float hitbox1 = new Rectangle2D.Float(
            0, 0,
            hbSize, hbSize);
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(nonSolidColor);
      assertFalse(HelpMethods.CollidesWithMap(hitbox1, imgMock));

      // Simulate a collision with a solid pixel in the bottom left corner
      // (Note that we scale the pixel coordinates by 3 to match the in-game scaling)
      Rectangle2D.Float hitbox2 = new Rectangle2D.Float(
            pixel.x * 3, pixel.y * 3,
            hbSize, hbSize);
      when(pixmap.getPixel(pixel.x, pixel.y)).thenReturn(solidColor);
      assertTrue(HelpMethods.CollidesWithMap(hitbox2, imgMock));

      // Simulate a collision with a solid pixel in the upper right corner
      Rectangle2D.Float hitbox3 = new Rectangle2D.Float(
            pixel.x * 3, pixel.y * 3,
            hbSize, hbSize);
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(nonSolidColor);
      when(pixmap.getPixel(pixel.x + hbSize / 3, pixel.y + hbSize / 3)).thenReturn(solidColor);
      assertTrue(HelpMethods.CollidesWithMap(hitbox3, imgMock));
   }

}
