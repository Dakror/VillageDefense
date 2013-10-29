package de.dakror.villagedefense.util;


public class Vector
{
	public float x;
	public float y;
	
	public Vector(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector add(Vector o)
	{
		x += o.x;
		y += o.y;
		
		return this;
	}
	
	public Vector sub(Vector o)
	{
		x -= o.x;
		y -= o.y;
		
		return this;
	}
	
	public float getLength()
	{
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public void setLength(float length)
	{
		float fac = length / getLength();
		x *= fac;
		y *= fac;
	}
	
	@Override
	public Vector clone()
	{
		return new Vector(x, y);
	}
	
	public void normalize()
	{
		setLength(1);
	}
	
	@Override
	public String toString()
	{
		return "[x=" + x + ", y=" + y + "]";
	}
}
