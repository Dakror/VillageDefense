package de.dakror.villagedefense.game.entity;

import java.awt.geom.Rectangle2D;

/**
 * @author Dakror
 */
public enum Structs
{
	CORE_HOUSE(4, 0, 3, 5, new Rectangle2D.Float(0.1f, 3, 2.8f, 2), true),
	TREE(0, 0, 4, 5, new Rectangle2D.Float(1.4f, 4.3f, 1, 0.6f), false),
	HOUSE(0, 5, 5, 5, new Rectangle2D.Float(0.25f, 2f, 4.5f, 3f), true),
	
	;
	
	private int x, y, width, height;
	private boolean placeGround;
	private Rectangle2D bump;
	
	private Structs(int x, int y, int width, int height, Rectangle2D bump, boolean placeGround)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bump = bump;
		this.placeGround = placeGround;
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
	
	public boolean isPlaceGround()
	{
		return placeGround;
	}
}
