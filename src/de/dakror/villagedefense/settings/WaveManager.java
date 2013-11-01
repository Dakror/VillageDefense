package de.dakror.villagedefense.settings;

import de.dakror.villagedefense.game.Game;
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
	
	public static void init()
	{
		wave = 0;
		zombies = 1;
		generateNextWave();
	}
	
	static void generateNextWave()
	{
		Game.world.getCoreHouse().dealDamage(-1);
		zombies = (int) Math.ceil(wave * wave + 3);
		nextWave = System.currentTimeMillis() + (1000 * 60 * 1); // for now each wave is 1 minutes from now
		wave++;
	}
	
	public static void update()
	{
		if (System.currentTimeMillis() > nextWave)
		{
			for (int i = 0; i < zombies; i++)
			{
				boolean left = Math.random() < 0.5;
				Game.world.addEntity(new Zombie(left ? -i * Tile.SIZE * 2 : Game.getWidth() + i * Tile.SIZE, Game.world.height / 2 - Tile.SIZE));
			}
			generateNextWave();
		}
	}
}
