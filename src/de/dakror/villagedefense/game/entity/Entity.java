package de.dakror.villagedefense.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import de.dakror.villagedefense.util.Drawable;

/**
 * @author Dakror
 */
public abstract class Entity implements Drawable
{
	protected int x, y, width, height;
	protected boolean hovered, clicked;
	protected Rectangle bump;
	
	public Entity(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		bump = new Rectangle();
	}
	
	public void drawBump(Graphics2D g, boolean above)
	{
		if (bump == null || (!hovered && !clicked)) return;
		
		Color oldColor = g.getColor();
		g.setColor(clicked ? Color.black : Color.darkGray);
		if (above)
		{
			g.drawLine(x + bump.x, y + bump.y, x + bump.x, y + bump.y + bump.height); // left
			g.drawLine(x + bump.x, y + bump.y + bump.height, x + bump.x + bump.width, y + bump.y + bump.height); // bottom
			g.drawLine(x + bump.x + bump.width, y + bump.y, x + bump.x + bump.width, y + bump.y + bump.height); // right
		}
		else
		{
			g.drawLine(x + bump.x, y + bump.y, x + bump.x + bump.width, y + bump.y); // top
		}
		g.setColor(oldColor);
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
	
	public void translate(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
	
	public Rectangle getBump(boolean includePos)
	{
		if (!includePos) return bump;
		else
		{
			Rectangle rect = (Rectangle) bump.clone();
			rect.translate(x, y);
			return rect;
		}
	}
	
	public void setBump(Rectangle r)
	{
		bump = r;
	}
	
	public boolean contains(int x, int y)
	{
		return x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height;
	}
	
	public boolean mouseMoved(MouseEvent e)
	{
		return hovered = contains(e.getXOnScreen(), e.getYOnScreen());
	}
	
	public boolean mousePressed(MouseEvent e)
	{
		return clicked = contains(e.getXOnScreen(), e.getYOnScreen()) && e.getButton() == MouseEvent.BUTTON1;
	}
	
	public void setClicked(boolean b)
	{
		clicked = b;
	}
	
	public void setHovered(boolean b)
	{
		hovered = b;
	}
}
