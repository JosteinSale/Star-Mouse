package rendering.flying;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;

import entities.flying.EntityFactory;
import entities.flying.EntityInfo;
import utils.HelpMethods2;
import utils.ResourceLoader;

/**
 * Loads all entity images, constructs animation arrays,
 * and provides a get-method for getting a subImage in an animation.
 */
public class EntityImages {
   private HashMap<Integer, BufferedImage[][]> animations;
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
         BufferedImage spriteSheet = ResourceLoader.getFlyImageSprite(info.spriteSheet);
         BufferedImage[][] animation = HelpMethods2.GetEnemyAnimations(
               spriteSheet, info.spriteW, info.spriteH, info.rows, info.cols);
         animations.put(info.typeConstant, animation);
      }
   }

   public BufferedImage getImageFor(int enemyType, int aniAction, int aniIndex) {
      return animations.get(enemyType)[aniAction][aniIndex];
   }
}
