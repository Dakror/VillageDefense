package de.dakror.villagedefense.settings;

import de.dakror.villagedefense.settings.Resources.Resource;

public enum Researches
{
	TOWER_DOUBLESHOT("Pfeil-Turm Doppelschuss", new Resources().set(Resource.GOLD, 250).set(Resource.WOOD, 150));
	private String name;
	private Resources costs;
	
	private Researches(String name, Resources costs)
	{
		this.name = name;
		this.costs = costs;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Resources getCosts()
	{
		return costs;
	}
	
	
}
