package de.dakror.villagedefense.layer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.settings.WaveManager;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class NewGameLayer extends Layer
{
	MenuLayer ml;
	int height = 90;
	int h;
	String[] sizes = { "Normal", "Groß", "Riesig", "Extrem" };
	int[][] res = { { 1920, 1024 }, { 3072, 1728 }, { 5120, 2880 }, { 8192, 4608 } };
	
	public NewGameLayer(MenuLayer ml)
	{
		this.ml = ml;
		modal = true;
		ml.setEnabled(false);
		h = Game.getHeight() / 2 > 400 ? Game.getHeight() / 2 : 400;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Assistant.drawContainer(Game.getWidth() / 8 * 3 - 20, Game.getHeight() / 4, Game.getWidth() / 4 + 40, h, true, false, g);
		Assistant.drawHorizontallyCenteredString("Neues Spiel", Game.getWidth(), Game.getHeight() / 4 + 70, g, 70);
		
		for (int i = 0; i < sizes.length; i++)
		{
			Rectangle r = new Rectangle(Game.getWidth() / 8 * 3, Game.getHeight() / 4 + 100 + height * i, Game.getWidth() / 4, height - 10);
			
			Assistant.drawShadow(r.x, r.y, r.width, r.height, g);
			Assistant.drawOutline(r.x, r.y, r.width, r.height, r.contains(Game.currentGame.mouse), g);
			Assistant.drawHorizontallyCenteredString(sizes[i], Game.getWidth(), r.y + 50, g, 35);
		}
	}
	
	@Override
	public void update(int tick)
	{
		h = Game.getHeight() / 2 > 400 ? Game.getHeight() / 2 : 400;
	}
	
	@Override
	public void init()
	{}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (!new Rectangle(Game.getWidth() / 8 * 3 - 20, Game.getHeight() / 4, Game.getWidth() / 4 + 40, h).contains(e.getPoint()))
		{
			ml.setEnabled(true);
			Game.currentGame.layers.remove(this);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		
		for (int i = 0; i < sizes.length; i++)
		{
			Rectangle r = new Rectangle(Game.getWidth() / 8 * 3, Game.getHeight() / 4 + 100 + height * i, Game.getWidth() / 4, height - 10);
			if (r.contains(e.getPoint()))
			{
				Game.currentGame.skipDraw = true;
				Game.currentGame.startGame(res[i][0], res[i][1]);
				Game.currentGame.state = 3;
				WaveManager.nextWave = WaveManager.waveTimer;
				Game.currentGame.fadeTo(1, 0.05f);
				ml.setEnabled(true);
				Game.currentGame.layers.remove(this);
			}
		}
	}
}
