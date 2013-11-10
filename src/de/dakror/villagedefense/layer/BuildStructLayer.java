package de.dakror.villagedefense.layer;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Resources.Resource;
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
				Game.currentGame.activeStruct.setX(Assistant.round(Game.currentGame.mouse.x - Game.currentGame.activeStruct.getBump(false).x, Tile.SIZE)/* - Game.currentGame.activeStruct.getBump(false).x - (Game.currentGame.activeStruct.getBump(true).width % Tile.SIZE) */);
				Game.currentGame.activeStruct.setY(Assistant.round(Game.currentGame.mouse.y - Game.currentGame.activeStruct.getBump(false).y, Tile.SIZE)/* - Game.currentGame.activeStruct.getBump(false).y - (Game.currentGame.activeStruct.getBump(true).height % Tile.SIZE) */);
				Game.currentGame.activeStruct.setClicked(true);
				
				Rectangle bump = Game.currentGame.activeStruct.getBump(true);
				int malus = 5;
				
				canPlace = true;
				
				int centerY = (int) Math.floor(Game.world.height / 2f / Tile.SIZE);
				
				for (int i = bump.x + Game.world.x; i < bump.x + bump.width + Game.world.x; i += Tile.SIZE)
				{
					for (int j = bump.y + Game.world.y; j < bump.y + bump.height + Game.world.y; j += Tile.SIZE)
					{
						boolean blocked = false;
						
						if (Game.currentGame.activeStruct.canPlaceOnWay())
						{
							blocked = true;
						}
						
						if (Assistant.round(j, Tile.SIZE) == centerY * Tile.SIZE + Tile.SIZE || Assistant.round(j, Tile.SIZE) == centerY * Tile.SIZE)
						{
							blocked = !Game.currentGame.activeStruct.canPlaceOnWay();
						}
						
						for (Entity e : Game.world.entities)
						{
							if (e.getBump(true).intersects(i + 5, j + 5, Tile.SIZE - 10, Tile.SIZE - 10))
							{
								blocked = true;
								break;
							}
						}
						
						if (blocked) canPlace = false;
						
						g.drawImage(Game.getImage(blocked ? "tile/blockedtile.png" : "tile/freetile.png"), Assistant.round(i, Tile.SIZE) - malus, Assistant.round(j, Tile.SIZE) - malus, Tile.SIZE + malus * 2, Tile.SIZE + malus * 2, Game.w);
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
			if (canPlace && e.getY() < Game.getHeight() - 100)
			{
				Game.currentGame.activeStruct.setClicked(false);
				ArrayList<Resource> filled = Game.currentGame.activeStruct.getBuildingCosts().getFilled();
				for (Resource r : filled)
				{
					if (!r.isUsable()) continue;
					Game.currentGame.resources.add(r, -Game.currentGame.activeStruct.getBuildingCosts().get(r));
				}
				Game.world.addEntity(Game.currentGame.activeStruct);
				
				for (Component c : components)
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
}
