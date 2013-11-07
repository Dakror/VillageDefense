package de.dakror.villagedefense.game.entity.struct;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.util.Assistant;

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
		guiSize = new Dimension(250, 250);
		// buildingCosts.set(Resource.GOLD, 500);
		// buildingCosts.set(Resource.STONE, 200);
		// buildingCosts.set(Resource.WOOD, 80);
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
		Assistant.drawContainer(guiPoint.x - 125, guiPoint.y - 125, 250, 250, false, false, g);
		Assistant.drawHorizontallyCenteredString("Wissenschaft", guiPoint.x - 125, 250, guiPoint.y - 85, g, 40);
		
		int width = guiSize.width - 20;
		int height = guiSize.height - 20;
		
		int size = 32;
		int Osize = 48;
		int gap = 8;
		
		int proRow = width / (size + gap);
		
		for (Researches research : Researches.values())
		{
			Assistant.drawOutline(guiPoint.x - 115 + ((research.ordinal() % proRow) * (size + gap)), guiPoint.y - 80 + ((research.ordinal() / proRow) * (size + gap)), size + 16, size + 16, false, g);
			Assistant.drawImage(Game.getImage("researches.png"), guiPoint.x - 115 + ((research.ordinal() % proRow) * (size + gap)) + 8, guiPoint.y - 80 + ((research.ordinal() / proRow) * (size + gap)) + 8, size, size, research.getTexturePoint().x * Osize, research.getTexturePoint().y * Osize, Osize, Osize, g);
		}
	}
	
	@Override
	public void initGUI()
	{}
}
