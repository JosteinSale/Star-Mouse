package rendering.flying;

import java.util.Collection;
import java.util.HashMap;

import entities.flying.EntityFactory;
import entities.flying.EntityInfo;
import rendering.MyImage;
import rendering.MySubImage;
import utils.HelpMethods2;
import utils.ResourceLoader;

/**
 * Loads all entity images, constructs animation arrays,
 * and provides a get-method for getting a subImage in an animation.
 */
public class EntityImages {
   private HashMap<Integer, MySubImage[][]> animations;
   private EntityFactory entityFactory;

   public EntityImages(EntityFactory entityFactory) {
      this.entityFactory = entityFactory;
      this.animations = new HashMap<>();
      this.addImagesFor(entityFactory.pickupInfo.keySet()); // Must be first
      this.addImagesFor(entityFactory.enemyInfo.keySet()); // Must be second
   }

   private void addImagesFor(Collection<String> c) {
      for (String entityName : c) {
         EntityInfo info = entityFactory.getEntityInfo(entityName);
         MyImage spriteSheet = ResourceLoader.getFlyImageSprite(info.spriteSheet);
         MySubImage[][] animation = HelpMethods2.GetEnemyAnimations(
               spriteSheet, info.spriteW, info.spriteH, info.rows, info.cols);
         animations.put(info.typeConstant, animation);
      }
   }

   public MySubImage getImageFor(int enemyType, int aniAction, int aniIndex) {
      return animations.get(enemyType)[aniAction][aniIndex];
   }
}
