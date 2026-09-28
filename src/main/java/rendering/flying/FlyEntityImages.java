package rendering.flying;

import java.util.Collection;
import java.util.HashMap;

import entities.flying.EnemyFactory;
import entities.flying.FlyEntityInfo;
import entities.flying.pickupItems.PickupItemFactory;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.misc.SpriteInfo;
import utils.HelpMethods;
import utils.Images;

/**
 * Loads all images for PickupItems and Enemies, constructs animation arrays,
 * and provides a get-method for getting a subImage in an animation.
 */
public class FlyEntityImages {
   private HashMap<Integer, MySubImage[][]> animations;

   public FlyEntityImages(EnemyFactory enemyFactory, PickupItemFactory pickupFactory, Images images) {
      this.animations = new HashMap<>();
      this.addImagesFor(pickupFactory.pickupInfo.values(), images);
      this.addImagesFor(enemyFactory.enemyInfo.values(), images);
   }

   private void addImagesFor(Collection<FlyEntityInfo> c, Images images) {
      for (FlyEntityInfo info : c) {
         SpriteInfo spriteInfo = info.spriteInfo;
         MyImage spriteSheet = images.getFlyImageSprite(spriteInfo.spriteName, true);
         MySubImage[][] animation = HelpMethods.GetUnscaled2DAnimationArray(
               spriteSheet,
               spriteInfo.rows, spriteInfo.cols,
               spriteInfo.spriteW, spriteInfo.spriteH);
         animations.put(info.typeConstant, animation);
      }
   }

   public MySubImage getImageFor(int enemyType, int row, int col) {
      return animations.get(enemyType)[row][col];
   }
}
