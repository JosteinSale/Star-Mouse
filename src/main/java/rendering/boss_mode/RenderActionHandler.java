package rendering.boss_mode;

import java.awt.Graphics;

import entities.bossmode.BossActionHandler;
import entities.bossmode.DefaultBossPart;
import projectiles.shootPatterns.ShootPattern;
import java.util.ArrayList;

public class RenderActionHandler {

   // TODO - The structure in this object is a bit ugly. Maybe improve it.
   // OBS : Remember that many of the entries in the lists will be empty.

   private ArrayList<ArrayList<RenderBossPart>> bpAnimations;
   private ArrayList<ArrayList<RenderShootPattern>> spAnimations;
   // The 2D-ArrayLists reflects how BossParts are structured in the ActionHandler.

   public RenderActionHandler(BossActionHandler actionHandler) {
      this.loadBossPartRenders(actionHandler);
      this.loadShootPatternRenders(actionHandler);
   }

   private void loadBossPartRenders(BossActionHandler actionHandler) {
      this.bpAnimations = new ArrayList<>();
      // 1. The outer arrayList represents the different boss actions
      for (ArrayList<DefaultBossPart> action : actionHandler.getAllBossParts()) {
         ArrayList<RenderBossPart> renders = new ArrayList<>();
         this.bpAnimations.add(renders);

         // 2. Each boss action can have multiple bossParts associated with it
         // (but usually just 1).
         for (DefaultBossPart bp : action) {
            renders.add(new RenderBossPart(bp)); // The render loads images
         }
      }
   }

   private void loadShootPatternRenders(BossActionHandler actionHandler) {
      this.spAnimations = new ArrayList<>();
      // 1 - The outer arrayList represents the different boss actions
      for (ArrayList<ShootPattern> action : actionHandler.getAllShootPatterns()) {
         ArrayList<RenderShootPattern> renders = new ArrayList<>();
         this.spAnimations.add(renders);

         // 2. Each boss action can have multiple shootPatterns associated with it
         // (e.g. two - if the boss has two cannons).
         for (ShootPattern sp : action) {
            renders.add(new RenderShootPattern(sp));
         }
      }
   }

   /**
    * Only visible bossParts and active shootPatterns are drawn.
    */
   public void draw(
         Graphics g, int index) {
      // For bossparts we loop through all actions since some may be visible,
      // even if inactive.
      for (ArrayList<RenderBossPart> partList : bpAnimations) {
         for (RenderBossPart part : partList) {
            part.draw(g);
         }
      }
      for (RenderShootPattern pattern : spAnimations.get(index)) {
         pattern.draw(g);
      }
   }

}
