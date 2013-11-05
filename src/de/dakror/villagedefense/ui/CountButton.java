package de.dakror.villagedefense.ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class CountButton extends Component
{
	int scrollSpeed = 3;
	long timeDown = 0;
	
	public int min, max, value, step;
	
	ArrowButton minus, plus;
	
	public CountButton(int x, int y, int width, int min, int max, int step, int value)
	{
		super(x, y, width, 32);
		this.min = min;
		this.max = max;
		this.step = step;
		this.value = value;
		
		minus = new ArrowButton(x, y, 326, 16, new ClickEvent()
		{
			@Override
			public void trigger()
			{
				CountButton.this.value = (CountButton.this.value - CountButton.this.step >= CountButton.this.min) ? CountButton.this.value - CountButton.this.step : CountButton.this.min;
			}
		});
		plus = new ArrowButton(x + width - 32, y, 363, 16, new ClickEvent()
		{
			@Override
			public void trigger()
			{
				CountButton.this.value = (CountButton.this.value + CountButton.this.step <= CountButton.this.max) ? CountButton.this.value + CountButton.this.step : CountButton.this.max;
			}
		});
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		minus.draw(g);
		Assistant.drawHorizontallyCenteredString(value + "", x, width, y + 25, g, 25);
		plus.draw(g);
	}
	
	@Override
	public void update(int tick)
	{
		if (System.currentTimeMillis() - timeDown > 800)
		{
			if (tick % scrollSpeed == 0 && minus.state == 1) minus.event.trigger();
			if (tick % scrollSpeed == 0 && plus.state == 1) plus.event.trigger();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		minus.mousePressed(e);
		plus.mousePressed(e);
		
		if (minus.state == 1 || plus.state == 1) timeDown = System.currentTimeMillis();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		minus.mouseReleased(e);
		plus.mouseReleased(e);
		
		timeDown = 0;
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		minus.mouseMoved(e);
		plus.mouseMoved(e);
	}
}
