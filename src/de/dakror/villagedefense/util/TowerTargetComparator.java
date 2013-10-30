package de.dakror.villagedefense.util;

import java.util.Comparator;

import de.dakror.villagedefense.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class TowerTargetComparator implements Comparator<Creature>
{
	@Override
	public int compare(Creature o1, Creature o2)
	{
		float distance1 = o1.getTarget2().getDistance(o1.getPos());
		float distance2 = o2.getTarget2().getDistance(o2.getPos());
		
		float dif = distance1 - distance2;
		if (dif < 0) return -1;
		else if (dif > 0) return 1;
		return 0;
	}
}
