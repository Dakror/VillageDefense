package de.dakror.villagedefense.game;

import de.dakror.villagedefense.settings.CFG;
import de.dakror.villagedefense.settings.WaveManager;
import de.dakror.villagedefense.ui.BuildButton;
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
		setPriority(Thread.MAX_PRIORITY);
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
			
			WaveManager.update();
			
			for (int i = 0; i < Game.buildableStructs.length; i++)
			{
				int x = Game.getWidth() / 2 + BuildButton.SIZE / 4 - (Game.buildableStructs.length * (BuildButton.SIZE + 32)) / 2 + i * (BuildButton.SIZE + 32);
				Game.currentGame.components.get(i).setX(x);
				Game.currentGame.components.get(i).setY(Game.getHeight() - 84);
			}
			
			if (Game.world != null && Game.currentGame.state == 0) Game.world.update(tick);
			
			for (Component c : Game.currentGame.components)
			{
				c.update(tick);
			}
			
			try
			{
				tick++;
				Thread.sleep(CFG.TICK_TIMEOUT);
			}
			catch (InterruptedException e)
			{}
		}
	}
}
