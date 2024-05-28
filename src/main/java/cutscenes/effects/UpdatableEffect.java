package cutscenes.effects;

/** An interface for an updatable cutscene effect. 
 * OBS: they advance the cutscene after they are finished 
 * (except the NumberDisplayEffect) */
public interface UpdatableEffect extends CutsceneEffect {

   /** Update the effect if it's active. 
    * OBS: once the specified conditions are reached, the effect should be set
    * inactive in this method. */
   public void update();

   /** Returns true if the effect is active */
   public boolean isActive();

   /** Returns true if the cutsceneManager should call the advance-method. */
   public boolean shouldAdvance();
}
