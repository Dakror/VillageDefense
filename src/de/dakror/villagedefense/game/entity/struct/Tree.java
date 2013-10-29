package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.entity.Struct;
import de.dakror.villagedefense.game.tile.Tile;

/**
 * @author Dakror
 */
public class Tree extends Struct
{
	public Tree(int x, int y)
	{
		super(x, y, 5, 5);
		tx = 0;
		ty = 0;
		setBump(new Rectangle2D.Float(2, 4.3f, 1, 0.6f));
		placeGround = false;
	}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	protected void onDeath()
	{
		if (ty == 5) return;
		
		// transform into leftovers
		width = Tile.SIZE;
		height = Tile.SIZE;
		tx = 7;
		ty = 5;
		x += 2 * Tile.SIZE;
		y += 4 * Tile.SIZE;
	}
}
