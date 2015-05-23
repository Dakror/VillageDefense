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
public class Tile {
	public static final int SIZE = 32;
	public static final int TILES = 256;
	private static Tile[] tileList = new Tile[TILES];
	
	// -- Tile List -- //
	public static Tile empty = new Tile(0, "Leer", "empty.png", 0);
	public static Tile grass = new Tile(1, "Gras", "grass.png", 100);
	public static Tile ground = new Tile(2, "Boden", "ground.png", 100);
	public static Tile way = new Way();
	
	// -- Class def -- //
	protected String name = "Unnamed";
	protected String tileset;
	protected byte id;
	public float G;
	
	public Tile(int id, String name, String tileset, float G) {
		register(id);
		
		this.name = name;
		this.tileset = tileset;
		this.G = G;
	}
	
	private void register(int id) {
		if (tileList[id] == null) tileList[id] = this;
		this.id = (byte) (id - 128);
	}
	
	public String getName() {
		return name;
	}
	
	public String getTileset() {
		return tileset;
	}
	
	/**
	 * @return textureCoords<br>
	 *         If they are out of bounds, handle them as follows:<br>
	 *         <ul>
	 *         <li>if x = 3 it's the trigger to:<br>
	 *         <ul>
	 *         <li>y = 0: Use the texture for concave edges TOPLEFT</li>
	 *         <li>y = 1: Use the texture for concave edges TOPRIGHT</li>
	 *         <li>y = 2: Use the texture for concave edges BOTTOMLEFT</li>
	 *         <li>y = 3: Use the texture for concave edges BOTTOMRIGHT</li>
	 *         </ul>
	 *         </li>
	 *         </ul>
	 */
	public Point getTexturePos(int x, int y) {
		Point p = new Point(1, 2);
		
		byte[][] n = Game.world.getNeighbors(x, y);
		
		if (n[1][0] != id) p.y--;
		if (n[1][2] != id) p.y++;
		if (n[0][1] != id) p.x--;
		if (n[2][1] != id) p.x++;
		
		if (n[0][0] != id && n[1][0] == id && n[0][1] == id && n[2][1] == id && n[1][2] == id) return new Point(3, 0);
		if (n[2][0] != id && n[1][0] == id && n[0][1] == id && n[2][1] == id && n[1][2] == id) return new Point(3, 1);
		if (n[0][2] != id && n[1][0] == id && n[0][1] == id && n[2][1] == id && n[1][2] == id) return new Point(3, 2);
		if (n[2][2] != id && n[1][0] == id && n[0][1] == id && n[2][1] == id && n[1][2] == id) return new Point(3, 3);
		
		return p;
	}
	
	public void drawTile(int cx, int cy, int i, int j, Graphics2D g) {
		int x = i * SIZE;
		int y = j * SIZE;
		Point tp = getTexturePos(cx * Chunk.SIZE + i, cy * Chunk.SIZE + j);
		if (tp.x < 3) // convex
		{
			Helper.drawImage(Game.getImage("tile/" + getTileset()), x, y, SIZE, SIZE, tp.x * SIZE, tp.y * SIZE, SIZE, SIZE, g);
		} else {
			// absolute sizes, not SIZE, easier to read.
			Helper.drawImage(Game.getImage("tile/" + getTileset()), x, y, SIZE, SIZE, 32, 64, 32, 32, g);
			
			if (tp.y == 0) Helper.drawImage(Game.getImage("tile/" + getTileset()), x, y, 16, 16, 64, 0, 16, 16, g);
			if (tp.y == 1) Helper.drawImage(Game.getImage("tile/" + getTileset()), x + 16, y, 16, 16, 80, 0, 16, 16, g);
			if (tp.y == 2) Helper.drawImage(Game.getImage("tile/" + getTileset()), x, y + 16, 16, 16, 64, 16, 16, 16, g);
			if (tp.y == 3) Helper.drawImage(Game.getImage("tile/" + getTileset()), x + 16, y + 16, 16, 16, 80, 16, 16, 16, g);
		}
	}
	
	public byte getId() {
		return id;
	}
	
	public static Tile getTileForId(byte id) {
		return tileList[id + 128];
	}
	
	public static Tile getTileForId(int id) {
		return tileList[id];
	}
}
