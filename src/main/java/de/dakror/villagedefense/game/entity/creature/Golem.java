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
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Golem extends Creature {
	public Golem(int x, int y) {
		super(x, y, "golem");
		
		attributes.set(Attribute.DAMAGE_STRUCT, 20);
		attributes.set(Attribute.HEALTH, 150);
		attributes.set(Attribute.HEALTH_MAX, 150);
		attributes.set(Attribute.ATTACK_RANGE, Tile.SIZE);
		
		setHostile(true);
		
		name = "Golem";
		
		resources.set(Resource.GOLD, 30);
		
		description = "Ein Golem";
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		return false;
	}
	
	@Override
	public void dealDamage(int amount, Object source) {
		if (source instanceof Projectile) {
			Projectile p = (Projectile) source;
			if (p.getImageName().equals("arrow")) return;
		}
		
		super.dealDamage(amount, source);
	}
	
	@Override
	public Entity clone() {
		return new Golem((int) x, (int) y);
	}
}
