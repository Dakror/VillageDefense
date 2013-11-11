package de.dakror.villagedefense.ui.button;

import java.awt.Graphics2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class ArrowButton extends Button
{
	public static int MARGIN = 52;
	
	int tx, ty;
	
	public ArrowButton(int x, int y, int tx, int ty)
	{
		super(x, y, 32, 32);
		
		this.tx = tx;
		this.ty = ty;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Assistant.drawImage(Game.getImage("gui/gui.png"), x, y, width, height, tx, ty + MARGIN * state, width, height, g);
	}
	
	@Override
	public void update(int tick)
	{}
}
