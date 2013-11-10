package de.dakror.villagedefense.ui.button;

import java.awt.event.MouseEvent;

import de.dakror.villagedefense.ui.ClickEvent;
import de.dakror.villagedefense.ui.Component;

/**
 * @author Dakror
 */
public abstract class Button extends Component
{
	ClickEvent event;
	
	public Button(int x, int y, int width, int height, ClickEvent event)
	{
		super(x, y, width, height);
		this.event = event;
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
}
