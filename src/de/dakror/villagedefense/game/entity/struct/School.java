package de.dakror.villagedefense.game.entity.struct;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class School extends Struct
{
	public School(int x, int y)
	{
		super(x, y, 6, 8);
		
		name = "Schule";
		tx = 8;
		ty = 0;
		
		buildingCosts.set(Resource.GOLD, 500);
		buildingCosts.set(Resource.STONE, 200);
		buildingCosts.set(Resource.WOOD, 80);
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	public Entity clone()
	{
		return null;
	}
	
	@Override
	protected void onDeath()
	{}
}
