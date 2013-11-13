package de.dakror.villagedefense.layer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Rock;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.Tree;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.settings.WaveManager;
import de.dakror.villagedefense.settings.WaveManager.Monster;
import de.dakror.villagedefense.ui.button.BuildButton;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class HUDLayer extends Layer
{
	public static HUDLayer currentHudLayer;
	
	Resources rps;
	
	public HUDLayer()
	{
		super();
		
		currentHudLayer = this;
	}
	
	@Override
	public void init()
	{
		for (int i = 0; i < Game.buildableStructs.length; i++)
		{
			int x = Game.getWidth() / 2 + BuildButton.SIZE / 4 - (Game.buildableStructs.length * (BuildButton.SIZE + 32)) / 2 + i * (BuildButton.SIZE + 32);
			BuildButton bb = new BuildButton(x, Game.getHeight() - 84, Game.buildableStructs[i]);
			components.add(bb);
		}
		
		rps = new Resources();
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		try
		{
			// -- wave monster faces -- //
			int selectedMonster = -1;
			if (WaveManager.monsters.size() > 0)
			{
				int cSize = 70;
				Monster[] keys = WaveManager.monsters.keySet().toArray(new Monster[] {});
				
				
				for (int i = 0; i < WaveManager.monsters.size(); i++)
				{
					Monster m = keys[i];
					
					int x = Game.getWidth() / 2 + 200 + i * cSize;
					Assistant.drawShadow(x, 72, cSize, cSize, g);
					Assistant.drawOutline(x, 72, cSize, cSize, false, g);
					g.drawImage(Game.getImage("creature/" + m.getImage() + "_face.png"), Game.getWidth() / 2 + 200 + i * cSize + (cSize - 48) / 2, 72 + (cSize - 48) / 2, 48, 48, Game.w);
					
					Assistant.drawString(WaveManager.monsters.get(m) + "", x + 6, 72 + cSize - 6, g, 22);
					
					if (new Rectangle(x, 72, cSize, cSize).contains(Game.currentGame.mouse)) selectedMonster = i;
				}
			}
			
			// -- selected entity stuff -- //
			Assistant.drawContainer(Game.getWidth() / 2 - 175, 70, 350, 60, false, false, g);
			if (Game.world.selectedEntity != null)
			{
				Assistant.drawHorizontallyCenteredString(Game.world.selectedEntity.getName(), Game.getWidth(), 111, g, 40);
				
				if (Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH_MAX) > Attribute.HEALTH_MAX.getDefaultValue())
				{
					Assistant.drawProgressBar(Game.getWidth() / 2 - 179, 111, 358, Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH) / Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH_MAX), "ff3232", g);
					Color oldColor = g.getColor();
					g.setColor(Color.white);
					Assistant.drawHorizontallyCenteredString((int) Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH) + " / " + (int) Game.world.selectedEntity.getAttributes().get(Attribute.HEALTH_MAX), Game.getWidth(), 126, g, 15);
					g.setColor(oldColor);
				}
				
				if (Game.world.selectedEntity.getResources().size() > 0)
				{
					ArrayList<Resource> resources = Game.world.selectedEntity.getResources().getFilled();
					Assistant.drawShadow(0, 80, 160, resources.size() * 24 + 40, g);
					for (int i = 0; i < resources.size(); i++)
					{
						Assistant.drawResource(Game.world.selectedEntity.getResources(), resources.get(i), 16, 100 + i * 24, 26, 30, g);
					}
				}
				
				if (Game.world.selectedEntity instanceof Struct)
				{
					Struct s = (Struct) Game.world.selectedEntity;
					if (!(s instanceof Tree) && !(s instanceof Rock))
					{
						Assistant.drawShadow(Game.getWidth() / 2 - 260, 72, 70, 70, g);
						
						if (new Rectangle(Game.getWidth() / 2 - 260, 72, 70, 70).contains(Game.currentGame.mouse)) Assistant.drawContainer(Game.getWidth() / 2 - 260, 72, 70, 70, false, false, g);
						else Assistant.drawOutline(Game.getWidth() / 2 - 260, 72, 70, 70, false, g);
						
						g.drawImage(Game.getImage("icon/bomb.png"), Game.getWidth() / 2 - 250, 82, 50, 50, Game.w);
					}
				}
			}
			
			// -- top bar -- //
			int total = Game.getWidth() / 2 - 200;
			boolean hover = new Rectangle(0, 0, total + 48, 80).contains(Game.currentGame.mouse);
			
			int w = total / (Resource.values().length);
			int min = Game.getWidth() / (hover ? 5 : 10);
			if (w < min) w = min;
			
			int rows = Math.round((Resource.values().length * (float) w) / total);
			int del = Resource.values().length / rows;
			
			Assistant.drawContainer(0, 0, Game.getWidth(), 80, false, false, g);
			if (hover) Assistant.drawContainer(0, 0, Game.getWidth() / 2 - 150, rows * 30 + 20, false, false, g);
			
			for (int i = 0; i < Resource.values().length; i++)
			{
				Resource r = Resource.values()[i];
				float dif = Math.round(rps.getF(r) * 100) / 100f;
				
				String delta = (dif != 0 ? " (" + (dif < 0 ? "" : "+") + dif + ")" : "");
				if (!hover) delta = "";
				
				
				if (r == Resource.PEOPLE)
				{
					int free = Game.currentGame.getPeople();
					Assistant.drawLabelWithIcon(25 + (i % del) * w, 13 + 30 * (i / del), 30, new Point(r.getIconX(), r.getIconY()), (free != Game.currentGame.resources.get(r) ? Game.currentGame.getPeople() + " / " : "") + Game.currentGame.resources.get(r) + delta, 30, g);
				}
				else
				{
					Assistant.drawLabelWithIcon(25 + (i % del) * w, 13 + 30 * (i / del), 30, new Point(r.getIconX(), r.getIconY()), Game.currentGame.resources.get(r) + delta, 30, g);
				}
			}
			
			// -- wave info -- //
			Assistant.drawString("Welle: " + WaveManager.wave, Game.getWidth() / 2 + 170, 55, g, 45);
			
			Assistant.drawHorizontallyCenteredString("Punktestand: " + Game.currentGame.getPlayerScore(), Game.getWidth() / 2, Game.getWidth() / 2, 50, g, 25);
			
			// -- time panel -- //
			Assistant.drawContainer(Game.getWidth() / 2 - 150, 0, 300, 80, true, true, g);
			String minutes = ((int) Math.floor(WaveManager.nextWave / 60f)) + "";
			String seconds = (WaveManager.nextWave % 60) + "";
			
			while (minutes.length() < 2)
				minutes = "0" + minutes;
			while (seconds.length() < 2)
				seconds = "0" + seconds;
			
			Assistant.drawHorizontallyCenteredString(minutes + ":" + seconds, Game.getWidth(), 60, g, 70);
			
			// -- forward -- //
			int x = Game.getWidth() / 2 + 100;
			int y = 30;
			
			boolean hover2 = new Rectangle(x, y, 60, 60).contains(Game.currentGame.mouse);
			
			if (hover2 || Game.currentGame.updateThread.speed != 1) Assistant.drawShadow(x, y, 60, 60, g);
			Assistant.drawOutline(x, y, 60, 60, false, g);
			g.drawImage(Game.getImage("icon/fwd.png"), x + 10, y + 10, 40, 40, Game.w);
			
			// -- pause -- //
			if (!new Rectangle(Game.getWidth() - 75, 5, 70, 70).contains(Game.currentGame.mouse)) Assistant.drawContainer(Game.getWidth() - 75, 5, 70, 70, false, false, g);
			else Assistant.drawContainer(Game.getWidth() - 80, 0, 80, 80, false, true, g);
			
			g.drawImage(Game.getImage("gui/pause.png"), Game.getWidth() - 75, 5, 70, 70, Game.w);
			
			// -- build/bottom bar -- //
			Assistant.drawContainer(0, Game.getHeight() - 100, Game.getWidth(), 100, false, false, g);
			
			// -- UI components -- //
			drawComponents(g);
			
			// -- monster tooltip -- //
			if (selectedMonster != -1)
			{
				String text = Monster.values()[selectedMonster].getDescription();
				String name = text.substring(0, text.indexOf(":"));
				text = text.substring(text.indexOf(":") + 1);
				String[] lines = text.split("\\. ");
				int width = 0;
				for (String s : lines)
				{
					int myW = g.getFontMetrics(g.getFont().deriveFont(24f)).stringWidth(s);
					if (myW > width) width = myW;
				}
				
				width += 32;
				int height = 64 + text.split("\\. ").length * 30;
				
				Assistant.drawShadow(Game.currentGame.mouse.x + 12, Game.currentGame.mouse.y + 24, width, height, g);
				Assistant.drawOutline(Game.currentGame.mouse.x + 12, Game.currentGame.mouse.y + 24, width, height, false, g);
				
				Assistant.drawHorizontallyCenteredString(name, Game.currentGame.mouse.x + 12, width, Game.currentGame.mouse.y + 24 + 40, g, 30);
				for (int i = 0; i < lines.length; i++)
				{
					Assistant.drawString(lines[i] + ".", Game.currentGame.mouse.x + 25, Game.currentGame.mouse.y + 24 + 70 + i * 30, g, 24);
				}
			}
		}
		catch (Exception e)
		{}
	}
	
	@Override
	public void update(int tick)
	{
		updateComponents(tick);
		
		rps = new Resources();
		
		for (Entity e : Game.world.entities)
		{
			if (e instanceof Struct) rps.add(((Struct) e).getResourcesPerSecond());
		}
		
		
		for (int i = 0; i < components.size(); i++)
		{
			int x = Game.getWidth() / 2 + BuildButton.SIZE / 4 - (components.size() * (BuildButton.SIZE + 32)) / 2 + i * (BuildButton.SIZE + 32);
			components.get(i).setX(x);
			components.get(i).setY(Game.getHeight() - 84);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (Game.currentGame.state == 0)
		{
			if (new Rectangle(Game.getWidth() / 2 + 100, 30, 60, 60).contains(e.getPoint()))
			{
				Game.currentGame.updateThread.ticks = 0;
				Game.currentGame.frames = 0;
				Game.currentGame.start = System.currentTimeMillis();
				Game.currentGame.updateThread.speed = Game.currentGame.updateThread.speed == 1 ? 5 : 1;
			}
			
			if (Game.world.selectedEntity != null && Game.world.selectedEntity instanceof Struct)
			{
				if (new Rectangle(Game.getWidth() / 2 - 260, 72, 70, 70).contains(e.getPoint()))
				{
					Resources res = ((Struct) Game.world.selectedEntity).getBuildingCosts();
					
					for (Resource r : res.getFilled())
					{
						if (r.isUsable()) Game.currentGame.resources.add(r, Math.round(res.get(r) / 4f)); // give 25% back
					}
					
					Game.world.selectedEntity.kill();
					Game.currentGame.killedCoreHouse = true; // hack to prevent StateLayer from funk around
				}
			}
		}
	}
}
