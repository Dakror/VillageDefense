package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class House extends Struct
{
	public House(int x, int y)
	{
		super(x, y, 5, 5);
		tx = 0;
		ty = 5;
		setBump(new Rectangle2D.Float(0.25f, 2f, 4.5f, 3f));
		placeGround = true;
		name = "Haus";
		attributes.set(Attribute.HEALTH, 35);
		attributes.set(Attribute.HEALTH_MAX, 35);
		
		buildingCosts.set(Resource.GOLD, 50);
		buildingCosts.set(Resource.WOOD, 10);
	}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	protected void onDeath()
	{
		dead = true;
	}
	
	@Override
	public void onSpawn()
	{
		Game.world.addEntity(new Villager((int) x + 2 * Tile.SIZE, (int) y + 4 * Tile.SIZE));
	}
	
	@Override
	protected void onMinedUp()
	{}
}
