package de.dakror.villagedefense.ui;

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
	int state;
	ClickEvent event;
	
	public Button(int x, int y, int width, int height, ClickEvent event)
	{
		super(x, y, width, height);
		this.event = event;
		state = 0;
	}
}
