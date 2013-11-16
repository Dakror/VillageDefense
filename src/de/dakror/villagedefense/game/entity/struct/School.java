package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.ui.Component;
import de.dakror.villagedefense.ui.button.ResearchButton;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class School extends Struct
{
	public School(int x, int y)
	{
		super(x, y, 6, 8);
		
		name = "Schule";
		tx = 8;
		ty = 0;
		placeGround = true;
		setBump(new Rectangle2D.Float(0, 4, 6, 4));
		
		buildingCosts.set(Resource.GOLD, 500);
		buildingCosts.set(Resource.STONE, 350);
		buildingCosts.set(Resource.WOOD, 80);
		
		description = "Hier werden neue Technologien erforscht.";
	}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public Entity clone()
	{
		return new School((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath()
	{}
	
	@Override
	public void drawGUI(Graphics2D g)
	{
		if (components.size() == 0) initGUI();
		try
		{
			Assistant.drawContainer(guiPoint.x - 125, guiPoint.y - 125, 250, 250, false, false, g);
			Assistant.drawHorizontallyCenteredString("Wissenschaft", guiPoint.x - 125, 250, guiPoint.y - 85, g, 40);
			
			drawComponents(guiPoint.x - 125, guiPoint.y - 125, g);
			for (Component c : components)
			{
				if ((c instanceof ResearchButton))
				{
					ResearchButton n = (ResearchButton) c;
					if (n.state != 2) continue;
					
					((ResearchButton) c).drawTooltip(Game.currentGame.mouse.x, Game.currentGame.mouse.y, g);
					break;
				}
			}
		}
		catch (NullPointerException e)
		{}
	}
	
	@Override
	public void initGUI()
	{
		int width = guiSize.width - 20;
		
		int size = 32;
		int gap = 24;
		
		int proRow = width / (size + gap);
		
		int i = 0;
		for (Researches research : Researches.values())
		{
			if (research.getCosts(false).size() > 0)
			{
				components.add(new ResearchButton(20 + ((i % proRow) * (size + gap)), 55 + ((i / proRow) * (size + gap)), research, Game.currentGame.researches));
				i++;
			}
		}
	}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial)
	{}
}
