package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Struct;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.tile.Tile;

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
}
