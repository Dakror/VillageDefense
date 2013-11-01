package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

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
		attributes.set(Attribute.HEALTH, 15);
		attributes.set(Attribute.HEALTH_MAX, 15);
	}
	
	@Override
	public void onSpawn()
	{
		Game.currentGame.resources.add(Resource.PEOPLE, 1);
	}
	
	@Override
	public void onDeath()
	{
		super.onDeath();
		Game.currentGame.resources.add(Resource.PEOPLE, -1);
	}
	
	@Override
	public Entity clone()
	{
		return new Villager((int) x, (int) y);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick)
	{
		if (targetEntity instanceof Struct)
		{
			if (((Struct) targetEntity).getBuildingCosts().get(Resource.PEOPLE) > 0)
			{
				alpha = 0;
			}
			else if ((tick + randomOffset) % targetEntity.getAttributes().get(Attribute.MINE_SPEED) == 0 && targetEntity.getResources().size() > 0)
			{
				if (frame % 2 == 0) ((Struct) targetEntity).mineAllResources((int) attributes.get(Attribute.MINE_AMOUNT));
				frame++;
			}
			
			return true;
		}
		return false;
	}
}
