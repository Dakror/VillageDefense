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

import java.util.EnumMap;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.entity.creature.Creature;
import de.dakror.villagedefense.game.entity.creature.Ghost;
import de.dakror.villagedefense.game.entity.creature.Golem;
import de.dakror.villagedefense.game.entity.creature.Skeleton;
import de.dakror.villagedefense.game.entity.creature.TreeMonster;
import de.dakror.villagedefense.game.entity.creature.Troll;
import de.dakror.villagedefense.game.entity.creature.Zombie;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class WaveManager {
    public enum Monster {
        ZOMBIE("zombie", Zombie.class, "Zombie:Default speed undead. Deals 5 damage to buildings. Can take 20 damage"),
        GHOST("ghost", Ghost.class, "Ghost:Fast ghost. Deals 10 damage to buildings. Can take 8 damage. Flies through barricades"),
        SKELETON("skeleton", Skeleton.class, "Skeleton:Slow knight of death. Deals 10 damage to buildings. Can take 50 damage"),
        TROLL("troll", Troll.class, "Troll:Scuffing Supermonster. Deals 25 damage to buildings. Can take 500 damage"),
        GOLEM("golem", Golem.class, "Golem:Default speed stone golem. Deals 20 damage to buildings. Can take 150 damage. Immune against arrows"),
        TREE("treemonster", TreeMonster.class, "Treemonster:Slow monstrous tree. Deals 18 damage to buildings. Can take 100 damage. Only takes fire damage")

        ;

        private final String image;
        private final Class<?> creatureClass;
        private final String description;

        private Monster(String image, Class<?> creatureClass, String desc) {
            this.image = image;
            this.creatureClass = creatureClass;
            description = desc;
        }

        public String getImage() {
            return image;
        }

        public String getDescription() {
            return description;
        }

        public Class<?> getCreatureClass() {
            return creatureClass;
        }
    }

    public static int wave = 0;
    public static int nextWave; // int seconds

    public static final int waveTimer = 60; // in seconds

    public static EnumMap<Monster, Integer> monsters = new EnumMap<>(Monster.class);

    public static void reset() {
        wave = 0;
        nextWave = waveTimer;
    }

    public static void init() {
        generateNextWave();
    }

    static void generateNextWave() {
        Game.world.core.dealDamage(-5, null);
        monsters.clear();

        wave++;

        Game.currentGame.resources.add(Resource.GOLD, 10 * wave);

        /*
         * either parabola: 0.075 * wave*wave + 3
         * or line: 2 * wave + 3
         */
        if (wave < 30) monsters.put(Monster.ZOMBIE, Math.round(2 * wave + 1));
        else monsters.put(Monster.GOLEM, Math.round(wave - 29));

        if (wave >= 5 && wave < 40) monsters.put(Monster.SKELETON, Math.round(1.25f * wave - 5 - (wave > 30 ? 30 : 0)));
        else if (wave > 40) monsters.put(Monster.TREE, Math.round(1.25f * wave - 5 - 40));

        if (wave % 10 == 0 && wave > 2) {
            monsters.clear();
            monsters.put(Monster.TROLL, wave / 10);
        }

        nextWave = waveTimer;
    }

    public static void update() {
        if (nextWave <= 0) {
            if (monsters.size() > 0) {
                new Thread() {
                    @Override
                    public void run() {
                        EnumMap<Monster, Integer> monsters = WaveManager.monsters.clone();
                        WaveManager.monsters.clear();
                        int leftLength = 0;
                        int rightLength = 0;

                        int space = Tile.SIZE * 2 - wave;
                        space = space < Tile.SIZE ? Tile.SIZE : space;

                        for (Monster monster : monsters.keySet()) {
                            for (int i = 0; i < monsters.get(monster); i++) {
                                try {
                                    boolean left = Math.random() < 0.5;

                                    int x = left ? -leftLength * space : Game.world.width + rightLength * space;
                                    Entity e = (Entity) monster.getCreatureClass().getConstructor(int.class, int.class).newInstance(x, 0);
                                    int y = Game.world.height / 2 - e.getBump(false).y + e.getBump(false).height;
                                    e.setY(y);

                                    Game.world.addEntity2(e, false);

                                    if (left) leftLength++;
                                    else rightLength++;
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }

                            leftLength /= 2;
                            rightLength /= 2;
                        }
                    }
                }.start();
            } else {
                if (stageClear()) generateNextWave();
            }
        }
    }

    public static boolean stageClear() {
        for (Entity e : Game.world.entities) {
            if (e instanceof Creature && ((Creature) e).isHostile()) return false;
        }

        return true;
    }
}
