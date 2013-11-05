package de.dakror.villagedefense.ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class TextButton extends Component
{
	static final int ty = 124, tx = 12, tw = 288, th = 58;
	
	String text;
	ClickEvent event;
	/**
	 * 0 = default<br>
	 * 1 = pressed<br>
	 * 2 = hovered<br>
	 * 3 = disabled<br>
	 */
	int state;
	int size;
	
	public TextButton(int x, int y, int width, String text, int size, ClickEvent event)
	{
		super(x, y, width, Math.round(th / (float) tw * width));
		this.text = text;
		this.event = event;
		this.size = size;
		state = 0;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Assistant.drawImage(Game.getImage("gui/gui.png"), x, y, width, height, tx, ty + (78) * state, tw, th, g);
		Assistant.drawHorizontallyCenteredString(text, x, width, y + size, g, size);
	}
	
	@Override
	public void update(int tick)
	{}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (state == 3) return;
		
		if (contains(e.getX(), e.getY())) state = 1;
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3) return;
		
		if (contains(e.getX(), e.getY()) && state == 1)
		{
			event.trigger();
			state = 0;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (state == 3) return;
		
		state = contains(e.getX(), e.getY()) ? 2 : 0;
	}
}
