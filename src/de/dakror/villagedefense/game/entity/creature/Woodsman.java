package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.Tree;
import de.dakror.villagedefense.settings.Attributes.Attribute;

/**
 * @author Dakror
 */
public class Woodsman extends Creature
{
	public Woodsman(int x, int y)
	{
		super(x, y, "forester");
		setHostile(false);
		name = "Holzfäller";
		attributes.set(Attribute.MINE_AMOUNT, 2);
		attributes.set(Attribute.MINE_SPEED, 15);
		
		description = "Fällt rasant Bäume.";
	}
	
	@Override
	public void lookupTargetEntity()
	{
		Entity nearest = null;
		for (Entity e : Game.world.entities)
		{
			if (e instanceof Tree && ((Tree) e).isGrown())
			{
				if (nearest == null || e.getCenter2().getDistance(getCenter2()) < nearest.getCenter2().getDistance(getCenter2())) nearest = e;
			}
		}
		
		if (nearest != null) setTarget(nearest);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick)
	{
		if (targetEntity instanceof Tree)
		{
			if ((tick + randomOffset) % attributes.get(Attribute.MINE_SPEED) == 0 && targetEntity.getResources().size() > 0)
			{
				if (frame % 2 == 0) ((Struct) targetEntity).mineAllResources((int) attributes.get(Attribute.MINE_AMOUNT));
				frame++;
			}
			
			return true;
		}
		return false;
	}
	
	@Override
	public Entity clone()
	{
		return new Woodsman((int) x, (int) y);
	}
}
