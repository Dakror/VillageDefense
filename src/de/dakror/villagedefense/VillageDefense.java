package de.dakror.villagedefense;

import java.io.File;

import javax.swing.UIManager;

import de.dakror.gamesetup.util.Helper;
import de.dakror.reporter.Reporter;
import de.dakror.universion.UniVersion;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.UpdateThread;
import de.dakror.villagedefense.settings.CFG;

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
		UniVersion.offline = !Helper.isInternetReachable();
		
		UniVersion.init(VillageDefense.class, CFG.VERSION, CFG.PHASE);
		if (!UniVersion.offline) Reporter.init(new File(CFG.DIR, "log"));
		
		new Game();
		Game.currentFrame.init("Village Defense");
		Game.currentFrame.setFullscreen();
		Game.currentFrame.updater = new UpdateThread();
		
		CFG.init();
		
		while (true)
			Game.currentFrame.main();
	}
}
