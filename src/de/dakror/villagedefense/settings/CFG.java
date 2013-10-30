package de.dakror.villagedefense.settings;

import java.util.Arrays;

/**
 * @author Dakror
 */
public class CFG
{
	
	static long time = 0;
	
	// -- debug profiling -- //
	public static void u()
	{
		if (time == 0) time = System.currentTimeMillis();
		else
		{
			p(System.currentTimeMillis() - time);
			time = 0;
		}
	}
	
	public static void p(Object... p)
	{
		if (p.length == 1) System.out.println(p[0]);
		else System.out.println(Arrays.toString(p));
	}
}
