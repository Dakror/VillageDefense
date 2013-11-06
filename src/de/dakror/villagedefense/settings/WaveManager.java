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
		TROLL("troll", de.dakror.villagedefense.game.entity.creature.Troll.class), ;
		
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
	public static int nextWave; // int seconds
	
	public static EnumMap<Monster, Integer> monsters = new EnumMap<>(Monster.class);
	
	public static void init()
	{
		wave = 0;
		generateNextWave();
	}
	
	static void generateNextWave()
	{
		Game.world.core.dealDamage(-1);
		monsters.clear();
		
		/*
		 * either parabola: 0.075 * wave² + 3
		 * or line: 2 * wave + 3
		 */
		monsters.put(Monster.ZOMBIE, Math.round(2 * wave + 3));
		if (wave > 5) monsters.put(Monster.SKELETON, (int) Math.ceil(Math.random() * (wave - 5)) + 1);
		
		if (wave == 9)
		{
			monsters.clear();
			monsters.put(Monster.TROLL, 1);
		}
		
		nextWave = 60; // in seconds
		wave++;
	}
	
	public static void update()
	{
		if (nextWave <= 0)
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
					
					int space = Tile.SIZE * 2;
					
					for (Monster monster : monsters.keySet())
					{
						for (int i = 0; i < monsters.get(monster); i++)
						{
							try
							{
								boolean left = Math.random() < 0.5;
								
								int x = left ? -leftLength * space : Game.getWidth() + rightLength * space;
								Entity e = (Entity) monster.getCreatureClass().getConstructor(int.class, int.class).newInstance(x, 0);
								int y = Game.world.height / 2 - e.getHeight() / 2;
								e.setY(y);
								
								Game.world.addEntity2(e);
								
								if (left) leftLength++;
								else rightLength++;
							}
							catch (Exception e1)
							{
								e1.printStackTrace();
							}
						}
					}
					
				}
			}.start();
		}
	}
}
