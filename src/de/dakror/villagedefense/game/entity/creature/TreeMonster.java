package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

public class TreeMonster extends Creature
{
	
	public TreeMonster(int x, int y)
	{
		super(x, y, "treemonster");
		
		name = "Schatteneiche";
		
		attributes.set(Attribute.SPEED, 1.5f);
		attributes.set(Attribute.DAMAGE_STRUCT, 18);
		attributes.set(Attribute.HEALTH, 100);
		attributes.set(Attribute.HEALTH_MAX, 100);
		attributes.set(Attribute.ATTACK_RANGE, Tile.SIZE);
		
		setHostile(true);
		
		resources.set(Resource.GOLD, 20);
		resources.set(Resource.WOOD, 25);
		
		description = "Ein monsterhafter Baum.";
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick)
	{
		return false;
	}
	
	@Override
	public Entity clone()
	{
		return new TreeMonster((int) x, (int) y);
	}
	
}
