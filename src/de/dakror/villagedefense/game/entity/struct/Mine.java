package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Mine extends Struct
{
	public Mine(int x, int y)
	{
		super(x, y, 2, 3);
		tx = 0;
		ty = 10;
		name = "Mine";
		placeGround = true;
		setBump(new Rectangle2D.Double(0.1f, 2, 1.8f, 1));
		// attributes.set(Attribute.HEALTH, 35);
		// attributes.set(Attribute.HEALTH_MAX, 35);
		attributes.set(Attribute.MINE_SPEED, 40);
		attributes.set(Attribute.MINE_AMOUNT, 2);
		
		buildingCosts.set(Resource.GOLD, 125);
		buildingCosts.set(Resource.WOOD, 65);
		buildingCosts.set(Resource.STONE, 30);
		buildingCosts.set(Resource.PEOPLE, 2);
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	protected void tick(int tick)
	{
		if (tick % attributes.get(Attribute.MINE_SPEED) == 0)
		{
			Game.currentGame.resources.add(Resource.STONE, (int) attributes.get(Attribute.MINE_AMOUNT));
		}
	}
	
	@Override
	public Entity clone()
	{
		return new Mine((int) x, (int) y);
	}
	
	@Override
	protected void onDeath()
	{}
	
	@Override
	public void drawGUI(Graphics2D g)
	{}
}
