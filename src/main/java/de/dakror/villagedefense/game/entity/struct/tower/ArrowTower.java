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


package de.dakror.villagedefense.game.entity.struct.tower;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class ArrowTower extends Tower {
	public ArrowTower(int x, int y) {
		super(x, y);
		
		name = "Pfeil-Turm";
		color = 0;
		spheres = 1;
		attributes.set(Attribute.ATTACK_RANGE, Tile.SIZE * 5);
		attributes.set(Attribute.ATTACK_SPEED, 30);
		attributes.set(Attribute.DAMAGE_CREATURE, 3);
		
		buildingCosts.set(Resource.GOLD, 75);
		buildingCosts.set(Resource.STONE, 15);
		
		researchClass = Tower.class;
		
		description = "Schießt mit Pfeilen auf Monster.";
	}
	
	@Override
	public Entity clone() {
		return new ArrowTower((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
}
