package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
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
}
