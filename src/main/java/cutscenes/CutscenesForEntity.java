package cutscenes;

import java.util.ArrayList;

/** A simple class which represents all cutscenes for an entity */
public class CutscenesForEntity {
   private ArrayList<Cutscene> cutscenes;

   public CutscenesForEntity() {
      this.cutscenes = new ArrayList<>();
   }

   public void addCutscene(Cutscene cutscene) {
      this.cutscenes.add(cutscene);
   }

   public int numberOfCutscenes() {
      return this.cutscenes.size();
   }

   public ArrayList<Cutscene> getAllCutscenes() {
      return this.cutscenes;
   }
}
