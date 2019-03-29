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
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Zombie extends Creature {
    public Zombie(int x, int y) {
        super(x, y, "zombie");

        setHostile(true);
        attributes.set(Attribute.DAMAGE_STRUCT, 5);
        attributes.set(Attribute.HEALTH, 20);
        attributes.set(Attribute.HEALTH_MAX, 20);

        resources.add(Resource.GOLD, 6);

        name = "Zombie";

        description = "A Zombie.";
    }

    @Override
    public void onDeath() {
        if (Math.random() <= 0.35) // 35% prob. spawning ghost
        {
            boolean left = Math.random() < 0.5;
            Game.world.addEntity(new Ghost(left ? 0 : Game.world.width, Game.world.height / 2), false);
        }
        super.onDeath();
    }

    @Override
    protected boolean onArrivalAtEntity(int tick) {
        return false;
    }

    @Override
    public Entity clone() {
        return new Zombie((int) x, (int) y);
    }
}
