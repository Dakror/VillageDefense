package de.dakror.villagedefense.game.entity.struct;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.ui.CountButton;
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
		
		guiSize = new Dimension(250, 250);
		
		name = "Marktplatz [wip]";
		
		components.add(new CountButton(125, 60, 115, 0, 99, 1, 0));
		// buildingCosts.set(Resource.GOLD, 200);
		// buildingCosts.set(Resource.WOOD, 80);
		// buildingCosts.set(Resource.STONE, 150);
		// buildingCosts.set(Resource.PEOPLE, 1);
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{
		Assistant.drawContainer(guiPoint.x - 125, guiPoint.y - 125, 250, 250, false, false, g);
		
		drawComponents(guiPoint.x - 125, guiPoint.y - 125, g);
	}
	
	@Override
	protected void onMinedUp()
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
