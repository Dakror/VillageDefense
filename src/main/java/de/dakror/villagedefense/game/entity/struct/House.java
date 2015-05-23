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
 

package de.dakror.villagedefense.game.entity.struct;

import java.awt.geom.Rectangle2D;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.entity.creature.Forester;
import de.dakror.villagedefense.game.entity.creature.Villager;
import de.dakror.villagedefense.game.entity.creature.Woodsman;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class House extends Struct {
	public House(int x, int y) {
		super(x, y, 5, 5);
		tx = 0;
		ty = 5;
		setBump(new Rectangle2D.Float(0.25f, 2f, 4.5f, 3.1f));
		placeGround = true;
		name = "Haus";
		description = "Bietet Platz für einen Einwohner.";
		buildingCosts.set(Resource.GOLD, 50);
		buildingCosts.set(Resource.WOOD, 10);
	}
	
	@Override
	protected void onDeath() {
		for (Entity e : Game.world.entities) {
			
			if ((e instanceof Creature) && ((Creature) e).getOrigin() != null && ((Creature) e).getOrigin().equals(this)) {
				e.kill();
			}
		}
		dead = true;
	}
	
	@Override
	public void onSpawn(boolean initial) {
		if (initial) return;
		super.onSpawn(initial);
		Game.world.addEntity2(new Villager((int) x + 2 * Tile.SIZE, (int) y + 4 * Tile.SIZE).setOrigin(this), false);
	}
	
	@Override
	protected void onMinedUp() {}
	
	@Override
	public Entity clone() {
		return new House((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial) {
		if (research == Researches.HOUSE_FORESTER) {
			ty = 23;
			tx = 0;
			width = 5 * Tile.SIZE;
			height = 10 * Tile.SIZE;
			if (!inititial) y -= 5 * Tile.SIZE;
			setBump(new Rectangle2D.Float(0.25f, 7f, 4.5f, 3f));
			image = null;
			Game.world.addEntity2(new Forester((int) x + 2 * Tile.SIZE, (int) y + height - Tile.SIZE).setOrigin(this), false);
			canHunger = true;
		} else if (research == Researches.HOUSE_WOODSMAN) {
			Game.world.addEntity2(new Woodsman((int) x + 2 * Tile.SIZE, (int) y + height - Tile.SIZE).setOrigin(this), false);
			canHunger = true;
		}
	}
}
