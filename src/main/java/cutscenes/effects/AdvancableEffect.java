package cutscenes.effects;

/** An interface for cutscene effects that can advance the cutscene */
public interface AdvancableEffect {
   
   public boolean shouldAdvance();

   public void reset();
}
