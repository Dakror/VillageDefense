package de.dakror.villagedefense;

import javax.swing.UIManager;

import de.dakror.dakrorbin.DakrorBin;
import de.dakror.dakrorbin.Launch;
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
		Launch.init(args);
		
		new Game();
		Game.currentFrame.init("Village Defense");
		
		DakrorBin.init(Game.w, "VillageDefense");
		
		Game.currentFrame.setFullscreen();
		Game.currentFrame.updater = new UpdateThread();
		
		CFG.init();
		
		while (true)
			Game.currentFrame.main();
	}
}
