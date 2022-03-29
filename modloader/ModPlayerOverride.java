package modloader;

import java.io.Serializable;

import game.Player;

// This class allows you to make the player update uniquely when playing with your mod
public abstract class ModPlayerOverride implements Cloneable, Serializable {
   // Uniquely update the player with this mod
   public abstract void update(Player player);

   // Uniquely animate the player with this mod
   public void animate(Player player) {
   }

   // Uniquely force animate the player with this mod
   public void forceAnimate(Player player) {
   }

   public ModPlayerOverride copy() {
      try {
         return (ModPlayerOverride) this.clone();
      } catch (CloneNotSupportedException e) {
      }
      return null;
   }
}