package de.dakror.villagedefense.ui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class ArrowButton extends Component
{
	public static int MARGIN = 52;
	
	int tx, ty;
	/**
	 * 0 = default<br>
	 * 1 = pressed<br>
	 * 2 = hovered<br>
	 * 3 = disabled<br>
	 */
	int state;
	
	ClickEvent event;
	
	Point m = new Point(0, 0);
	
	public ArrowButton(int x, int y, int tx, int ty, ClickEvent event)
	{
		super(x, y, 32, 32);
		
		this.tx = tx;
		this.ty = ty;
		this.event = event;
		state = 0;
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Assistant.drawImage(Game.getImage("gui/gui.png"), x, y, width, height, tx, ty + MARGIN * state, width, height, g);
		
		g.fillRect(m.x, m.y, 4, 4);
	}
	
	@Override
	public void update(int tick)
	{}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (state == 3) return;
		
		if (contains(e.getX(), e.getY()))
		{
			event.trigger();
			state = 1;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (state == 3) return;
		
		if (contains(e.getX(), e.getY()) && state == 1) state = 0;
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (state == 3) return;
		m = e.getPoint();
		state = contains(e.getX(), e.getY()) ? 2 : 0;
	}
}
