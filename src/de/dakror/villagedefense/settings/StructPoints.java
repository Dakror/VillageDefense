package de.dakror.villagedefense.settings;

import java.util.ArrayList;

import de.dakror.villagedefense.util.Vector;


/**
 * @author Dakror
 */
public class StructPoints
{
	public ArrayList<Vector> entries = new ArrayList<>();
	public ArrayList<Vector> exits = new ArrayList<>();
	public ArrayList<Vector> attacks = new ArrayList<>();
	
	public StructPoints addEntries(Vector... points)
	{
		for (Vector p : points)
			entries.add(p);
		
		return this;
	}
	
	public StructPoints addExits(Vector... points)
	{
		for (Vector p : points)
			exits.add(p);
		
		return this;
	}
	
	public StructPoints addAttacks(Vector... points)
	{
		for (Vector p : points)
			attacks.add(p);
		
		return this;
	}
}
