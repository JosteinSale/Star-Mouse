package rendering.boss_mode;

import java.awt.Graphics;

import entities.bossmode.AnimatedComponent;
import projectiles.shootPatterns.ShootPattern;
import rendering.misc.RenderAnimatedComponent;

public class RenderShootPattern {
   private ShootPattern sp;
   private AnimatedComponent chargeAnimation;
   private AnimatedComponent shootAnimation;
   private RenderAnimatedComponent rCharge;
   private RenderAnimatedComponent rShoot;

   public RenderShootPattern(ShootPattern sp) {
      this.sp = sp;
      this.chargeAnimation = sp.getChargeAnimation();
      this.shootAnimation = sp.getShootAnimation();
      this.rCharge = new RenderAnimatedComponent(chargeAnimation);
      this.rShoot = new RenderAnimatedComponent(shootAnimation);
   }

   /**
    * If the shootpattern is charging, it draws the charging animation.
    * Else if the shootpattern is in the shoot phase, it draws the shoot animation.
    * (Projectiles are drawn by the projectileHandler)
    */
   public void draw(Graphics g) {
      if (sp.isCharging()) {
         rCharge.draw(g);
      } else if (sp.isInShootPhase()) {
         rShoot.draw(g);
      }
   }
}
