package de.dakror.villagedefense.game;



/**
 * @author Dakror
 */
public class UpdateThread extends Thread
{
	public UpdateThread()
	{
		start();
	}
	
	@Override
	public void run()
	{
		while (Game.w.isVisible())
		{
			// update content
			
			try
			{
				Thread.sleep(50);
			}
			catch (InterruptedException e)
			{}
		}
	}
}
