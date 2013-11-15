package de.dakror.villagedefense.settings;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Dakror
 */
public class Attributes
{
	/**
	 * Attributes about speed are expressed as ticks
	 * 
	 * @author Dakror
	 */
	public enum Attribute
	{
		ATTACK_RANGE(0),
		ATTACK_SPEED(25),
		DAMAGE_CREATURE(0),
		DAMAGE_STRUCT(0),
		HEALTH(1),
		HEALTH_MAX(1),
		MINE_SPEED(25),
		MINE_AMOUNT(1),
		SPEED(2),
		
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
	
	public int size()
	{
		int s = 0;
		
		for (Attribute r : attr.keySet())
			if (attr.get(r) != 0) s++;
		
		return s;
	}
	
	public JSONObject getData()
	{
		JSONObject o = new JSONObject();
		try
		{
			for (Attribute a : Attribute.values())
				o.put(a.name(), get(a));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return o;
	}
	
	@Override
	public String toString()
	{
		return attr.toString();
	}
}
