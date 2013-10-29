package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.entity.Creature;
import de.dakror.villagedefense.settings.Attributes.Types;

/**
 * @author Dakror
 */
public class Zombie extends Creature
{
	public Zombie(int x, int y)
	{
		super(x, y, "zombie");
		
		setHostile(true);
		attributes.set(Types.SPEED, 2f);
		attributes.set(Types.DAMAGE_STRUCT, 5);
	}
}