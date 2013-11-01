package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

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
		
		resources.add(Resource.GOLD, 6);
		
		name = "Zombie";
	}
	
	@Override
	public void onDeath()
	{
		super.onDeath();
		if (Math.random() <= 0.5)
		{
			boolean left = Math.random() < 0.5;
			Game.world.addEntity(new Zombie(left ? 0 : Game.getWidth(), Game.world.height / 2 - Tile.SIZE));
		}
		Game.currentGame.resources.add(resources);
	}
	
	@Override
	public Entity clone()
	{
		return new Zombie((int) x, (int) y);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick)
	{
		return false;
	}
}
