package rendering.root_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import rendering.MyColor;
import rendering.Render;
import rendering.misc.RenderCutscene;
import utils.DrawUtils;

public class RenderCinematic implements Render {
   private RenderCutscene rCutscene;

   public RenderCinematic(RenderCutscene rCutscene) {
      this.rCutscene = rCutscene;
   }

   @Override
   public void draw(SpriteBatch sb) {
      rCutscene.draw(sb);
      this.drawSkipText(sb);
   }

   private void drawSkipText(SpriteBatch sb) {
      DrawUtils.drawText(
            sb, MyColor.GRAY, DrawUtils.infoFont,
            "ENTER to skip", 30, 30);
   }
}
