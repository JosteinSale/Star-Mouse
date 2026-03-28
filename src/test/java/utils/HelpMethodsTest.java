package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.badlogic.gdx.graphics.Pixmap;
import cutscenes.Cutscene;
import cutscenes.CutscenesForEntity;
import entities.MyCollisionImage;

public class HelpMethodsTest {

   @Test
   public void testIsSolid() {
      Pixmap pixmap = mock(Pixmap.class);
      MyCollisionImage imgMock = new MyCollisionImage(pixmap);

      int color1 = 0x01000000; // Red is 1 -> should collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color1);
      assertTrue(HelpMethods.IsSolid(0, 0, imgMock));

      int color2 = 0x63000000; // Red is 99 -> should collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color2);
      assertTrue(HelpMethods.IsSolid(0, 0, imgMock));

      int color3 = 0x64000000; // Red is 100 (just at the threshold) -> should not collide
      when(pixmap.getPixel(anyInt(), anyInt())).thenReturn(color3);
      assertFalse(HelpMethods.IsSolid(0, 0, imgMock));

      int color4 = 0x01FFFFFF; // Other colors don't affect the result.
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
      int solidColor = 0x01000000; // Red is 1 -> should collide
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

   @Test
   public void testParseCutscene() {
      // We construct an input consisting of the first few cutscenes in the game.
      // It containts 4 unique entities (bed, drawer, game, cabinet)
      // The bed has 1 cutscene, the drawer has 3 cutscenes, the game has 1 cutscene
      // and the cabinet has 1 cutscene. Each custcene has 1 or more sequences,
      // and the sequence have 1 or more events.
      String[] input = {
            "cutscene;true;bed",
            "info;Your bed. Semi-comfortable.",
            "playSFX;infoBox",
            "endSequence",
            "",
            "cutscene;true;drawer",
            "info;A drawer full of clothes. There's a pilot jacket.",
            "playSFX;infoBox",
            "endSequence",
            "infoChoice;Get dressed?;Yes;No",
            "endSequence",
            "",
            "cutscene;true;drawer",
            "info;You got dressed!",
            "playSFX;success",
            "setPlayerSheet;0",
            "setStartCutscene;drawer;3",
            "setRequirementMet;0;0",
            "setStartCutscene;bedroomDoor;1",
            "endSequence",
            "",
            "cutscene;true;drawer",
            "wait;1",
            "endSequence",
            "",
            "cutscene;true;drawer",
            "playSFX;infoBox",
            "info;A drawer now slightly less full of clothes.",
            "endSequence",
            "",
            "cutscene;true;game",
            "playSFX;infoBox",
            "info;Duties first, gaming later.",
            "endSequence",
            "",
            "cutscene;false;cabinet",
            "playSFX;infoBox",
            "info;You open the cabinet and rummage through it.",
            "endSequence",
            "info;You found a present!",
            "playSFX;pickup",
            "endSequence",
            "info;Open your inventory with {pause} to inspect it.",
            "setRequirementMet;0;1",
            "addToInventory;GRADUATION PRESENT;For my brother on his special day. I hope he likes it!;item_present.png",
            "endSequence",
            ""
      };

      HashMap<String, CutscenesForEntity> parsedResult = HelpMethods.ParseCutscenes(Arrays.asList(input));
      assertEquals(4, parsedResult.size());
      assertTrue(parsedResult.containsKey("bed"));
      assertTrue(parsedResult.containsKey("drawer"));
      assertTrue(parsedResult.containsKey("game"));
      assertTrue(parsedResult.containsKey("cabinet"));

      // Bed cutscenes
      CutscenesForEntity bedCutscenes = parsedResult.get("bed");
      assertEquals(1, bedCutscenes.getAllCutscenes().size());
      Cutscene bedCutscene = bedCutscenes.getAllCutscenes().get(0);
      assertEquals(1, bedCutscene.amountOfSequences());
      assertEquals(2, bedCutscene.getSequence(0).events.size());

      // Drawer cutscenes
      CutscenesForEntity drawerCutscenes = parsedResult.get("drawer");
      assertEquals(4, drawerCutscenes.getAllCutscenes().size());

      Cutscene drawerCutscene1 = drawerCutscenes.getAllCutscenes().get(0);
      assertEquals(2, drawerCutscene1.amountOfSequences());
      assertEquals(2, drawerCutscene1.getSequence(0).events.size());
      assertEquals(1, drawerCutscene1.getSequence(1).events.size());
      Cutscene drawerCutscene2 = drawerCutscenes.getAllCutscenes().get(1);
      assertEquals(1, drawerCutscene2.amountOfSequences());
      assertEquals(6, drawerCutscene2.getSequence(0).events.size());
      Cutscene drawerCutscene3 = drawerCutscenes.getAllCutscenes().get(2);
      assertEquals(1, drawerCutscene3.amountOfSequences());
      assertEquals(1, drawerCutscene3.getSequence(0).events.size());
      Cutscene drawerCutscene4 = drawerCutscenes.getAllCutscenes().get(3);
      assertEquals(1, drawerCutscene4.amountOfSequences());
      assertEquals(2, drawerCutscene4.getSequence(0).events.size());

      // Game console cutscenes
      CutscenesForEntity gameCutscenes = parsedResult.get("game");
      assertEquals(1, gameCutscenes.getAllCutscenes().size());
      Cutscene gameCutscene = gameCutscenes.getAllCutscenes().get(0);
      assertEquals(1, gameCutscene.amountOfSequences());
      assertEquals(2, gameCutscene.getSequence(0).events.size());

      // Cabinet cutscenes
      CutscenesForEntity cabinetCutscenes = parsedResult.get("cabinet");
      assertEquals(1, cabinetCutscenes.getAllCutscenes().size());
      Cutscene cabinetCutscene = cabinetCutscenes.getAllCutscenes().get(0);
      assertEquals(3, cabinetCutscene.amountOfSequences());
      assertEquals(2, cabinetCutscene.getSequence(0).events.size());
      assertEquals(2, cabinetCutscene.getSequence(1).events.size());
      assertEquals(3, cabinetCutscene.getSequence(2).events.size());
   }

}
