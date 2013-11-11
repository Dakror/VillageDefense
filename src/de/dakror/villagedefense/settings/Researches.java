package de.dakror.villagedefense.settings;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dakror.villagedefense.game.entity.struct.House;
import de.dakror.villagedefense.game.entity.struct.Mine;
import de.dakror.villagedefense.game.entity.struct.tower.Tower;
import de.dakror.villagedefense.settings.Resources.Resource;

public enum Researches
{
	TOWER_DOUBLESHOT("Pfeil-Turm Doppelschuss", new Resources().set(Resource.GOLD, 250).set(Resource.WOOD, 150), new Point(0, 0), 5, Tower.class),
	HOUSE_FORESTER("Forsthaus", new Resources().set(Resource.GOLD, 150).set(Resource.WOOD, 150), new Point(1, 0), 1, House.class),
	MINE_STONE("Stein-Abbau", new Resources(), new Point(2, 0), 1, Mine.class),
	MINE_IRON("Eisenerz-Abbau", new Resources().set(Resource.GOLD, 200).set(Resource.STONE, 100), new Point(0, 1), -1, Mine.class),
	
	;
	
	private String name;
	private Resources costs;
	private Point texturePoint;
	private List<Class<?>> targetClasses;
	/**
	 * -1 = discounted version is free
	 */
	private float buyDiscount;
	
	private Researches(String name, Resources costs, Point texturePoint, float buyDiscount, Class<?>... targetClasses)
	{
		this.name = name;
		this.costs = costs;
		this.texturePoint = texturePoint;
		this.targetClasses = Arrays.asList(targetClasses);
		this.buyDiscount = buyDiscount;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Resources getCosts(boolean discounted)
	{
		if (!discounted) return costs;
		
		if (buyDiscount == -1) return new Resources();
		
		Resources r = new Resources();
		for (Resource res : costs.getFilled())
			r.set(res, Math.round(costs.get(res) / buyDiscount));
		return r;
	}
	
	public Point getTexturePoint()
	{
		return texturePoint;
	}
	
	public boolean isTarget(Class<?> targetClass)
	{
		return targetClasses.contains(targetClass);
	}
	
	public float getBuyDiscount()
	{
		return buyDiscount;
	}
	
	public static Researches[] values(Class<?> targetClass)
	{
		ArrayList<Researches> res = new ArrayList<>();
		for (Researches r : values())
		{
			if (r.isTarget(targetClass)) res.add(r);
		}
		
		return res.toArray(new Researches[] {});
	}
}
