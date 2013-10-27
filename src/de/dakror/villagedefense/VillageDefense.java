package de.dakror.villagedefense;

import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class VillageDefense
{
	
	public static void main(String[] args)
	{
		new Game();
		
		while (Game.w.isVisible())
			Game.currentGame.draw();
	}
}
