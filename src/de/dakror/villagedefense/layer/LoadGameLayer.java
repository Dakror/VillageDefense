package de.dakror.villagedefense.layer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.Assistant;
import de.dakror.villagedefense.util.SaveHandler;

/**
 * @author Dakror
 */
public class LoadGameLayer extends Layer
{
	MenuLayer ml;
	File[] saves;
	int y;
	int height = 90;
	int h;
	
	public LoadGameLayer(MenuLayer ml)
	{
		super();
		this.ml = ml;
		modal = true;
		ml.setEnabled(false);
		saves = SaveHandler.getSaves();
		Arrays.sort(saves, new Comparator<File>()
		{
			@Override
			public int compare(File o1, File o2)
			{
				return o2.getName().compareTo(o1.getName());
			}
		});
		y = 0;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Assistant.drawContainer(Game.getWidth() / 8, Game.getHeight() / 8, Game.getWidth() / 8 * 6, Game.getHeight() / 8 * 6, true, false, g);
		Assistant.drawHorizontallyCenteredString("Spiel laden", Game.getWidth(), Game.getHeight() / 8 + 70, g, 70);
		
		Shape c = g.getClip();
		
		h = Assistant.round(Game.getHeight() / 8 * 6 - 140, height);
		g.setClip(new Rectangle(Game.getWidth() / 8 + 20, Game.getHeight() / 8 + 120, Game.getWidth() / 8 * 6 - 40, h));
		
		for (int i = 0; i < saves.length; i++)
		{
			Rectangle r = new Rectangle(Game.getWidth() / 4 + 100, Game.getHeight() / 8 + 120 + height * i + y, Game.getWidth() / 2 - 200, height - 10);
			File f = saves[i];
			Assistant.drawShadow(r.x, r.y, r.width, r.height, g);
			Assistant.drawOutline(r.x, r.y, r.width, r.height, r.contains(Game.currentGame.mouse), g);
			Assistant.drawHorizontallyCenteredString(f.getName().replace(".save", ""), Game.getWidth(), r.y + 50, g, 35);
		}
		
		g.setClip(c);
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
		if (!new Rectangle(Game.getWidth() / 8, Game.getHeight() / 8, Game.getWidth() / 8 * 6, Game.getHeight() / 8 * 6).contains(e.getPoint()))
		{
			ml.setEnabled(true);
			Game.currentGame.layers.remove(this);
		}
		else if (new Rectangle(Game.getWidth() / 4 + 100, Game.getHeight() / 8 + 120, Game.getWidth() / 2 - 200, h).contains(e.getPoint()))
		{
			int eY = Assistant.round(e.getY() - Game.getHeight() / 8 + 120 - y, height) / height - 3;
			SaveHandler.loadSave(saves[eY]);
			
			ml.setEnabled(true);
			Game.currentGame.layers.remove(this);
			Game.currentGame.alpha = 1;
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		super.mouseWheelMoved(e);
		int dif = saves.length * height - h;
		
		if (dif < 0) return;
		
		int delta = -(int) (e.getWheelRotation() * height * 0.25f);
		if (y + delta < -dif) y = -dif;
		else if (y + delta > 0) y = 0;
		else y += delta;
	}
}
