package de.dakror.villagedefense.game;




/**
 * @author Dakror
 */
public class UpdateThread extends Thread
{
	int frames;
	long time;
	
	public UpdateThread()
	{
		start();
	}
	
	@Override
	public void run()
	{
		frames = 0;
		time = System.currentTimeMillis();
		while (Game.w.isVisible())
		{
			// update content
			
			if (Game.currentGame.world != null) Game.currentGame.world.update();
			
			try
			{
				frames++;
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{}
		}
	}
}
