/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.villagedefense.util;

import java.util.Comparator;

import de.dakror.villagedefense.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class TowerTargetComparator implements Comparator<Creature> {
	@Override
	public int compare(Creature o1, Creature o2) {
		float distance1 = o1.getTarget2().getDistance(o1.getPos());
		float distance2 = o2.getTarget2().getDistance(o2.getPos());
		
		float dif = distance1 - distance2;
		if (dif < 0) return -1;
		else if (dif > 0) return 1;
		return 0;
	}
}
