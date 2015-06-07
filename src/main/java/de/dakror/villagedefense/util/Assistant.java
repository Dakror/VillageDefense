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


package de.dakror.villagedefense.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Assistant {
	public static void drawResource(Resources resources, Resource r, int x, int y, int size, int space, Graphics2D g) {
		drawLabelWithIcon(x, y, size, new Point(r.getIconX(), r.getIconY()), resources.get(r) + "", space, g);
	}
	
	public static void drawResource(Resources resources, Resource r, int amount, int x, int y, int size, int space, Graphics2D g) {
		drawLabelWithIcon(x, y, size, new Point(r.getIconX(), r.getIconY()), amount + "", space, g);
	}
	
	public static void drawLabelWithIcon(int x, int y, int size, Point icon, String text, int space, Graphics2D g) {
		Helper.drawImage(Game.getImage("icons.png"), x, y, 24, 24, icon.x * 24, icon.y * 24, 24, 24, g);
		Font old = g.getFont();
		g.setFont(g.getFont().deriveFont((float) size));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(text, x + space, y + fm.getAscent() + 2);
		g.setFont(old);
	}
}
