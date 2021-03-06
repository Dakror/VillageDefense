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
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Bakery extends Struct {
    public Bakery(int x, int y) {
        super(x, y, 4, 7);
        tx = 5;
        ty = 23;
        placeGround = true;
        name = "Bakery";

        description = "Bakes bread made from flour.";

        buildingCosts.set(Resource.GOLD, 350);
        buildingCosts.set(Resource.COAL, 25);
        buildingCosts.set(Resource.WOOD, 100);
        buildingCosts.set(Resource.STONE, 250);
        buildingCosts.set(Resource.IRONINGOT, 15);
        buildingCosts.set(Resource.PEOPLE, 2);

        attributes.set(Attribute.MINE_SPEED, 100);
        attributes.set(Attribute.MINE_AMOUNT, 2); // use 2 coal + 3 flour -> 2 bread

        canHunger = true;
        setBump(new Rectangle2D.Float(0, 3, 3.1f, 4));
    }

    @Override
    public Resources getResourcesPerSecond() {
        Resources res = new Resources();

        if (!working) return res;

        res.set(Resource.COAL, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * -2);
        res.set(Resource.FLOUR, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * -3);
        res.set(Resource.BREAD, Game.currentGame.getUPS2() / attributes.get(Attribute.MINE_SPEED) * attributes.get(Attribute.MINE_AMOUNT));

        return res;
    }

    @Override
    protected void tick(int tick) {
        super.tick(tick);

        if (tick % attributes.get(Attribute.MINE_SPEED) == 0 && Game.currentGame.resources.get(Resource.COAL) >= 2 && Game.currentGame.resources.get(Resource.FLOUR) >= 3 && working) {
            Game.currentGame.resources.add(Resource.COAL, -2);
            Game.currentGame.resources.add(Resource.FLOUR, -3);
            resources.add(Resource.BREAD, 2);
        }
    }

    @Override
    public void initGUI() {}

    @Override
    protected void onMinedUp() {}

    @Override
    public void onUpgrade(Researches research, boolean initial) {}

    @Override
    public Entity clone() {
        return new Bakery((int) x / Tile.SIZE, (int) y / Tile.SIZE);
    }

    @Override
    protected void onDeath() {}
}
