package entities.flying;

import java.awt.geom.Rectangle2D;

/** An interface for a player that can interact with a ProjectileHandler */
public interface ShootingPlayer {

   public Rectangle2D.Float getHitbox();

   public void setBombs(int nrOfBombs);

   public void takeShootDamage(int damage);

   public void setMaxHp(int hp);
}
