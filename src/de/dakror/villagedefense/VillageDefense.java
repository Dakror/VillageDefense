package de.dakror.villagedefense;

import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class VillageDefense
{
	public static boolean running = true;
	
	public static void main(String[] args)
	{
		new Game();
		
		while (running)
			Game.currentGame.draw();
	}
}
