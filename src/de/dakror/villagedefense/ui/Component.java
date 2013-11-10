package de.dakror.villagedefense.ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.EventListener;

/**
 * @author Dakror
 */
public abstract class Component extends EventListener implements Drawable
{
	protected int x, y, width, height;
	/**
	 * 0 = default<br>
	 * 1 = pressed<br>
	 * 2 = hovered<br>
	 */
	public int state;
	public boolean enabled;
	
	public Component(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		state = 0;
		enabled = true;
	}
	
	public void drawTooltip(int x, int y, Graphics2D g)
	{}
	
	public boolean contains(int x, int y)
	{
		return new Rectangle(this.x, this.y, width, height).contains(x, y);
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (contains(e.getX(), e.getY())) state = 1;
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (contains(e.getX(), e.getY()) && enabled) state = 0;
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		state = contains(e.getX(), e.getY()) ? 2 : 0;
	}
}
