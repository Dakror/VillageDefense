package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.entity.Creature;
import de.dakror.villagedefense.settings.Attributes.Attribute;

/**
 * @author Dakror
 */
public class Villager extends Creature
{
	public Villager(int x, int y)
	{
		super(x, y, "villager" + (int) Math.round(Math.random()));
		setHostile(false);
		attributes.set(Attribute.SPEED, 2f);
		name = "Einwohner";
	}
}
