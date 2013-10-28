package de.dakror.villagedefense.game.entity;

/**
 * @author Dakror
 */
public enum Structs
{
	CORE_HOUSE(4, 0, 3, 5),
	
	;
	
	private int x, y, width, height;
	
	private Structs(int x, int y, int width, int height)
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
}
