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

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Rock extends Struct {
	public Rock(int x, int y) {
		super(x, y, 1, 1);
		tx = 7;
		ty = 6;
		setBump(new Rectangle2D.Float(0, 0.5f, 1, 0.5f));
		placeGround = false;
		resources.set(Resource.STONE, 50);
		name = "Stein";
		description = "Ein Stein.";
	}
	
	@Override
	protected void onDeath() {
		dead = true;
	}
	
	@Override
	protected void onMinedUp() {
		onDeath();
	}
	
	@Override
	public Entity clone() {
		return new Rock((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	public void initGUI() {}
	
	@Override
	public void onUpgrade(Researches research, boolean inititial) {}
}
