package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.entity.Struct;

/**
 * @author Dakror
 */
public class Rock extends Struct
{
	public Rock(int x, int y)
	{
		super(x, y, 1, 1);
		tx = 7;
		ty = 6;
		setBump(new Rectangle2D.Float(0, 0.5f, 1, 0.5f));
		placeGround = false;
	}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	protected void onDeath()
	{
		dead = true;
	}
}
