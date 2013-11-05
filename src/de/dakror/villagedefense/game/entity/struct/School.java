package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class School extends Struct
{
	public School(int x, int y)
	{
		super(x, y, 6, 8);
		
		name = "Schule [wip]";
		tx = 8;
		ty = 0;
		setBump(new Rectangle2D.Float(0, 4, 6, 4));
		
		buildingCosts.set(Resource.GOLD, 500);
		buildingCosts.set(Resource.STONE, 200);
		buildingCosts.set(Resource.WOOD, 80);
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public Entity clone()
	{
		return new School((int) x, (int) y);
	}
	
	@Override
	protected void onDeath()
	{}
	
	@Override
	public void drawGUI(Graphics2D g)
	{}
}
