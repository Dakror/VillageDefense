package de.dakror.villagedefense;

import java.io.File;

import javax.swing.UIManager;

import de.dakror.reporter.Reporter;
import de.dakror.universion.UniVersion;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.settings.CFG;
import de.dakror.villagedefense.util.Assistant;

/**
 * @author Dakror
 */
public class VillageDefense
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		UniVersion.offline = !Assistant.isInternetReachable();
		
		UniVersion.init(VillageDefense.class, CFG.VERSION - 1, CFG.PHASE);
		if (!UniVersion.offline) Reporter.init(new File(CFG.DIR, "log"));
		
		new Game();
		
		CFG.init();
		
		while (true)
			Game.currentGame.draw();
	}
}
