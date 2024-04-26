package entities.bossmode;

import java.awt.Graphics;
import java.util.ArrayList;

public interface IBoss {

   public void draw(Graphics g);

   public void update();

   /** Needed for player.setBossParts() */
   public ArrayList<IBossPart> getBossParts();
}
