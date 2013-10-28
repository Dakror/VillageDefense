package de.dakror.villagedefense.game.entity;

import de.dakror.villagedefense.util.Drawable;

/**
 * @author Dakror
 */
public abstract class Entity implements Drawable
{
	protected int x, y, width, height, bumpWidth, bumpHeight;
	
	public Entity(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
	
	public int getBumpWidth()
	{
		return bumpWidth;
	}
	
	public void setBumpWidth(int bumpWidth)
	{
		this.bumpWidth = bumpWidth;
	}
	
	public int getBumpHeight()
	{
		return bumpHeight;
	}
	
	public void setBumpHeight(int bumpHeight)
	{
		this.bumpHeight = bumpHeight;
	}
	
	public void translate(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
}
