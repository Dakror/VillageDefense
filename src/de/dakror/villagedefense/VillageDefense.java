package de.dakror.villagedefense;

import javax.swing.UIManager;

import de.dakror.villagedefense.game.Game;
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
		
		new Game();
		
		CFG.init();
		
		while (true)
			Game.currentGame.draw();
	}
}
