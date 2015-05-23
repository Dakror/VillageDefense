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
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.Tree;
import de.dakror.villagedefense.settings.Attributes.Attribute;

/**
 * @author Dakror
 */
public class Woodsman extends Creature {
	public Woodsman(int x, int y) {
		super(x, y, "forester");
		setHostile(false);
		name = "Holzfäller";
		attributes.set(Attribute.MINE_AMOUNT, 2);
		attributes.set(Attribute.MINE_SPEED, 15);
		
		description = "Fällt rasant Bäume.";
		canHunger = true;
	}
	
	@Override
	public void lookupTargetEntity() {
		Entity nearest = null;
		for (Entity e : Game.world.entities) {
			if (e instanceof Tree && ((Tree) e).isGrown()) {
				if (nearest == null || e.getCenter2().getDistance(getCenter2()) < nearest.getCenter2().getDistance(getCenter2())) nearest = e;
			}
		}
		
		if (nearest != null) setTarget(nearest, false);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		if (targetEntity instanceof Tree) {
			if ((tick + randomOffset) % attributes.get(Attribute.MINE_SPEED) == 0 && targetEntity.getResources().size() > 0) {
				if (frame % 2 == 0) ((Struct) targetEntity).mineAllResources((int) attributes.get(Attribute.MINE_AMOUNT), Game.currentGame.resources); // TODO: woodsman resource storage 'n' stuff
				frame++;
			}
			
			return true;
		}
		return false;
	}
	
	@Override
	public Entity clone() {
		return new Woodsman((int) x, (int) y);
	}
}
