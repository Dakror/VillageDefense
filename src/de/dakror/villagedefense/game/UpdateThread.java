package de.dakror.villagedefense.game;

import de.dakror.villagedefense.ui.Component;

/**
 * @author Dakror
 */
public class UpdateThread extends Thread
{
	int tick;
	long time;
	
	public UpdateThread()
	{
		start();
	}
	
	@Override
	public void run()
	{
		tick = 0;
		time = System.currentTimeMillis();
		while (true)
		{
			if (tick == Integer.MAX_VALUE) tick = 0;
			
			if (Game.world != null && Game.currentGame.state == 0) Game.world.update(tick);
			
			for (Component c : Game.currentGame.components)
			{
				c.update(tick);
			}
			
			try
			{
				tick++;
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{}
		}
	}
}
