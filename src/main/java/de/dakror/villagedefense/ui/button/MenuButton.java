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

package de.dakror.villagedefense.ui.button;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class MenuButton extends Button {
    String image;
    int y1;
    Dimension size;
    float alpha;
    float speed = 0.015f;
    float min = 0.6f;
    String english;

    public MenuButton(String english, int y) {
        super(0, 0, 0, 100);
        this.english = english;
        y1 = y;
        size = new Dimension(0, 0);
        alpha = min;
    }

    @Override
    public void draw(Graphics2D g) {
        x = (Game.getWidth() - this.width) / 2;
        y = Game.getHeight() / 3 + y1 * (70 + 40);
        width = Game.getWidth() / 2;
        height = 80;

        Composite c = g.getComposite();

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Helper.drawHorizontallyCenteredString(english, Game.getWidth(), y + 60, g, 64);

        g.setComposite(c);
    }

    @Override
    public void update(int tick) {
        if (!enabled) return;
        if (state == 2 || state == 1) {
            alpha += speed;
        } else if (alpha > min + speed) {
            alpha -= speed;
        }

        if (alpha > 1) alpha = 1;
        if (alpha < min) alpha = min;
    }
}
