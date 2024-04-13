package entities.bossmode.rudinger1;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.bossmode.IBoss;
import entities.bossmode.IBossPart;
import utils.LoadSave;

public class Rudinger1 implements IBoss {
   private ArrayList<IBossPart> bossParts;
   private boolean rotatingLazerActive = false;
   private boolean idle = true;

   // Indexes for the bossParts in the list.
   private final int VERTICAL_LAZER = 0;
   private final int HORIZONTAL_LAZER = 1;
   private final int HEATSEEKING_LAZER = 2;
   private final int HEART = 3;

   // Timers
   private int idleDuration = 200;
   private int idleTick = 0;
   private int rotatingLazerDuration = 300;
   private int rotatingLazerTick = 0;

   public Rudinger1() {
      bossParts = new ArrayList<>();

      // We start with making two rotating lazers. One vertical and one horizontal.
      BufferedImage lazerImg = LoadSave.getFlyImageSprite(LoadSave.BOSS_TEST);
      RotatingLazer verticalLazer = new RotatingLazer(
         new Rectangle2D.Float(500f, -300f, 30, 1300),
         lazerImg, 1, 3, 30, 1300);
      RotatingLazer horizontalLazer = new RotatingLazer(
         new Rectangle2D.Float(500f, -300f, 30, 1300),
         lazerImg, 1, 3, 30, 1300);
      horizontalLazer.updatePosition(0, 0, Math.PI/2);

      bossParts.add(verticalLazer);
      bossParts.add(horizontalLazer);
   }

   @Override
   public void update() {
      if (idle) {
         updateIdle();
      }
      if (rotatingLazerActive) {
         updateRotatingLazers();
      }
   }

   private void updateIdle() {
      this.idleTick ++;
      if (idleTick > idleDuration) {
         idleTick = 0;
         idle = false;
         rotatingLazerActive = true;
      }
   }

   private void updateRotatingLazers() {
      IBossPart verticalLazer = bossParts.get(VERTICAL_LAZER);
      IBossPart horizontalLazer = bossParts.get(HORIZONTAL_LAZER);

      rotatingLazerTick++;
      if (rotatingLazerTick > rotatingLazerDuration) {
         rotatingLazerTick = 0;
         rotatingLazerActive = false;
         verticalLazer.setActive(false);
         horizontalLazer.setActive(false);
         this.idle = true;
         return;
      }

      verticalLazer.setActive(true);
      verticalLazer.updateBehavior();
      verticalLazer.updateAnimations();

      horizontalLazer.setActive(true);
      horizontalLazer.updateBehavior();
      horizontalLazer.updateAnimations();
   }

   @Override
   public ArrayList<IBossPart> getBossParts() {
      return this.bossParts;
   }

   @Override
   public void draw(Graphics g) {
      if (rotatingLazerActive) {
         bossParts.get(VERTICAL_LAZER).draw(g);
         bossParts.get(HORIZONTAL_LAZER).draw(g);
      }
   }
}
