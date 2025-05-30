package rendering.flying;

import java.util.Collection;
import java.util.HashMap;

import entities.flying.EntityFactory;
import entities.flying.EntityInfo;
import rendering.MyImage;
import rendering.MySubImage;
import utils.HelpMethods2;
import utils.Images;

/**
 * Loads all entity images, constructs animation arrays,
 * and provides a get-method for getting a subImage in an animation.
 */
public class EntityImages {
   private HashMap<Integer, MySubImage[][]> animations;
   private EntityFactory entityFactory;

   public EntityImages(EntityFactory entityFactory, Images images) {
      this.entityFactory = entityFactory;
      this.animations = new HashMap<>();
      this.addImagesFor(entityFactory.pickupInfo.keySet(), images); // Must be first
      this.addImagesFor(entityFactory.enemyInfo.keySet(), images); // Must be second
   }

   private void addImagesFor(Collection<String> c, Images images) {
      for (String entityName : c) {
         EntityInfo info = entityFactory.getEntityInfo(entityName);
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
