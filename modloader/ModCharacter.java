package modloader;

import game.Player;
import game.physics.Vector;

public abstract class ModCharacter extends PrimitiveModCharacter {

   public int modIndex;

   public ModCharacter() {
      width = 64;
      height = 64;
      hitboxWidth = 48;
      hitboxHeight = 64;
      jumps = 1;
      doubleJumpDropoff = 0.5;
      weight = 50;
      speed = 1.5;
      drag = 0.9;
      jumpHeight = 40;
      offset = new Vector(8, 0);
      fileName = "marvin.png";
      walkFrameTimer = 2;
      characterName = "Name";
      description = new String[] { "Description",
            "\n",
            "\nDebut: <Game Name>" };
   }

   @Override
   public abstract void neutralAttack(Player player);

   @Override
   public abstract void sideAttack(Player player);

   @Override
   public abstract void upAttack(Player player);

   @Override
   public abstract void downAttack(Player player);

   @Override
   public abstract void chargeAttack(Player player, int charge);
}