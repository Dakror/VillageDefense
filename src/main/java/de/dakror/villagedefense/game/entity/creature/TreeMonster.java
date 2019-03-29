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

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.projectile.Projectile;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

public class TreeMonster extends Creature {
    public TreeMonster(int x, int y) {
        super(x, y, "treemonster");

        name = "Treemonster";

        attributes.set(Attribute.SPEED, 1.5f);
        attributes.set(Attribute.DAMAGE_STRUCT, 18);
        attributes.set(Attribute.HEALTH, 100);
        attributes.set(Attribute.HEALTH_MAX, 100);
        attributes.set(Attribute.ATTACK_RANGE, Tile.SIZE);

        setHostile(true);

        resources.set(Resource.GOLD, 20);
        resources.set(Resource.WOOD, 25);

        description = "A monstrous tree.";
    }

    @Override
    protected boolean onArrivalAtEntity(int tick) {
        return false;
    }

    @Override
    public Entity clone() {
        return new TreeMonster((int) x, (int) y);
    }

    @Override
    public void dealDamage(int amount, Object source) {
        if (source instanceof Projectile) {
            Projectile p = (Projectile) source;
            if (!p.isOnFire()) return;
        }

        super.dealDamage(amount, source);
    }
}
