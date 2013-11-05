package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.world.Tile;
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
		// attributes.set(Attribute.HEALTH, 25);
		// attributes.set(Attribute.HEALTH_MAX, 25);
		
		buildingCosts.set(Resource.GOLD, 50);
		buildingCosts.set(Resource.WOOD, 10);
	}
	
	@Override
	protected void onDeath()
	{
		dead = true;
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		Game.world.addEntity(new Villager((int) x + 2 * Tile.SIZE, (int) y + 4 * Tile.SIZE));
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public Entity clone()
	{
		return new House((int) x, (int) y);
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{}
}
