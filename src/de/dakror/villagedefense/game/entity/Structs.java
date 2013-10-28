package de.dakror.villagedefense.game.entity;

import java.awt.geom.Rectangle2D;

/**
 * @author Dakror
 */
public enum Structs
{
	CORE_HOUSE(4, 0, 3, 5, new Rectangle2D.Float(0, 3, 3, 2)),
	TREE(0, 0, 4, 5, new Rectangle2D.Float(1.4f, 4.3f, 1, 0.6f)),
	
	;
	
	private int x, y, width, height;
	private Rectangle2D bump;
	
	private Structs(int x, int y, int width, int height, Rectangle2D bump)
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
	
	public Rectangle2D getBump()
	{
		return bump;
	}
}
