package modloader;

import game.character.Character;
import game.Player;

public abstract class PrimitiveModCharacter extends Character {

   public int modIndex;

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