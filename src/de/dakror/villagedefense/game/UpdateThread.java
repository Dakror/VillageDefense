package de.dakror.villagedefense.game;






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
