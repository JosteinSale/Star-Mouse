package data_storage;

/**
 * Contains 3 progressValue-objects, which each represent a save file.
 * This entire object will be saved into JSON-format every time
 * the game is saved.
 */
public class SaveData {
   /*
    * NOTE: progValues must be public, as the DataStorage-machinery will look for
    * public variables with these names, or getters and setters for variables
    * with these names.
    */

   // SAVE 1
   public ProgressValues progValues1;

   // SAVE 2
   public ProgressValues progValues2;

   // SAVE 3
   public ProgressValues progValues3;

   // OBS: It's important to include an empty constructor, that can serve as a
   // default
   // constructor when the data is read.
   public SaveData() {
   }

   public SaveData(ProgressValues progValues1, ProgressValues progValues2, ProgressValues progValues3) {
      this.progValues1 = progValues1;
      this.progValues2 = progValues2;
      this.progValues3 = progValues3;
   }

   public ProgressValues getProgValuesFor(int saveNr) {
      switch (saveNr) {
         case 1:
            return progValues1;
         case 2:
            return progValues2;
         case 3:
            return progValues3;
         default:
            throw new IllegalArgumentException("No progressValues for saveNr " + saveNr);
      }
   }
}
