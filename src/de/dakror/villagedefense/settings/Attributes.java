package de.dakror.villagedefense.settings;

import java.util.HashMap;

/**
 * @author Dakror
 */
public class Attributes
{
	public static enum Attribute
	{
		ATTACK_RANGE(0),
		ATTACK_SPEED(25), // in ticks
		DAMAGE_CREATURE(0),
		DAMAGE_STRUCT(0),
		HEALTH(1),
		HEALTH_MAX(1),
		SPEED(1),
		
		;
		
		private float defaultValue;
		
		private Attribute(float defaultValue)
		{
			this.defaultValue = defaultValue;
		}
		
		public float getDefaultValue()
		{
			return defaultValue;
		}
	}
	
	HashMap<Attribute, Float> attr = new HashMap<>();
	
	public Attributes()
	{
		for (Attribute t : Attribute.values())
			attr.put(t, t.getDefaultValue());
	}
	
	public float get(Attribute t)
	{
		return attr.get(t);
	}
	
	public Attributes set(Attribute t, float value)
	{
		attr.put(t, value);
		
		return this;
	}
	
	public Attributes add(Attribute t, float value)
	{
		attr.put(t, get(t) + value);
		
		return this;
	}
}
