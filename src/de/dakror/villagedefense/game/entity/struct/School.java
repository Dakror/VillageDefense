package de.dakror.villagedefense.game.entity.struct;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
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
		setBump(new Rectangle2D.Float(0, 4, 6, 4));
		guiSize = new Dimension(250, 250);
		buildingCosts.set(Resource.GOLD, 1000);
		buildingCosts.set(Resource.STONE, 400);
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
	{
		if (components.size() == 0) initGUI();
		
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
	
	@Override
	public void initGUI()
	{
		int width = guiSize.width - 20;
		
		int size = 32;
		int gap = 24;
		
		int proRow = width / (size + gap);
		
		for (Researches research : Researches.values())
		{
			components.add(new ResearchButton(20 + ((research.ordinal() % proRow) * (size + gap)), 55 + ((research.ordinal() / proRow) * (size + gap)), research, Game.currentGame.researches, false));
		}
	}
}
