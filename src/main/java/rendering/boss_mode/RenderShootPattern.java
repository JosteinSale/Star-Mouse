package rendering.boss_mode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.bossmode.AnimatedComponent;
import projectiles.shootPatterns.ShootPattern;
import rendering.misc.RenderAnimatedComponent;
import utils.Images;

public class RenderShootPattern {
   private ShootPattern sp;
   private AnimatedComponent chargeAnimation;
   private AnimatedComponent shootAnimation;
   private RenderAnimatedComponent rCharge;
   private RenderAnimatedComponent rShoot;

   public RenderShootPattern(ShootPattern sp, Images images) {
      this.sp = sp;
      this.chargeAnimation = sp.getChargeAnimation();
      this.shootAnimation = sp.getShootAnimation();
      this.rCharge = new RenderAnimatedComponent(chargeAnimation, images);
      this.rShoot = new RenderAnimatedComponent(shootAnimation, images);
   }

   /**
    * If the shootpattern is charging, it draws the charging animation.
    * Else if the shootpattern is in the shoot phase, it draws the shoot animation.
    * (Projectiles are drawn by the projectileHandler)
    */
   public void draw(SpriteBatch sb) {
      if (sp.isCharging()) {
         rCharge.draw(sb);
      } else if (sp.isInShootPhase()) {
         rShoot.draw(sb);
      }
   }
}
