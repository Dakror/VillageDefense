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


package de.dakror.villagedefense.game.entity.creature;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Troll extends Creature {
	public Troll(int x, int y) {
		super(x, y, "troll");
		
		name = "Troll";
		attributes.set(Attribute.HEALTH, 500);
		attributes.set(Attribute.HEALTH_MAX, 500);
		attributes.set(Attribute.DAMAGE_STRUCT, 25);
		attributes.set(Attribute.SPEED, 0.6f);
		attributes.set(Attribute.ATTACK_RANGE, Tile.SIZE);
		
		setHostile(true);
		
		resources.set(Resource.GOLD, 150);
		
		description = "Ein Troll.";
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		return false;
	}
	
	@Override
	public Entity clone() {
		return new Troll((int) x, (int) y);
	}
}
