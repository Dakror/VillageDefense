package de.dakror.villagedefense.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Assistant;

public class ResearchButton extends Button
{
	public static final int tSize = 48;
	
	Researches research;
	
	public ResearchButton(int x, int y, Researches research)
	{
		super(x, y, 32, 32, new ClickEvent()
		{
			@Override
			public void trigger()
			{}
		});
		this.research = research;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Assistant.drawContainer(x - 8, y - 8, width + 16, height + 16, false, state == 2, false, g);
		Assistant.drawImage(Game.getImage("researches.png"), x, y, width, height, research.getTexturePoint().x * tSize, research.getTexturePoint().y * tSize, tSize, tSize, g);
		
		if (state == 3) Assistant.drawShadow(x - 10, y - 10, width + 20, height + 20, g);
	}
	
	public void drawTooltip(int x, int y, Graphics2D g)
	{
		int w = g.getFontMetrics(g.getFont().deriveFont(30f)).stringWidth(research.getName()) + 32;
		w = w > 150 ? w : 150;
		
		ArrayList<Resource> filled = research.getCosts().getFilled();
		boolean hasPreq = false;
		for (Resource r : filled)
		{
			if (!r.isUsable())
			{
				hasPreq = true;
				break;
			}
		}
		
		int height = 64 + (research.getCosts().size() + (hasPreq ? 2 : 1)) * 26;
		Assistant.drawShadow(x, y - height, w, height, g);
		Assistant.drawOutline(x, y - height, w, height, false, g);
		
		Assistant.drawHorizontallyCenteredString(research.getName(), x, w, y - height + 40, g, 30);
		
		int y1 = y - height + 80;
		
		// -- costs -- //
		y1 -= 12;
		Assistant.drawHorizontallyCenteredString("Baukosten", x + 60, 0, y1, g, 24);
		y1 += 8;
		for (int i = 0; i < research.getCosts().size(); i++)
		{
			if (!filled.get(i).isUsable())
			{
				hasPreq = true;
				continue;
			}
			Color oldColor = g.getColor();
			
			if (Game.currentGame.resources.get(filled.get(i)) < research.getCosts().get(filled.get(i))) g.setColor(Color.decode("#6b0000"));
			Assistant.drawResource(research.getCosts(), filled.get(i), x + 16, y1, 24, 30, g);
			y1 += 26;
			g.setColor(oldColor);
		}
		
		// -- prerequisites -- //
		if (hasPreq)
		{
			y1 += 20;
			Assistant.drawHorizontallyCenteredString("Bedingungen", x + 70, 0, y1, g, 24);
			y1 += 6;
			
			for (int i = 0; i < research.getCosts().size(); i++)
			{
				Resource res = filled.get(i);
				if (res.isUsable()) continue;
				Color oldColor = g.getColor();
				
				if ((res.equals(Resource.PEOPLE) ? Game.currentGame.getPeople() : Game.currentGame.resources.get(res)) < research.getCosts().get(res)) g.setColor(Color.decode("#6b0000"));
				else g.setColor(Color.decode("#18acf1"));
				Assistant.drawResource(research.getCosts(), res, x + 16, y1, 24, 30, g);
				
				y1 += 26;
				g.setColor(oldColor);
			}
		}
	}
	
	@Override
	public void update(int tick)
	{
		ArrayList<Resource> filled = research.getCosts().getFilled();
		for (Resource r : filled)
		{
			if ((r.equals(Resource.PEOPLE) ? Game.currentGame.getPeople() : Game.currentGame.resources.get(r)) < research.getCosts().get(r))
			{
				state = 3;
				return;
			}
		}
	}
}
