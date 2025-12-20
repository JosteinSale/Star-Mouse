package rendering.flying;

import java.util.Collection;
import java.util.HashMap;

import entities.flying.EnemyFactory;
import entities.flying.EntityInfo;
import entities.flying.pickupItems.PickupItemFactory;
import rendering.MyImage;
import rendering.MySubImage;
import utils.HelpMethods2;
import utils.Images;

/**
 * Loads all images for PickupItems and Enemies, constructs animation arrays,
 * and provides a get-method for getting a subImage in an animation.
 */
public class EntityImages {
   private HashMap<Integer, MySubImage[][]> animations;

   public EntityImages(EnemyFactory enemyFactory, PickupItemFactory pickupFactory, Images images) {
      this.animations = new HashMap<>();
      this.addImagesFor(pickupFactory.pickupInfo.values(), images);
      this.addImagesFor(enemyFactory.enemyInfo.values(), images);
   }

   private void addImagesFor(Collection<EntityInfo> c, Images images) {
      for (EntityInfo info : c) {
         MyImage spriteSheet = images.getFlyImageSprite(info.spriteSheet, true);
         MySubImage[][] animation = HelpMethods2.GetEnemyAnimations(
               spriteSheet, info.spriteW, info.spriteH, info.rows, info.cols);
         animations.put(info.typeConstant, animation);
      }
   }

   public MySubImage getImageFor(int enemyType, int aniAction, int aniIndex) {
      return animations.get(enemyType)[aniAction][aniIndex];
   }
}
