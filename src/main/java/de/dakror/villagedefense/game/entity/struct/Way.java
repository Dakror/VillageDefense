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

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Way extends Struct {
    public Way(int x, int y) {
        super(x, y, 1, 1);
        tx = 6;
        ty = 7;

        name = "Path";

        buildingCosts.set(Resource.GOLD, 5);

        description = "Path which Villagers walk faster on.";
        setBump(new Rectangle2D.Float(0, 0, 1, 1));
    }

    @Override
    public void onSpawn(boolean initial) {
        if (!initial) Game.world.setTileId(Helper.round((int) x, Tile.SIZE) / Tile.SIZE, Helper.round((int) y, Tile.SIZE) / Tile.SIZE, Tile.way.getId());
        dead = true;
    }

    @Override
    public void initGUI() {}

    @Override
    protected void onMinedUp() {}

    @Override
    public void onUpgrade(Researches research, boolean initial) {}

    @Override
    public Entity clone() {
        return new Way((int) x / Tile.SIZE, (int) y / Tile.SIZE);
    }

    @Override
    protected void onDeath() {}

}
