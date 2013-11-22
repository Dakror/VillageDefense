package de.dakror.villagedefense.ui.button;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.ui.ClickEvent;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class BuildButton extends Button
{
	public static final int SIZE = 68;
	
	Struct struct;
	Dimension scale;
	
	public BuildButton(int x, int y, final Struct s)
	{
		super(x, y, SIZE, SIZE);
		addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.activeStruct = (Struct) s.clone();
			}
		});
		struct = s;
		scale = Assistant.scaleTo(new Dimension(struct.getWidth(), struct.getHeight()), new Dimension(width, height));
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		if (state == 2) Assistant.drawContainer(x - 10, y - 16, width + 20, height + 32, false, true, g);
		else Assistant.drawOutline(x - 10, y - 10, width + 20, height + 20, false, g);
		
		g.drawImage(struct.getImage(), x + (SIZE - scale.width) / 2, y + (SIZE - scale.height) / 2, scale.width, scale.height, Game.w);
		
		if (!enabled)
		{
			if (state == 2) Assistant.drawShadow(x - 20, y - 26, width + 40, height + 52, g);
			else Assistant.drawShadow(x - 10, y - 10, width + 20, height + 20, g);
		}
	}
	
	@Override
	public void drawTooltip(int x, int y, Graphics2D g)
	{
		int w = g.getFontMetrics(g.getFont().deriveFont(30f)).stringWidth(struct.getName()) + 32;
		
		if (x + w + 30 > Game.getWidth()) x -= (x + w + 30) - Game.getWidth();
		
		int min = 170;
		
		w = w > min ? w : min;
		
		ArrayList<Resource> filled = struct.getBuildingCosts().getFilled();
		boolean hasPreq = false;
		for (Resource r : filled)
		{
			if (!r.isUsable())
			{
				hasPreq = true;
				break;
			}
		}
		
		int infoHeight = 26 + (struct.getResourcesPerSecond().size() + Assistant.getLineCount(struct.description, w - 20, g, 24)) * 24 - 12;
		int height = 64 + (struct.getBuildingCosts().size() + (hasPreq ? 3 : 2) - (struct.getAttributes().get(Attribute.HEALTH) > Attribute.HEALTH.getDefaultValue() ? 0 : 1)) * 26 + infoHeight;
		Assistant.drawShadow(x, y - height, w, height, g);
		Assistant.drawOutline(x, y - height, w, height, false, g);
		
		Assistant.drawHorizontallyCenteredString(struct.getName(), x, w, y - height + 40, g, 30);
		
		int y1 = y - height + 80;
		
		if (struct.getAttributes().get(Attribute.HEALTH) > Attribute.HEALTH.getDefaultValue())
		{
			Assistant.drawLabelWithIcon(x + 16, y - height + 48, 24, new Point(1, 0), (int) struct.getAttributes().get(Attribute.HEALTH_MAX) + "", 30, g);
			y1 += 26;
		}
		
		// -- costs -- //
		y1 -= 12;
		Assistant.drawString((filled.size() == 0 ? "Keine " : "") + "Baukosten", x + 10, y1, g, 24);
		y1 += 8;
		for (int i = 0; i < struct.getBuildingCosts().size(); i++)
		{
			if (!filled.get(i).isUsable())
			{
				hasPreq = true;
				continue;
			}
			Color oldColor = g.getColor();
			
			if (Game.currentGame.resources.get(filled.get(i)) < struct.getBuildingCosts().get(filled.get(i))) g.setColor(Color.decode("#6b0000"));
			Assistant.drawResource(struct.getBuildingCosts(), filled.get(i), x + 16, y1, 24, 30, g);
			y1 += 26;
			g.setColor(oldColor);
		}
		
		// -- prerequisites -- //
		if (hasPreq)
		{
			y1 += 20;
			Assistant.drawString("Bedingungen", x + 10, y1, g, 24);
			y1 += 6;
			
			for (int i = 0; i < struct.getBuildingCosts().size(); i++)
			{
				Resource res = filled.get(i);
				if (res.isUsable()) continue;
				Color oldColor = g.getColor();
				
				if ((res == Resource.PEOPLE ? Game.currentGame.getPeople() : Game.currentGame.resources.get(res)) < struct.getBuildingCosts().get(res)) g.setColor(Color.decode("#6b0000"));
				else g.setColor(Color.decode("#18acf1"));
				Assistant.drawResource(struct.getBuildingCosts(), res, x + 16, y1, 24, 30, g);
				
				y1 += 26;
				g.setColor(oldColor);
			}
		}
		
		// -- info -- //
		y1 += 20;
		Assistant.drawString("Details", x + 10, y1, g, 24);
		y1 += 26;
		
		Assistant.drawStringWrapped(struct.description, x + 10, y1, w - 20, g, 24);
		y1 += Assistant.getLineCount(struct.description, w - 20, g, 24) * 24 - 30;
		
		Resources res = struct.getResourcesPerSecond();
		ArrayList<Resource> fll = res.getFilled();
		
		for (int i = 0; i < res.size(); i++)
		{
			float r = Math.round(res.getF(fll.get(i)) * 100) / 100f;
			Assistant.drawLabelWithIcon(x + 16, y1, 24, new Point(fll.get(i).getIconX(), fll.get(i).getIconY()), (r > 0 ? "+" : "") + r + "/s", 30, g);
			y1 += 26;
		}
	}
	
	@Override
	public void update(int tick)
	{
		ArrayList<Resource> filled = struct.getBuildingCosts().getFilled();
		for (Resource r : filled)
		{
			if ((r == Resource.PEOPLE ? Game.currentGame.getPeople() : Game.currentGame.resources.get(r)) < struct.getBuildingCosts().get(r))
			{
				enabled = false;
				return;
			}
		}
		
		enabled = true;
	}
	
	public Struct getStruct()
	{
		return struct;
	}
}
