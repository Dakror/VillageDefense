package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Struct;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class CoreHouse extends Struct
{
	
	public CoreHouse(int x, int y)
	{
		super(x, y, 3, 5);
		tx = 5;
		ty = 0;
		setBump(new Rectangle2D.Float(0.1f, 3, 2.8f, 2));
		structPoints.addAttacks(new Vector(-1, 3.75f), new Vector(3, 3.75f));
		placeGround = true;
	}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	protected void onDeath()
	{
		Game.currentGame.state = 2; // game lost
	}
}