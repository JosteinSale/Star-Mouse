package entities.flying;

import entities.MyRectangle;

/** An interface for a player that can interact with a ProjectileHandler */
public interface ShootingPlayer {

   public MyRectangle getHitbox();

   public void setBombs(int nrOfBombs);

   public void takeShootDamage(int damage);

   public void setMaxHp(int hp);

   public void onLazerShoot();
}
