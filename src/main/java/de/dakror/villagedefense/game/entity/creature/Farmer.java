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

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.struct.Struct;
import de.dakror.villagedefense.game.entity.struct.WheatField;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Farmer extends Creature {
	int range;
	
	public Farmer(int x, int y) {
		super(x, y, "farmer");
		
		setHostile(false);
		name = "Bauer";
		
		attributes.set(Attribute.ATTACK_SPEED, 30 * 5); // 5 second plant cooldown
		
		range = 6 * Tile.SIZE; // 5 fields plant radius
		
		attributes.set(Attribute.MINE_AMOUNT, 1);
		
		description = "Pflanzt und erntet Weizenfelder um sein Haus herum.";
	}
	
	@Override
	public void tick(int tick) {
		super.tick(tick);
		
		if (target == null && targetEntity == null && path == null) {
			if ((tick + randomOffset) % attributes.get(Attribute.ATTACK_SPEED) == 0) {
				
				if (!getPos().equals(new Vector(spawnPoint))) Game.world.addEntity2(new WheatField(Math.round(getPos().x / Tile.SIZE), Math.round(getPos().y / Tile.SIZE) + 1), false);
				setTarget(lookupPlantTarget(), false);
			} else {
				setTarget(lookupHarvestTarget(), false);
			}
		}
	}
	
	public Entity lookupHarvestTarget() {
		for (Entity e : Game.world.entities) {
			if (e instanceof WheatField && e.getCenter().getDistance(origin.getCenter2()) <= range && ((WheatField) e).getPhase() == 3) {
				return e;
			}
		}
		
		return null;
	}
	
	public Vector lookupPlantTarget() {
		float rad = (float) Math.toRadians(Math.random() * 360);
		float hyp = (float) Math.random() * range - Tile.SIZE;
		
		int x = Helper.round(Math.round(origin.getCenter2().x + (float) Math.cos(rad) * hyp), Tile.SIZE);
		int y = Helper.round(Math.round(origin.getCenter2().y + (float) Math.sin(rad) * hyp), Tile.SIZE);
		int my = Helper.round(Game.world.height / 2, Tile.SIZE);
		
		if (x < 0 || y < 100 || x >= Game.world.width || y >= Game.world.height - 120 || y == my || y == my + Tile.SIZE) return lookupPlantTarget();
		
		if (Game.world.isFreeTile(x, y)) return new Vector(x, y);
		else return lookupPlantTarget();
	}
	
	@Override
	public void onSpawn(boolean initial) {
		Game.currentGame.resources.add(Resource.PEOPLE, 1);
	}
	
	@Override
	public void onDeath() {
		super.onDeath();
		Game.currentGame.resources.add(Resource.PEOPLE, -1);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		if (targetEntity instanceof WheatField) {
			if ((tick + randomOffset) % targetEntity.getAttributes().get(Attribute.MINE_SPEED) == 0 && targetEntity.getResources().size() > 0) {
				if (frame % 2 == 0) ((Struct) targetEntity).mineAllResources((int) attributes.get(Attribute.MINE_AMOUNT), origin.getResources());
				frame++;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public Entity clone() {
		return new Farmer((int) x, (int) y);
	}
}
