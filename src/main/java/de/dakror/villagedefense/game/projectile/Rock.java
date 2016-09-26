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

import java.awt.Shape;
import java.awt.geom.Arc2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.animation.Animation;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Rock extends Projectile {
	Vector t;
	int damage;
	int radius;
	
	public Rock(Vector pos, Vector target, float speed, int damage, int radius) {
		super(pos, "rock", speed);
		t = target;
		rotate = false;
		this.radius = radius;
		this.damage = damage;
		canSetOnFire = true;
	}
	
	@Override
	protected Vector getTargetVector() {
		return t;
	}
	
	@Override
	protected void onImpact() {
		Game.world.addAnimation(new Animation(Math.round(getPos().x - radius), Math.round(getPos().y - radius) - (onFire ? Tile.SIZE : 0), Math.round(radius * 2), 2, onFire ? "rock_impact_fire" : "rock_impact"));
		Shape s = new Arc2D.Float(getPos().x - radius, getPos().y - radius, radius * 2, radius * 2, 0, 360, Arc2D.OPEN);
		for (Entity e : Game.world.entities) {
			if (e instanceof Creature) {
				Creature c = (Creature) e;
				if (c.isHostile() && s.intersects(c.getBump(true))) c.dealDamage(damage, this);
			}
		}
	}
}
