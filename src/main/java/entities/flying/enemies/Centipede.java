package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;

public class Centipede extends BaseEnemy {

   public Centipede(Rectangle2D.Float hitbox, EntityInfo info, int startTimer) {
      super(hitbox, info, startTimer, null);
   }
}
