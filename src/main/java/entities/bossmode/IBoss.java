package entities.bossmode;

import java.awt.Graphics;
import java.util.ArrayList;

public interface IBoss {

   public void draw(Graphics g);

   public void update();

   public int getXPos();

   public int getYPos();

   /** Needed for player.setBossParts() */
   public ArrayList<IBossPart> getBossParts();
}
