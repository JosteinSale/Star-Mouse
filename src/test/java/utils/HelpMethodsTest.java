package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.Pixmap;
import entities.MyCollisionImage;

public class HelpMethodsTest {

   @Test
   public void isSolidReturnsTrueForSolidPixel() {
      Pixmap pixmap = mock(Pixmap.class);
      MyCollisionImage imgMock = new MyCollisionImage(pixmap);

      int color1 = 0x01000000; // Red is 1 -> should collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color1);
      assertTrue(HelpMethods.IsSolid(0, 0, imgMock));

      int color2 = 0x63000000; // Red is 99 -> should collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color2);
      assertTrue(HelpMethods.IsSolid(0, 0, imgMock));

      int color3 = 0x01FFFFFF; // Other colors don't affect the result.
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color3);
      assertTrue(HelpMethods.IsSolid(0, 0, imgMock));

      // Pixel is outside the image bounds -> should return false
      assertFalse(HelpMethods.IsSolid(-1, 0, imgMock));
   }

   @Test
   public void isSolidReturnsFalseForNonSolidPixel() {
      Pixmap pixmap = mock(Pixmap.class);
      MyCollisionImage imgMock = new MyCollisionImage(pixmap);

      int color3 = 0x64000000; // Red is 100 (just at the threshold) -> should not collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color3);
      assertFalse(HelpMethods.IsSolid(0, 0, imgMock));
   }

   @Test
   public void isSolidReturnsFalseIfPixelIsOutOfBounds() {
      Pixmap pixmap = mock(Pixmap.class);
      MyCollisionImage imgMock = new MyCollisionImage(pixmap);

      assertFalse(HelpMethods.IsSolid(-1, 0, imgMock));
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
      // Simulate a pixmap image with size 30x30 px
      Pixmap pixmap = mock(Pixmap.class);
      MyCollisionImage imgMock = new MyCollisionImage(pixmap);
      when(pixmap.getWidth()).thenReturn(30);
      when(pixmap.getHeight()).thenReturn(30);

      int solidColor = 0x01000000; // Red is 1 -> should collide
      int nonSolidColor = 0x64000000; // Red is 100 -> should not collide

      // Simulate that all pixels are non-solid -> should not collide
      Rectangle2D.Float hitbox1 = new Rectangle2D.Float(
            30, 30, // will be divided by 3
            30, 30); // will be divided by 3
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(nonSolidColor);
      assertFalse(HelpMethods.CollidesWithMap(hitbox1, imgMock));

      // Simulate a collision with a solid pixel in the top left corner
      // Note that we divide the pixel coordinates by 3 to match the in-game scaling
      when(pixmap.getPixel(10, 10)).thenReturn(solidColor);
      assertTrue(HelpMethods.CollidesWithMap(hitbox1, imgMock));

      // Simulate a collision with a solid pixel in the down right corner
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(nonSolidColor);
      when(pixmap.getPixel(20, 20)).thenReturn(solidColor);
      assertTrue(HelpMethods.CollidesWithMap(hitbox1, imgMock));

      // Trying to check a pixel outside of the image bounds should return false
      Rectangle2D.Float hitbox2 = new Rectangle2D.Float(
            50, 50,
            50, 50); // The X2 and Y2 will be 100 (divided by 3 = 33) -> outside the bounds
      assertFalse(HelpMethods.CollidesWithMap(hitbox2, imgMock));
   }
}
