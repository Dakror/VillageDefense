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
import de.dakror.villagedefense.game.entity.struct.Warehouse;
import de.dakror.villagedefense.game.entity.struct.WheatField;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.game.world.Way;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;
import de.dakror.villagedefense.util.path.AStar;
import de.dakror.villagedefense.util.path.Path;

/**
 * @author Dakror
 */
public class Villager extends Creature {
	public Villager(int x, int y) {
		super(x, y, "villager" + (int) Math.round(Math.random()));
		setHostile(false);
		name = "Einwohner";
		attributes.set(Attribute.HEALTH, 15);
		attributes.set(Attribute.HEALTH_MAX, 15);
		attributes.set(Attribute.MINE_AMOUNT, 50); // transport capacity
		
		description = "Kann Rohstoffe sammeln, transportieren und Gebäuden arbeiten.";
		canHunger = false;
	}
	
	@Override
	public void tick(int tick) {
		super.tick(tick);
		
		if (getTileIdBelow() == Tile.way.getId()) attributes.set(Attribute.SPEED, Way.speed);
		else attributes.set(Attribute.SPEED, Attribute.SPEED.getDefaultValue());
		
		if (target == null && targetEntity == null && path == null) {
			Struct t = getMostImportantStructToClear();
			if (t != null) setTarget(t, true);
		}
	}
	
	public boolean isTargetingToCarry() {
		if (targetEntity == null) return false;
		return targetByUser && targetEntity instanceof Struct && targetEntity.getResources().size() > 0;
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
	public Entity clone() {
		return new Villager((int) x, (int) y);
	}
	
	@Override
	protected boolean onArrivalAtEntity(int tick) {
		if (targetEntity instanceof Struct) {
			if (((Struct) targetEntity).getBuildingCosts().get(Resource.PEOPLE) > 0 && !targetByUser) {
				alpha = 0;
			} else if (targetEntity.getResources().size() > 0) {
				if (!((Struct) targetEntity).isPlaceGround()) {
					if (targetEntity instanceof WheatField) return false; // can't mine those
					if ((tick + randomOffset) % targetEntity.getAttributes().get(Attribute.MINE_SPEED) == 0) {
						if (frame % 2 == 0) {
							((Struct) targetEntity).mineAllResources(1, Game.currentGame.resources);
						}
						frame++;
					}
				} else {
					((Struct) targetEntity).mineAllResources((int) attributes.get(Attribute.MINE_AMOUNT), resources);
					setTarget(getNearestWarehouse(), false);
				}
				
			} else if (targetEntity instanceof Warehouse) {
				Game.currentGame.resources.add(resources);
				resources = new Resources();
				frame++;
				targetEntity = null;
			}
			return true;
		}
		return false;
	}
	
	public Struct getMostImportantStructToClear() {
		Struct struct = null;
		float F = 0;
		
		for (Entity e : Game.world.entities) {
			if (e instanceof Struct && ((Struct) e).isPlaceGround() && e.getResources().size() > 0) {
				Struct s = (Struct) e;
				
				Path path = AStar.getPath(getTile(), Game.world.getTile(getTargetForStruct(s)));
				if (path == null) continue;
				
				path.mul(Tile.SIZE);
				path.translate(0, -bump.y + bump.height);
				
				if (s.getTargetedCarriers() * attributes.get(Attribute.MINE_AMOUNT) >= e.getResources().getLength()) continue;
				
				
				float myF = path.getLength() - (float) Math.pow(e.getResources().getLength(), 2) + (s.getTargetedCarriers() * attributes.get(Attribute.MINE_AMOUNT));
				// CFG.p(e.getClass(), myF);
				if (struct == null || myF < F) {
					F = myF;
					struct = (Struct) e;
				}
			}
		}
		
		// if (struct != null) CFG.p(struct.getClass());
		
		return struct;
	}
	
	public Struct getNearestWarehouse() {
		Struct nearest = null;
		float distance = 0;
		
		for (Entity e : Game.world.entities) {
			if (e instanceof Warehouse) {
				Path path = AStar.getPath(getTile(), Game.world.getTile(getTargetForStruct((Struct) e)));
				if (path == null) continue;
				
				path.mul(Tile.SIZE);
				path.translate(0, -bump.y + bump.height);
				
				if (distance == 0 || path.getLength() < distance) {
					distance = path.getLength();
					nearest = (Struct) e;
				}
			}
		}
		
		return nearest;
	}
}
