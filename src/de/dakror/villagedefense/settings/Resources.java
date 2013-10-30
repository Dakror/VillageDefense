package de.dakror.villagedefense.settings;

import java.util.HashMap;


/**
 * @author Dakror
 */
public class Resources
{
	public enum Resource
	{
		GOLD("Gold", 13, 12),
		WOOD("Holz", 0, 12),
		STONE("Stein", 2, 9),
		PEOPLE("Einwohner", 9, 8),
		
		;
		
		private String name;
		private int iconX, iconY;
		
		private Resource(String name, int iconX, int iconY)
		{
			this.name = name;
			this.iconX = iconX;
			this.iconY = iconY;
		}
		
		public String getName()
		{
			return name;
		}
		
		public int getIconX()
		{
			return iconX;
		}
		
		public int getIconY()
		{
			return iconY;
		}
	}
	
	HashMap<Resource, Integer> res = new HashMap<>();
	
	public Resources()
	{
		for (Resource t : Resource.values())
			res.put(t, 0);
	}
	
	public int get(Resource t)
	{
		return res.get(t);
	}
	
	public void set(Resource t, int value)
	{
		res.put(t, value);
	}
	
	public void add(Resource t, int value)
	{
		res.put(t, get(t) + value);
	}
}
