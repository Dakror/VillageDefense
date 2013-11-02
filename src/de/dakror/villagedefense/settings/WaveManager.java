package de.dakror.villagedefense.settings;

import java.util.EnumMap;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;

/**
 * @author Dakror
 */
public class WaveManager
{
	public enum Monster
	{
		ZOMBIE("zombie", de.dakror.villagedefense.game.entity.creature.Zombie.class),
		GHOST("ghost", de.dakror.villagedefense.game.entity.creature.Ghost.class),
		SKELETON("skeleton", de.dakror.villagedefense.game.entity.creature.Skeleton.class),
		
		;
		
		private String image;
		private Class<?> creatureClass;
		
		private Monster(String image, Class<?> creatureClass)
		{
			this.image = image;
			this.creatureClass = creatureClass;
		}
		
		public String getImage()
		{
			return image;
		}
		
		public Class<?> getCreatureClass()
		{
			return creatureClass;
		}
	}
	
	public static int wave;
	public static long nextWave; // UNIX timestamp
	
	public static EnumMap<Monster, Integer> monsters = new EnumMap<>(Monster.class);
	
	public static void init()
	{
		wave = 0;
		generateNextWave();
	}
	
	static void generateNextWave()
	{
		Game.world.getCoreHouse().dealDamage(-1);
		monsters.clear();
		
		/*
		 * either parabola: 0.075 * wave² + 3
		 * or line: 2 * wave + 3
		 */
		monsters.put(Monster.ZOMBIE, Math.round(2 * wave + 3));
		if (wave > 5) monsters.put(Monster.SKELETON, (int) Math.ceil(Math.random() * (wave - 5)) + 1);
		
		
		nextWave = System.currentTimeMillis() + (1000 * 60 * 1); // for now each wave is 1 minutes from now
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
						EnumMap<Monster, Integer> monsters = WaveManager.monsters.clone();
						generateNextWave();
						
						int leftLength = 0;
						int rightLength = 0;
						
						for (Monster monster : monsters.keySet())
						{
							for (int i = 0; i < monsters.get(monster); i++)
							{
								boolean left = Math.random() < 0.5;
								
								int x = left ? -leftLength * Tile.SIZE : Game.getWidth() + rightLength * Tile.SIZE;
								
								try
								{
									Game.world.addEntity2((Entity) monster.getCreatureClass().getConstructor(int.class, int.class).newInstance(x, Game.world.height / 2 - Tile.SIZE));
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								
								if (left) leftLength++;
								else rightLength++;
							}
						}
						
					}
				}.start();
			}
			
		}
	}
}
