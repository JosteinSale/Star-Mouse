file:///C:/Users/joste/StarFox%202D%20Shooter/StarMouse/src/main/java/rendering/root_renders/RenderOptionsMenu.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1492
uri: file:///C:/Users/joste/StarFox%202D%20Shooter/StarMouse/src/main/java/rendering/root_renders/RenderOptionsMenu.java
text:
```scala
package rendering.root_renders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main_classes.Game;
import rendering.SwingRender;
import rendering.misc.RenderControlsMenu;
import ui.ControlsMenu;
import ui.OptionsMenu;
import utils.ResourceLoader;

import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;
import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.SLIDER_HEIGHT;
import static utils.Constants.UI.SLIDER_WIDTH;

public class RenderOptionsMenu implements SwingRender {
      private OptionsMenu optionsMenu;
      private ControlsMenu controlsMenu;
      private RenderControlsMenu rControlsMenu;
      private Color bgColor = new Color(0, 0, 0, 230);
      private Font headerFont;
      private Font menuFont;
      private BufferedImage pointerImg;
      private BufferedImage sliderImg;

      private int bgW;
      private int bgH;
      private int bgX;
      private int bgY;
      private int optionsX = 250;
      private int optionsY = 330;

      public RenderOptionsMenu(OptionsMenu optionsMenu, ControlsMenu controlsMenu) {
            this.optionsMenu = optionsMenu;
            this.controlsMenu = controlsMenu;
            this.rControlsMenu = new RenderControlsMenu(controlsMenu);
            this.loadImages();@@
            this.loadFonts();
            this.calcDrawValues();
      }

      private void loadImages() {
            this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
            this.sliderImg = ResourceLoader.getExpImageSprite(ResourceLoader.SLIDER_SPRITE);
      }

      private void loadFonts() {
            this.headerFont = ResourceLoader.getHeaderFont();
            this.menuFont = ResourceLoader.getMenuFont();
      }

      private void calcDrawValues() {
            bgW = OPTIONS_WIDTH;
            bgH = OPTIONS_HEIGHT;
            bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
            bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
      }

      @Override
      public void draw(Graphics g) {
            if (controlsMenu.isActive()) {
                  rControlsMenu.draw(g);
                  return;
            }

            Graphics2D g2 = (Graphics2D) g;
            // Background
            g.setColor(bgColor);
            g.fillRect(
                        (int) (bgX * Game.SCALE), (int) (bgY * Game.SCALE),
                        (int) (bgW * Game.SCALE), (int) (bgH * Game.SCALE));

            g2.setColor(Color.WHITE);
            g2.drawRect(
                        (int) ((bgX + 10) * Game.SCALE), (int) ((bgY + 10) * Game.SCALE),
                        (int) ((bgW - 20) * Game.SCALE), (int) ((bgH - 20) * Game.SCALE));

            // Text
            g.setFont(headerFont);
            g.setColor(Color.WHITE);
            g.drawString("OPTIONS", (int) (420 * Game.SCALE), (int) (150 * Game.SCALE));

            g.setFont(menuFont);
            for (int i = 0; i < optionsMenu.menuOptions.length; i++) {
                  g.drawString(
                              optionsMenu.menuOptions[i],
                              (int) (optionsX * Game.SCALE),
                              (int) ((optionsY + i * optionsMenu.menuOptionsDiff) * Game.SCALE));
            }

            // Sliders
            g.fillRect(
                        (int) (550 * Game.SCALE), (int) (318 * Game.SCALE),
                        (int) (optionsMenu.sliderBarWidth * Game.SCALE), (int) (5 * Game.SCALE));
            g.fillRect(
                        (int) (550 * Game.SCALE), (int) (390 * Game.SCALE),
                        (int) (optionsMenu.sliderBarWidth * Game.SCALE), (int) (5 * Game.SCALE));
            g.drawImage(sliderImg,
                        (int) (optionsMenu.musicSliderX * Game.SCALE), (int) (295 * Game.SCALE),
                        (int) (SLIDER_WIDTH * Game.SCALE), (int) (SLIDER_HEIGHT * Game.SCALE), null);
            g.drawImage(sliderImg,
                        (int) (optionsMenu.sfxSliderX * Game.SCALE), (int) (370 * Game.SCALE),
                        (int) (SLIDER_WIDTH * Game.SCALE), (int) (SLIDER_HEIGHT * Game.SCALE), null);

            // Cursor
            g2.drawImage(
                        pointerImg,
                        (int) (optionsMenu.cursorX * Game.SCALE), (int) ((optionsMenu.cursorY - 30) * Game.SCALE),
                        (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
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
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:376)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator