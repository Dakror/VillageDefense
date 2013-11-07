package de.dakror.villagedefense.settings;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author Dakror
 */
public class Resources
{
	public enum Resource
	{
		GOLD("Gold", 13, 12, 1, true),
		WOOD("Holz", 0, 12, 6, true),
		STONE("Stein", 2, 9, 4, true),
		PEOPLE("Einwohner", 9, 8, 0, false),
		
		;
		
		private String name;
		private boolean usable;
		private int iconX, iconY, goldValue;
		
		private Resource(String name, int iconX, int iconY, int goldValue, boolean usable)
		{
			this.name = name;
			this.iconX = iconX;
			this.iconY = iconY;
			this.goldValue = goldValue;
			this.usable = usable;
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
		
		public int getGoldValue()
		{
			return goldValue;
		}
		
		public boolean isUsable()
		{
			return usable;
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
	
	public Resources set(Resource t, int value)
	{
		res.put(t, value);
		
		return this;
	}
	
	public void add(Resource t, int value)
	{
		res.put(t, get(t) + value);
	}
	
	public void add(Resources r)
	{
		for (Resource s : r.getFilled())
			add(s, r.get(s));
	}
	
	public int size()
	{
		int s = 0;
		
		for (Resource r : res.keySet())
			if (res.get(r) != 0) s++;
		
		return s;
	}
	
	public ArrayList<Resource> getFilled()
	{
		ArrayList<Resource> res = new ArrayList<>();
		
		for (Resource r : Resource.values())
			if (get(r) != 0) res.add(r);
		
		return res;
	}
	
	public static Resources mul(Resources res, int f)
	{
		Resources result = new Resources();
		for (Resource r : res.getFilled())
			result.set(r, res.get(r) * f);
		
		return result;
	}
}
