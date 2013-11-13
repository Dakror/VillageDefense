package de.dakror.villagedefense.game;

import de.dakror.villagedefense.layer.Layer;
import de.dakror.villagedefense.settings.CFG;
import de.dakror.villagedefense.settings.WaveManager;

/**
 * @author Dakror
 */
public class UpdateThread extends Thread
{
	public int tick, ticks;
	long time, time2;
	
	public int speed = 1;
	
	public boolean closeRequested = false;
	
	public UpdateThread()
	{
		setPriority(Thread.MAX_PRIORITY);
		start();
	}
	
	@Override
	public void run()
	{
		tick = 0;
		time = time2 = System.currentTimeMillis();
		while (!closeRequested)
		{
			if (tick == Integer.MAX_VALUE) tick = 0;
			
			if (System.currentTimeMillis() - time2 > 1000 / speed && Game.currentGame.state == 0)
			{
				if (WaveManager.nextWave > 0) WaveManager.nextWave--;
				time2 = System.currentTimeMillis();
			}
			
			WaveManager.update();
			
			if (Game.currentGame.fade == true)
			{
				if (Game.currentGame.alpha != Game.currentGame.fadeTo)
				{
					float dif = Game.currentGame.fadeTo - Game.currentGame.alpha;
					Game.currentGame.alpha += dif > 0 ? (dif > Game.currentGame.speed ? Game.currentGame.speed : dif) : (dif < -Game.currentGame.speed ? -Game.currentGame.speed : dif);
				}
				else Game.currentGame.fade = false;
			}
			
			for (Layer l : Game.currentGame.layers)
				l.update(tick);
			
			if (Game.world != null && Game.currentGame.state == 0) Game.world.update(tick);
			
			try
			{
				tick++;
				ticks++;
				Thread.sleep(Math.round(CFG.TICK_TIMEOUT / (float) speed));
			}
			catch (InterruptedException e)
			{}
		}
	}
}
