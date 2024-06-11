package entities.bossmode;

import java.awt.Graphics;
import java.util.ArrayList;

public interface IBoss {

   public void draw(Graphics g);

   public void update();

   /** Can be called from bossParts that take the Boss as an argument, 
    * as well as the projectileHandler.
    * The overrideInvincibility-boolean should be true in case of bombs.
    * This will make the boss take damage regardless of potential invincibility */
   public void takeDamage(int damage, boolean overrideInvincibility);

   /** Needed for ProjectileHandler */
   public int getXPos();

   /** Needed for ProjectileHandler */
   public int getYPos();

   /** Needed for player and projectileHandler */
   public ArrayList<IBossPart> getBossParts();

   /** Resets the boss's HP, actions and actionIndex */
   public void reset();

   /** Instantly kills the boss, allowing the player to progress to the next segment */
   public void skipBoss();

   /** Sets the visbility-status of the boss. Can be called from the cutsceneManger */
   public void setVisible(boolean visible);
}
