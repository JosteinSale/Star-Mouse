package rendering.boss_mode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.animation.ComplexAnimation;
import projectiles.shoot_patterns.ShootPattern;
import rendering.misc.RenderComplexAnimation;
import utils.Images;

public class RenderShootPattern {
   private ShootPattern sp;
   private ComplexAnimation chargeAnimation;
   private ComplexAnimation shootAnimation;
   private RenderComplexAnimation rCharge;
   private RenderComplexAnimation rShoot;

   public RenderShootPattern(ShootPattern sp, Images images) {
      this.sp = sp;
      this.chargeAnimation = sp.getChargeAnimation();
      this.shootAnimation = sp.getShootAnimation();
      this.rCharge = new RenderComplexAnimation(chargeAnimation, images);
      this.rShoot = new RenderComplexAnimation(shootAnimation, images);
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
