package de.dakror.villagedefense.ui;

import java.awt.event.MouseEvent;

/**
 * @author Dakror
 */
public abstract class Button extends Component
{
	/**
	 * 0 = default<br>
	 * 1 = pressed<br>
	 * 2 = hovered<br>
	 * 3 = disabled<br>
	 */
	public int state;
	ClickEvent event;
	
	public Button(int x, int y, int width, int height, ClickEvent event)
	{
		super(x, y, width, height);
		this.event = event;
		state = 0;
	}
	
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
