package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Resources.Resource;

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
		resources.set(Resource.STONE, 50);
		name = "Stein";
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
	protected void onMinedUp()
	{
		onDeath();
	}
	
	@Override
	public Entity clone()
	{
		return new Rock((int) x, (int) y);
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{}
}
