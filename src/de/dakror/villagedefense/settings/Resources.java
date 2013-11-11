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
		GOLD("Gold", 2, 2, 1, true),
		PEOPLE("Einwohner", 0, 0, 0, false),
		WOOD("Holz", 3, 0, 4, true),
		PLANKS("Bretter", 2, 0, 6, true),
		STONE("Stein", 1, 1, 2, true),
		COAL("Kohle", 3, 1, 5, true),
		IRONORE("Eisenerz", 0, 1, 3, true),
		// GOLDORE("Golderz", 2, 1, 4, true),
		IRONINGOT("Eisen", 0, 2, 7, true),
		// GOLDINGOT("Gold", 1, 2, 8, true),
		
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
		
		public static Resource[] usable()
		{
			ArrayList<Resource> res = new ArrayList<>();
			
			for (Resource r : values())
				if (r.isUsable()) res.add(r);
			
			return res.toArray(new Resource[] {});
		}
		
		public static Resource[] usableNoGold()
		{
			ArrayList<Resource> res = new ArrayList<>();
			
			for (Resource r : values())
				if (r.isUsable() && r != GOLD) res.add(r);
			
			return res.toArray(new Resource[] {});
		}
	}
	
	HashMap<Resource, Float> res = new HashMap<>();
	
	public Resources()
	{
		for (Resource t : Resource.values())
			res.put(t, 0f);
	}
	
	public int get(Resource t)
	{
		return (int) (float) res.get(t);
	}
	
	public float getF(Resource t)
	{
		return res.get(t);
	}
	
	public Resources set(Resource t, int value)
	{
		return set(t, (float) value);
	}
	
	public Resources set(Resource t, float value)
	{
		res.put(t, value);
		
		return this;
	}
	
	public void add(Resource t, int value)
	{
		add(t, (float) value);
	}
	
	public void add(Resource t, float value)
	{
		res.put(t, getF(t) + value);
	}
	
	public void add(Resources r)
	{
		for (Resource s : r.getFilled())
			add(s, r.getF(s));
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
			if (getF(r) != 0) res.add(r);
		
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
