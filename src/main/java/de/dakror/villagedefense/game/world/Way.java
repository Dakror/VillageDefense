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


package de.dakror.villagedefense.game.world;

import java.awt.Graphics2D;
import java.awt.Point;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class Way extends Tile {
	public static final float speed = 3.2f;
	
	public Way() {
		super(3, "Weg", "way.png", 5);
	}
	
	@Override
	public Point getTexturePos(int x, int y) {
		byte[][] n = Game.world.getNeighbors(x, y);
		
		if (n[0][1] != id && n[1][0] != id && n[2][1] != id && n[1][2] != id) return new Point(0, 0); // none adjacent
		
		int c = 0;
		if (n[0][1] == id) c++;
		if (n[2][1] == id) c++;
		if (n[1][0] == id) c++;
		if (n[1][2] == id) c++;
		
		if (c == 1) {
			if (n[0][1] == id) return new Point(1, 3); // l
			if (n[1][0] == id) return new Point(0, 4); // t
			if (n[2][1] == id) return new Point(0, 3); // r
			if (n[1][2] == id) return new Point(1, 4); // b
		} else if (c == 2) {
			if (n[0][1] == id && n[2][1] == id) return new Point(2, 0); // l - r
			if (n[1][0] == id && n[1][2] == id) return new Point(2, 1); // t - b
			
			if (n[2][1] == id && n[1][2] == id) return new Point(0, 1); // r - b
			if (n[0][1] == id && n[1][2] == id) return new Point(1, 1); // l - b
			if (n[2][1] == id && n[1][0] == id) return new Point(0, 2); // r - t
			if (n[0][1] == id && n[1][0] == id) return new Point(1, 2); // l - t
		} else if (c == 3) {
			if (n[1][0] != id) return new Point(2, 2);
			if (n[1][2] != id) return new Point(2, 3);
			if (n[0][1] != id) return new Point(2, 4);
			if (n[2][1] != id) return new Point(2, 5);
		}
		
		return new Point(1, 0);
	}
	
	@Override
	public void drawTile(int cx, int cy, int i, int j, Graphics2D g) {
		int x = i * Tile.SIZE;
		int y = j * Tile.SIZE;
		
		Point tp = getTexturePos(cx * Chunk.SIZE + i, cy * Chunk.SIZE + j);
		
		Helper.drawImage(Game.getImage("tile/" + getTileset()), x, y, SIZE, SIZE, tp.x * SIZE, tp.y * SIZE, SIZE, SIZE, g);
	}
}
