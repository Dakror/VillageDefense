package de.dakror.villagedefense.layer;

import java.awt.Graphics2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.settings.WaveManager;
import de.dakror.villagedefense.ui.ClickEvent;
import de.dakror.villagedefense.ui.button.MenuButton;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class MenuLayer extends Layer
{
	public MenuLayer()
	{
		modal = true;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		g.drawImage(Game.getImage("gui/menu/menuBG.png"), 0, 0, Game.getWidth(), Game.getHeight(), Game.w);
		Assistant.drawImageCenteredRelativeScaled(Game.getImage("gui/menu/menuFG.png"), 80, 1920, 1080, Game.getWidth(), Game.getHeight(), g);
		
		drawComponents(g);
	}
	
	@Override
	public void update(int tick)
	{
		if (Game.currentGame.alpha == 1)
		{
			Game.currentGame.layers.remove(Game.currentGame.layers.size() - 1);
			Game.currentGame.fadeTo(0, 0.05f);
			return;
		}
		
		updateComponents(tick);
	}
	
	@Override
	public void init()
	{
		MenuButton start = new MenuButton("newGame", 0);
		start.addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				Game.currentGame.state = 3;
				WaveManager.nextWave = WaveManager.waveTimer;
				Game.currentGame.fadeTo(1, 0.05f);
			}
		});
		components.add(start);
		
		MenuButton quit = new MenuButton("quit", 1);
		quit.addClickEvent(new ClickEvent()
		{
			@Override
			public void trigger()
			{
				System.exit(0);
			}
		});
		components.add(quit);
	}
}
