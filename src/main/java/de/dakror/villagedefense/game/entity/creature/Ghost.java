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

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Ghost extends Creature {
	public Ghost(int x, int y) {
		super(x, y, "ghost");
		
		name = "Geist";
		setHostile(true);
		attributes.set(Attribute.SPEED, 5);
		attributes.set(Attribute.HEALTH, 8);
		attributes.set(Attribute.HEALTH_MAX, 8);
		attributes.set(Attribute.DAMAGE_STRUCT, 10);
		
		resources.add(Resource.GOLD, 2);
		
		description = "Ein Geist.";
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		return false;
	}
	
	@Override
	public Entity clone() {
		return new Ghost((int) x, (int) y);
	}
	
	@Override
	public void lookupTargetEntity() {
		setTarget(Game.world.core, false);
	}
}
