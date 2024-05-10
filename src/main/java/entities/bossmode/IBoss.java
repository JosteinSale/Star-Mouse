package entities.bossmode;

import java.awt.Graphics;
import java.util.ArrayList;

public interface IBoss {

   public void draw(Graphics g);

   public void update();

   /** Can be called from bossParts that take the Boss as an argument. */
   public void takeDamage(int damage);

   /** Needed for ProjectileHandler */
   public int getXPos();

   /** Needed for ProjectileHandler */
   public int getYPos();

   /** Needed for player.setBossParts() */
   public ArrayList<IBossPart> getBossParts();
}
