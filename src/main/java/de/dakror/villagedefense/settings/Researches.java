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

package de.dakror.villagedefense.settings;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dakror.villagedefense.game.entity.struct.House;
import de.dakror.villagedefense.game.entity.struct.Mine;
import de.dakror.villagedefense.game.entity.struct.tower.Tower;
import de.dakror.villagedefense.settings.Resources.Resource;

public enum Researches {
    TOWER_DOUBLESHOT("Arrow-Tower double shot", new Resources().set(Resource.GOLD, 750).set(Resource.WOOD, 250), new Point(0, 0), 10, Tower.class),
    HOUSE_FORESTER("Ranger", new Resources().set(Resource.GOLD, 300).set(Resource.WOOD, 100), new Point(1, 0), 2, House.class),
    HOUSE_WOODSMAN("Woodcutter", new Resources().set(Resource.GOLD, 450).set(Resource.IRONINGOT, 4), new Point(2, 0), 2, House.class),
    MINE_STONE("Stone mining", new Resources(), new Point(0, 1), 1, Mine.class),
    MINE_IRON("Iron ore mining", new Resources().set(Resource.GOLD, 200).set(Resource.STONE, 100), new Point(1, 1), -1, Mine.class),

    ;

    private String name;
    private Resources costs;
    private Point texturePoint;
    private List<Class<?>> targetClasses;
    /**
     * -1 = discounted version is free
     */
    private float buyDiscount;

    private Researches(String name, Resources costs, Point texturePoint, float buyDiscount, Class<?>... targetClasses) {
        this.name = name;
        this.costs = costs;
        this.texturePoint = texturePoint;
        this.targetClasses = Arrays.asList(targetClasses);
        this.buyDiscount = buyDiscount;
    }

    public String getName() {
        return name;
    }

    public Resources getCosts(boolean discounted) {
        if (!discounted) return costs;

        if (buyDiscount == -1) return new Resources();

        Resources r = new Resources();
        for (Resource res : costs.getFilled())
            r.set(res, Math.round(costs.get(res) / buyDiscount));
        return r;
    }

    public Point getTexturePoint() {
        return texturePoint;
    }

    public boolean isTarget(Class<?> targetClass) {
        return targetClasses.contains(targetClass);
    }

    public float getBuyDiscount() {
        return buyDiscount;
    }

    public static Researches[] values(Class<?> targetClass) {
        ArrayList<Researches> res = new ArrayList<>();
        for (Researches r : values()) {
            if (r.isTarget(targetClass)) res.add(r);
        }

        return res.toArray(new Researches[] {});
    }
}
