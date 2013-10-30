package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.settings.Attributes.Attribute;

/**
 * @author Dakror
 */
public class Zombie extends Creature
{
	public Zombie(int x, int y)
	{
		super(x, y, "zombie");
		
		setHostile(true);
		attributes.set(Attribute.SPEED, 2f);
		attributes.set(Attribute.DAMAGE_STRUCT, 5);
		attributes.set(Attribute.HEALTH, 20);
		attributes.set(Attribute.HEALTH_MAX, 20);
		name = "Zombie";
	}
}
