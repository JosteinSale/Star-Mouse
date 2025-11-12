package rendering.boss_mode;

import entities.bossmode.BossActionHandler;
import entities.bossmode.DefaultBossPart;
import projectiles.shootPatterns.ShootPattern;
import utils.Images;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderActionHandler {
   /*
    * Note: due to the fact that we use HashMap, the order of actions is not
    * preserved. This could result in bossParts being drawn on top of each other in
    * an unintended order. However, we can fix this later if it becomes an issue.
    */
   private Images images;
   private HashMap<String, ArrayList<RenderBossPart>> bpAnimations;
   private HashMap<String, ArrayList<RenderShootPattern>> spAnimations;

   public RenderActionHandler(BossActionHandler actionHandler, Images images) {
      this.images = images;
      this.loadBossPartRenders(actionHandler, images);
      this.loadShootPatternRenders(actionHandler);
   }

   private void loadBossPartRenders(BossActionHandler actionHandler, Images images) {
      this.bpAnimations = new HashMap<>();
      // 1. Loop through each action, and make a new arrayList for it
      for (String action : actionHandler.getAllBossParts().keySet()) {
         this.bpAnimations.put(action, new ArrayList<RenderBossPart>());

         // 2. Loop through each bossPart associated with that action
         // and make a render for it
         for (DefaultBossPart bp : actionHandler.getAllBossParts().get(action)) {
            this.bpAnimations.get(action).add(new RenderBossPart(bp, images));
         }
      }
   }

   private void loadShootPatternRenders(BossActionHandler actionHandler) {
      this.spAnimations = new HashMap<>();
      // 1. Loop through each action, and make a new arrayList for it
      for (String action : actionHandler.getAllShootPatterns().keySet()) {
         this.spAnimations.put(action, new ArrayList<RenderShootPattern>());

         // 2. Loop through each shootPattern associated with that action
         // and make a render for it
         for (ShootPattern sp : actionHandler.getAllShootPatterns().get(action)) {
            this.spAnimations.get(action).add(new RenderShootPattern(sp, images));
         }
      }
   }

   /**
    * Only visible bossParts and active shootPatterns are drawn.
    */
   public void draw(SpriteBatch sb, String currentAction) {
      // For bossparts we loop through all actions since some may be visible,
      // even if inactive.
      for (ArrayList<RenderBossPart> partList : bpAnimations.values()) {
         for (RenderBossPart part : partList) {
            part.draw(sb);
         }
      }
      for (RenderShootPattern pattern : spAnimations.get(currentAction)) {
         pattern.draw(sb);
      }
   }

}
