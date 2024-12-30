file:///C:/Users/joste/StarFox%202D%20Shooter/StarMouse/src/main/java/rendering/root_renders/RenderFlying.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file:///C:/Users/joste/StarFox%202D%20Shooter/StarMouse/src/main/java/rendering/root_renders/RenderFlying.java
text:
```scala
package rendering.root_renders;

import java.awt.Graphics;

import entities.flying.EntityInfo;
import entities.flying.pickupItems.PickupItem;
import gamestates.flying.Flying;
import main_classes.Game;
import rendering.SwingRender;
import rendering.flying.RenderMap2;
import rendering.flying.RenderPlayerFly;

public class RenderFlying implements SwingRender {
   private Flying flying;
   private RenderMap2 rMap;
   private RenderPlayerFly rPlayer;

   public RenderFlying(Game game, Flying flying) {
      this.flying = flying;
      this.rMap = new RenderMap2(flying.getMapManager());
      this.rPlayer = new RenderPlayerFly(game, flying.getPlayer());
   }

   @Override
   public void draw(Graphics g) {
      if (!flying.levelFinished) {
         rMap.drawMaps(g);
         drawPickupItems(g);
         // rPlayer.draw(g);
         // this.enemyManager.draw(g);
         // this.projectileHandler.draw(g);
      }
      if (!flying.gameOver) {
         // this.cutsceneManager.draw(g);
      }
      if (flying.gameOver) {
         // gameoverOverlay.draw(g);
      } else if (flying.pause) {
         // pauseOverlay.draw(g);
      } else if (flying.levelFinished) {
         // levelFinishedOverlay.draw(g);
      }
   }

   private void drawPickupItems(Graphics g) {
      for (PickupItem p : flying.pickupItems) {
         // drawHitbox(g, 0, 0);
         EntityInfo info = p.getDrawInfo();
         if (p.isActive()) {
            g.drawImage(
                  info.animation[0][p.getAniIndex()],
                  (int) ((p.getHitbox().x - info.drawOffsetX) * Game.SCALE),
                  (int) ((p.getHitbox().y - info.drawOffsetY) * Game.SCALE),
                  (int) (info.spriteW * 3 * Game.SCALE),
                  (int) (info.spriteH * 3 * Game.SCALE), null);
         }
      }
   }

   /** Loads all resources for the given level */
   public void loadLevel(int lvl, int bgImgHeight) {
      this.rMap.loadNewMap(lvl, bgImgHeight);
   }

   @Override
   public void dispose() {
   }

}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.MetalsDriver.run(MetalsDriver.scala:45)
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:345)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:109)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator