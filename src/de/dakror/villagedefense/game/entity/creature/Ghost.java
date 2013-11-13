package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Ghost extends Creature
{
	public Ghost(int x, int y)
	{
		super(x, y, "ghost");
		
		name = "Geist";
		setHostile(true);
		attributes.set(Attribute.SPEED, 5);
		attributes.set(Attribute.HEALTH, 8);
		attributes.set(Attribute.HEALTH_MAX, 8);
		attributes.set(Attribute.DAMAGE_STRUCT, 10);
		
		resources.add(Resource.GOLD, 2);
		
		description = "Ein Geist.";
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick)
	{
		return false;
	}
	
	@Override
	public Entity clone()
	{
		return new Ghost((int) x, (int) y);
	}
	
	@Override
	public void lookupTargetEntity()
	{
		setTarget(Game.world.core);
	}
}
