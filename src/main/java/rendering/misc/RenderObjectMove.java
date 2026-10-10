package rendering.misc;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.animation.PositionedAnimation;
import rendering.MySubImage;
import rendering.Render;
import utils.HelpMethods;
import utils.Images;
import utils.SpriteInfo;

/**
 * Renders simple animations using the numerical SimpleAnimation-objects
 * and their corresponding sprite-arrays.
 * 
 * Loading of images and construction of objects is done upon demand.
 * They should also be cleared after each use.
 * 
 * Call addAnimation() and clear() to do this.
 */
public class RenderObjectMove implements Render {
   private Images images;
   private ArrayList<PositionedAnimation> animations;
   private ArrayList<MySubImage[]> spriteArrays;

   public RenderObjectMove(Images images) {
      this.images = images;
      this.animations = new ArrayList<>();
      this.spriteArrays = new ArrayList<>();
   }

   public void addAnimation(String spriteName, PositionedAnimation animation) {
      spriteName += ".png";
      MySubImage[] animationArray = getAnimationArray(spriteName);
      this.animations.add(animation);
      this.spriteArrays.add(animationArray);
   }

   private MySubImage[] getAnimationArray(String spriteName) {
      return HelpMethods.GetUnscaled1DAnimationArray(
            images.getCutsceneImage(spriteName, false),
            SpriteInfo.get(spriteName));
   }

   @Override
   public void draw(SpriteBatch sb) {
      int index = 0;
      for (PositionedAnimation sa : animations) {
         RenderPositionedAnimation.draw(sb, sa, spriteArrays.get(index));
         index++;
      }
   }

   public void clear() {
      this.animations.clear();
      this.spriteArrays.clear();
   }
}
