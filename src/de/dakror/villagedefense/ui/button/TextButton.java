package de.dakror.villagedefense.ui.button;

import java.awt.Graphics2D;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class TextButton extends Button
{
	static final int ty = 124, tx = 12, tw = 288, th = 58;
	
	String text;
	int size;
	
	public TextButton(int x, int y, int width, String text, int size)
	{
		super(x, y, width, Math.round(th / (float) tw * width));
		this.text = text;
		this.size = size;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Helper.drawImage(Game.getImage("gui/gui.png"), x, y, width, height, tx, ty + (78) * state, tw, th, g);
		Helper.drawHorizontallyCenteredString(text, x, width, y + size, g, size);
	}
	
	@Override
	public void update(int tick)
	{}
}
