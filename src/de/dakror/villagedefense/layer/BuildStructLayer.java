package de.dakror.villagedefense.layer;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Catapult;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.ui.BuildBar;
import de.dakror.villagedefense.ui.Component;
import de.dakror.villagedefense.ui.button.BuildButton;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class BuildStructLayer extends Layer
{
	public boolean canPlace;
	
	@Override
	public void draw(Graphics2D g)
	{
		try
		{
			if (Game.currentGame.activeStruct != null)
			{
				Game.currentGame.activeStruct.setX(Assistant.round(Game.currentGame.mouse.x - Game.currentGame.activeStruct.getBump(false).x, Tile.SIZE) + (Game.world.x % Tile.SIZE));
				Game.currentGame.activeStruct.setY(Assistant.round(Game.currentGame.mouse.y - Game.currentGame.activeStruct.getBump(false).y, Tile.SIZE) + (Game.world.y % Tile.SIZE));
				Game.currentGame.activeStruct.setClicked(true);
				
				Rectangle bump = Game.currentGame.activeStruct.getBump(true);
				bump.translate(-Game.world.x % Tile.SIZE, -Game.world.y % Tile.SIZE);
				int malus = 5;
				
				canPlace = true;
				
				int centerY = Assistant.round(Math.round(Game.world.height / 2f), Tile.SIZE);
				
				for (int i = Assistant.round(bump.x, Tile.SIZE) + Game.world.x % Tile.SIZE; i < bump.x + bump.width + Game.world.x % Tile.SIZE; i += Tile.SIZE)
				{
					for (int j = Assistant.round(bump.y, Tile.SIZE) + Game.world.y % Tile.SIZE; j < bump.y + bump.height + Game.world.y % Tile.SIZE; j += Tile.SIZE)
					{
						boolean blocked = false;
						
						if (Game.currentGame.activeStruct.canPlaceOnWay())
						{
							blocked = true;
						}
						
						if (j == centerY + Tile.SIZE + Game.world.y || j == centerY + Game.world.y)
						{
							blocked = !Game.currentGame.activeStruct.canPlaceOnWay();
						}
						
						for (Entity e : Game.world.entities)
						{
							if (e.getBump(true).intersects(i - Game.world.x, j - Game.world.y, Tile.SIZE, Tile.SIZE))
							{
								blocked = true;
								break;
							}
						}
						if (blocked) canPlace = false;
						
						g.drawImage(Game.getImage(blocked ? "tile/blockedtile.png" : "tile/freetile.png"), i - malus, j - malus, Tile.SIZE + malus * 2, Tile.SIZE + malus * 2, Game.w);
					}
				}
				
				Composite oldComposite = g.getComposite();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
				if (Game.currentGame.activeStruct != null) Game.currentGame.activeStruct.draw(g);
				g.setComposite(oldComposite);
			}
		}
		catch (NullPointerException e)
		{}
	}
	
	@Override
	public void update(int tick)
	{}
	
	@Override
	public void init()
	{}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (Game.currentGame.activeStruct != null && e.getButton() == 1)
		{
			if (canPlace && e.getY() > 80 && e.getY() < Game.getHeight() - 100)
			{
				Game.currentGame.activeStruct.setClicked(false);
				ArrayList<Resource> filled = Game.currentGame.activeStruct.getBuildingCosts().getFilled();
				for (Resource r : filled)
				{
					if (!r.isUsable()) continue;
					Game.currentGame.resources.add(r, -Game.currentGame.activeStruct.getBuildingCosts().get(r));
				}
				
				Game.currentGame.activeStruct.translate(-Game.world.x, -Game.world.y);
				
				Game.world.addEntity(Game.currentGame.activeStruct.clone(), false);
				Game.currentGame.placedStruct = true;
				
				for (Component c : ((BuildBar) HUDLayer.currentHudLayer.components.get(0)).buttons)
				{
					if (c instanceof BuildButton)
					{
						BuildButton b = (BuildButton) c;
						if (b.getStruct().getName().equals(Game.currentGame.activeStruct.getName()))
						{
							c.update(0);
							if (b.enabled)
							{
								Game.currentGame.activeStruct.setClicked(false);
								Game.currentGame.activeStruct = (Struct) b.getStruct().clone();
								
								return;
							}
						}
					}
				}
			}
			else return;
		}
		
		Game.currentGame.activeStruct = null;
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		super.keyReleased(e);
		
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_ESCAPE:
			{
				Game.currentGame.activeStruct = null;
				break;
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		super.mouseWheelMoved(e);
		if (Game.currentGame.activeStruct instanceof Catapult)
		{
			Catapult c = (Catapult) Game.currentGame.activeStruct;
			c.setDownwards(e.getWheelRotation() == 1);
		}
	}
}
