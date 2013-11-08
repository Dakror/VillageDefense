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
	 */
	public int state;
	public boolean enabled;
	ClickEvent event;
	
	public Button(int x, int y, int width, int height, ClickEvent event)
	{
		super(x, y, width, height);
		this.event = event;
		state = 0;
		enabled = true;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (contains(e.getX(), e.getY())) state = 1;
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (contains(e.getX(), e.getY()) && enabled)
		{
			event.trigger();
			state = 0;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		state = contains(e.getX(), e.getY()) ? 2 : 0;
	}
}
