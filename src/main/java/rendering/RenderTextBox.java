package rendering;

import java.awt.Graphics;

import ui.TextboxManager;

public class RenderTextBox implements SwingRender {
   private TextboxManager textBoxManager;

   public RenderTextBox(TextboxManager textBoxManager) {
      this.textBoxManager = textBoxManager;
   }

   @Override
   public void draw(Graphics g) {

   }

   @Override
   public void dispose() {
   }

}
