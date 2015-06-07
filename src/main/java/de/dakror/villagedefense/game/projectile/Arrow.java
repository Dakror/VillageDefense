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


package de.dakror.villagedefense.game.projectile;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Arrow extends Projectile {
	Entity targetEntity;
	int damage;
	
	public Arrow(Vector pos, Entity target, float speed, int damage) {
		super(pos, "arrow", speed);
		targetEntity = target;
		this.target = target.getCenter2();
		this.damage = damage;
		rotate = true;
		canSetOnFire = false;
	}
	
	@Override
	protected void onImpact() {
		targetEntity.dealDamage(damage, this);
	}
	
	@Override
	protected Vector getTargetVector() {
		return targetEntity.getCenter2();
	}
}
