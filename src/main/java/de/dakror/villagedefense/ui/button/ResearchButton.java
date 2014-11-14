package de.dakror.villagedefense.ui.button;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.ui.ClickEvent;
import de.dakror.villagedefense.util.Assistant;

public class ResearchButton extends Button {
	public static final int tSize = 48;
	
	public Researches research;
	boolean contains;
	boolean discount;
	
	public ResearchButton(int x, int y, final Researches research, final ArrayList<Researches> pool) {
		super(x, y, 32, 32);
		
		addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				if (!pool.contains(research)) {
					ArrayList<Resource> filled = research.getCosts(discount).getFilled();
					for (Resource r : filled) {
						if (!r.isUsable()) continue;
						Game.currentGame.resources.add(r, -research.getCosts(discount).get(r));
					}
					
					pool.add(research);
					contains = true;
				}
			}
		});
		this.research = research;
		discount = false;
		contains = pool.contains(research);
	}
	
	public ResearchButton(int x, int y, final Researches research, final ArrayList<Researches> pool, final Struct struct) {
		super(x, y, 32, 32);
		
		addClickEvent(new ClickEvent() {
			@Override
			public void trigger() {
				if (!pool.contains(research)) {
					ArrayList<Resource> filled = research.getCosts(discount).getFilled();
					for (Resource r : filled) {
						if (!r.isUsable()) continue;
						Game.currentGame.resources.add(r, -research.getCosts(discount).get(r));
					}
					
					pool.add(research);
					contains = true;
					struct.onUpgrade(research, false);
				}
			}
		});
		this.research = research;
		discount = true;
		contains = pool.contains(research);
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (state == 2 || contains) Helper.drawContainer(x - 8, y - 8, width + 16, height + 16, false, state == 2 || contains, true, g);
		else Helper.drawOutline(x - 8, y - 8, width + 16, height + 16, false, g);
		
		Helper.drawImage(Game.getImage("researches.png"), x, y, width, height, research.getTexturePoint().x * tSize, research.getTexturePoint().y * tSize, tSize, tSize, g);
		
		if (!enabled && !contains) {
			if (state == 2) Helper.drawShadow(x - 14, y - 14, width + 28, height + 28, g);
			else Helper.drawShadow(x - 4, y - 4, width + 8, height + 8, g);
		}
	}
	
	@Override
	public void drawTooltip(int x, int y, Graphics2D g) {
		int w = g.getFontMetrics(g.getFont().deriveFont(30f)).stringWidth(research.getName()) + 32;
		w = w > 150 ? w : 150;
		
		Resources costs = research.getCosts(discount);
		
		ArrayList<Resource> filled = costs.getFilled();
		boolean hasPreq = false;
		for (Resource r : filled) {
			if (!r.isUsable()) {
				hasPreq = true;
				break;
			}
		}
		
		int height = 64 + (costs.size() + (hasPreq ? 2 : 1)) * 26;
		Helper.drawShadow(x, y - height, w, height, g);
		Helper.drawOutline(x, y - height, w, height, false, g);
		
		Helper.drawHorizontallyCenteredString(research.getName(), x, w, y - height + 40, g, 30);
		
		int y1 = y - height + 80;
		
		// -- costs -- //
		y1 -= 12;
		Helper.drawString((costs.size() == 0 ? "Keine " : "") + "Kosten", x + 20, y1, g, 24);
		y1 += 8;
		for (int i = 0; i < costs.size(); i++) {
			if (!filled.get(i).isUsable()) {
				hasPreq = true;
				continue;
			}
			Color oldColor = g.getColor();
			
			if (Game.currentGame.resources.get(filled.get(i)) < costs.get(filled.get(i))) g.setColor(Color.decode("#6b0000"));
			Assistant.drawResource(costs, filled.get(i), x + 16, y1, 24, 30, g);
			y1 += 26;
			g.setColor(oldColor);
		}
		
		// -- prerequisites -- //
		if (hasPreq) {
			y1 += 20;
			Helper.drawHorizontallyCenteredString("Bedingungen", x + 70, 0, y1, g, 24);
			y1 += 6;
			
			for (int i = 0; i < costs.size(); i++) {
				Resource res = filled.get(i);
				if (res.isUsable()) continue;
				Color oldColor = g.getColor();
				
				if ((res == Resource.PEOPLE ? Game.currentGame.getPeople() : Game.currentGame.resources.get(res)) < costs.get(res)) g.setColor(Color.decode("#6b0000"));
				else g.setColor(Color.decode("#18acf1"));
				Assistant.drawResource(costs, res, x + 16, y1, 24, 30, g);
				
				y1 += 26;
				g.setColor(oldColor);
			}
		}
	}
	
	@Override
	public void update(int tick) {
		ArrayList<Resource> filled = research.getCosts(discount).getFilled();
		for (Resource r : filled) {
			if ((r == Resource.PEOPLE ? Game.currentGame.getPeople() : Game.currentGame.resources.get(r)) < research.getCosts(discount).get(r)) {
				enabled = false;
				return;
			}
		}
		enabled = true;
	}
	
	public void setContains(boolean b) {
		contains = b;
	}
}
