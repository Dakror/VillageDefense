package de.dakror.villagedefense.game.entity.struct;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Resources.Resource;
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
		
		components.add(new CountButton(140, 60, 100, 0, 99, 1, 0)); // Wood
		components.add(new CountButton(140, 100, 100, 0, 99, 1, 0)); // Stone
		// buildingCosts.set(Resource.GOLD, 200);
		// buildingCosts.set(Resource.WOOD, 80);
		// buildingCosts.set(Resource.STONE, 150);
		// buildingCosts.set(Resource.PEOPLE, 1);
	}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		((CountButton) components.get(0)).max = Game.currentGame.resources.get(Resource.WOOD); // Wood button
		((CountButton) components.get(1)).max = Game.currentGame.resources.get(Resource.STONE); // Stone button
	}
	
	@Override
	public void drawGUI(Graphics2D g)
	{
		Assistant.drawContainer(guiPoint.x - 125, guiPoint.y - 125, 250, 250, false, false, g);
		Assistant.drawHorizontallyCenteredString("Verkauf", guiPoint.x - 125, 250, guiPoint.y - 85, g, 40);
		Assistant.drawString(Resource.WOOD.getName(), guiPoint.x - 110, guiPoint.y - 125 + 80, g, 30);
		Assistant.drawResource(Game.currentGame.resources, Resource.GOLD, Resource.WOOD.getGoldValue(), guiPoint.x - 40, guiPoint.y - 125 + 60, 30, 30, g);
		
		Assistant.drawString(Resource.STONE.getName(), guiPoint.x - 110, guiPoint.y - 125 + 120, g, 30);
		Assistant.drawResource(Game.currentGame.resources, Resource.GOLD, Resource.STONE.getGoldValue(), guiPoint.x - 40, guiPoint.y - 125 + 100, 30, 30, g);
		drawComponents(guiPoint.x - 125, guiPoint.y - 125, g);
		
		Assistant.drawString("Gesamt:", guiPoint.x - 110, guiPoint.y + 70, g, 30);
		int sum = ((CountButton) components.get(0)).value * Resource.WOOD.getGoldValue() + ((CountButton) components.get(1)).value * Resource.STONE.getGoldValue();
		Assistant.drawResource(Game.currentGame.resources, Resource.GOLD, sum, guiPoint.x, guiPoint.y + 50, 30, 30, g);
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
