package de.dakror.villagedefense.settings;

import java.awt.Point;

import de.dakror.villagedefense.settings.Resources.Resource;

public enum Researches
{
	TOWER_DOUBLESHOT("Pfeil-Turm Doppelschuss", new Resources().set(Resource.GOLD, 250).set(Resource.WOOD, 150), new Point(0, 0));
	private String name;
	private Resources costs;
	private Point texturePoint;
	
	private Researches(String name, Resources costs, Point texturePoint)
	{
		this.name = name;
		this.costs = costs;
		this.texturePoint = texturePoint;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Resources getCosts()
	{
		return costs;
	}
	
	public Point getTexturePoint()
	{
		return texturePoint;
	}
}
