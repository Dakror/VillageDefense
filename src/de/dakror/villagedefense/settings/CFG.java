package de.dakror.villagedefense.settings;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class CFG
{
	public static final File DIR = new File(System.getProperty("user.home") + "/.dakror/VillageDefense");
	public static final int TICK_TIMEOUT = 33;
	
	// -- UniVersion -- //
	public static final int VERSION = 2013111923;
	public static final int PHASE = 1;
	
	static long time = 0;
	
	public static String USERNAME;
	
	public static void init()
	{
		try
		{
			DIR.mkdirs();
			new File(DIR, "saves").mkdir();
			
			File us = new File(DIR, "username");
			if (!us.exists() || us.length() == 0)
			{
				USERNAME = JOptionPane.showInputDialog("Bitte gib deinen Benutzernamen an.");
				
				if (USERNAME == null) System.exit(0);
				
				us.createNewFile();
				
				Assistant.setFileContent(us, USERNAME);
			}
			else
			{
				USERNAME = Assistant.getFileContent(us);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
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
