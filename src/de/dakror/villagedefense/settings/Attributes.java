package de.dakror.villagedefense.settings;

import java.util.HashMap;

/**
 * @author Dakror
 */
public class Attributes
{
	public static enum Types
	{
		DAMAGE_CREATURE,
		DAMAGE_STRUCT,
		HEALTH,
		HEALTH_MAX,
		SPEED,
	}
	
	HashMap<Types, Float> attr = new HashMap<>();
	
	public Attributes()
	{
		for (Types t : Types.values())
			attr.put(t, 0f);
	}
	
	public float get(Types t)
	{
		return attr.get(t);
	}
	
	public Attributes set(Types t, float value)
	{
		attr.put(t, value);
		
		return this;
	}
}
