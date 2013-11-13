package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Forester;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.entity.creature.Woodsman;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
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
		description = "Bietet Platz für einen Einwohner.";
		buildingCosts.set(Resource.GOLD, 50);
		buildingCosts.set(Resource.WOOD, 10);
	}
	
	@Override
	protected void onDeath()
	{
		dead = true;
		for (Entity e : Game.world.entities)
		{
			if (e instanceof Villager && e.alpha > 0)
			{
				e.kill();
				break;
			}
		}
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		Game.world.addEntity2(new Villager((int) x + 2 * Tile.SIZE, (int) y + 4 * Tile.SIZE));
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
	public void initGUI()
	{}
	
	@Override
	public void onUpgrade(Researches research)
	{
		if (research == Researches.HOUSE_FORESTER)
		{
			tx = 9;
			ty = 13;
			width = 5 * Tile.SIZE;
			height = 10 * Tile.SIZE;
			y -= 5 * Tile.SIZE;
			setBump(new Rectangle2D.Float(0.25f, 7f, 4.5f, 3f));
			image = null;
			Game.world.addEntity2(new Forester((int) x + 2 * Tile.SIZE, (int) y + height - Tile.SIZE));
		}
		else if (research == Researches.HOUSE_WOODSMAN)
		{
			
			Game.world.addEntity2(new Woodsman((int) x + 2 * Tile.SIZE, (int) y + height - Tile.SIZE));
		}
	}
}
