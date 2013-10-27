package de.dakror.villagedefense.settings;

import java.util.Arrays;

/**
 * @author Dakror
 */
public class CFG
{
	public static void p(Object... p)
	{
		if (p.length == 1) System.out.println(p[0]);
		else System.out.println(Arrays.toString(p));
	}
}
