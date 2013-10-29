package de.dakror.villagedefense.game.entity.struct;

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
