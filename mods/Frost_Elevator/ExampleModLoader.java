package mods.Frost_Elevator;

import modloader.ModReader;

public class ExampleModLoader {
   // use this method for all characters you want to add to the character select
   // screen, the file name of this can be whatever you want
   // however it is preferred you call it <ModName>Loader
   static {
      ModReader.addModCharacter(new FrostElevator());

      ModReader.addModStage(new Mountains());
   }
}