package de.dakror.villagedefense.settings;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.creature.Skeleton;
import de.dakror.villagedefense.game.entity.creature.Zombie;
import de.dakror.villagedefense.game.world.Tile;

/**
 * @author Dakror
 */
public class WaveManager
{
	public static int wave;
	public static long nextWave; // UNIX timestamp
	
	public static int zombies;
	public static int skeletons;
	
	public static void init()
	{
		wave = 0;
		zombies = 1;
		generateNextWave();
	}
	
	static void generateNextWave()
	{
		Game.world.getCoreHouse().dealDamage(-1);
		zombies = (int) Math.ceil(0.2f * wave * wave + 3);
		skeletons = (wave > 5) ? (int) Math.ceil(Math.random() * (wave - 5)) + 1 : 0;
		nextWave = System.currentTimeMillis() + (1000 * 10 * 1); // for now each wave is 1 minutes from now
		wave++;
	}
	
	public static void update()
	{
		if (System.currentTimeMillis() >= nextWave)
		{
			if (nextWave > 0)
			{
				new Thread()
				{
					@Override
					public void run()
					{
						int leftLength = 0;
						int rightLength = 0;
						
						for (int i = 0; i < zombies; i++)
						{
							boolean left = Math.random() < 0.5;
							
							int x = left ? -leftLength * Tile.SIZE : Game.getWidth() + rightLength * Tile.SIZE;
							
							Game.world.addEntity2(new Zombie(x, Game.world.height / 2 - Tile.SIZE));
							
							if (left) leftLength++;
							else rightLength++;
						}
						for (int i = 0; i < skeletons; i++)
						{
							boolean left = Math.random() < 0.5;
							
							int x = left ? -leftLength * Tile.SIZE : Game.getWidth() + rightLength * Tile.SIZE;
							
							Game.world.addEntity2(new Skeleton(x, Game.world.height / 2 - Tile.SIZE));
							
							if (left) leftLength++;
							else rightLength++;
						}
					}
				}.start();
			}
			generateNextWave();
		}
	}
}
