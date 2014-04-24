package de.dakror.villagedefense.ui.button;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import de.dakror.gamesetup.ui.Component;
import de.dakror.villagedefense.ui.ClickEvent;

/**
 * @author Dakror
 */
public abstract class Button extends Component
{
	ArrayList<ClickEvent> events;
	
	public Button(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		events = new ArrayList<>();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (contains(e.getX(), e.getY()) && enabled)
		{
			triggerEvents();
			state = 0;
		}
	}
	
	public void addClickEvent(ClickEvent e)
	{
		events.add(e);
	}
	
	public void triggerEvents()
	{
		for (ClickEvent e1 : events)
			e1.trigger();
	}
}
