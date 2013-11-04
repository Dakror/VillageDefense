package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class Marketplace extends Struct
{
	public Marketplace(int x, int y)
	{
		super(x, y, 5, 5);
		tx = 8;
		ty = 8;
		setBump(new Rectangle2D.Float(0, 0, 5, 5));
		
		name = "Marktplatz [wip]";
		
		buildingCosts.set(Resource.GOLD, 200);
		buildingCosts.set(Resource.WOOD, 80);
		buildingCosts.set(Resource.STONE, 150);
		buildingCosts.set(Resource.PEOPLE, 1);
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{
		Assistant.drawContainer(Game.currentGame.mouseDown.x - 125, Game.currentGame.mouseDown.y - 125, 250, 250, false, false, g);
		
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	protected void tick(int tick)
	{}
	
	@Override
	public Entity clone()
	{
		return new Marketplace((int) x, (int) y);
	}
	
	@Override
	protected void onDeath()
	{}
}
