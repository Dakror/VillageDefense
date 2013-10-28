package de.dakror.villagedefense.game.entity;

import java.awt.Rectangle;

/**
 * @author Dakror
 */
public enum Structs
{
	CORE_HOUSE(4, 0, 3, 5, new Rectangle(0, 3, 3, 2)),
	
	;
	
	private int x, y, width, height;
	private Rectangle bump;
	
	private Structs(int x, int y, int width, int height, Rectangle bump)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bump = bump;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public Rectangle getBump()
	{
		return bump;
	}
}
