package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Troll extends Creature
{
	public Troll(int x, int y)
	{
		super(x, y, "troll");
		
		name = "Troll";
		setHostile(true);
		attributes.set(Attribute.HEALTH, 250);
		attributes.set(Attribute.HEALTH_MAX, 250);
		attributes.set(Attribute.DAMAGE_STRUCT, 25);
		attributes.set(Attribute.SPEED, 0.6f);
		
		resources.set(Resource.GOLD, 150);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick)
	{
		return false;
	}
	
	@Override
	public Entity clone()
	{
		return new Troll((int) x, (int) y);
	}
}
