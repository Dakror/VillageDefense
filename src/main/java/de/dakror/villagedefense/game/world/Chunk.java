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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import de.dakror.villagedefense.game.Game;

/**
 * @author Dakror
 */
public class Chunk {
	public static final int SIZE = 8;
	
	int x, y;
	
	byte[][] data = new byte[SIZE][SIZE];
	
	BufferedImage image;
	
	public Chunk(int x, int y) {
		this.x = x;
		this.y = y;
		
		image = new BufferedImage(SIZE * Tile.SIZE, SIZE * Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				data[i][j] = Tile.grass.getId();
	}
	
	public void render() {
		if (image == null) image = new BufferedImage(SIZE * Tile.SIZE, SIZE * Tile.SIZE, BufferedImage.TYPE_INT_ARGB);
		else image.flush();
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Tile tile = Tile.getTileForId(data[i][j]);
				
				if (tile.equals(Tile.empty)) continue;
				
				tile.drawTile(x, y, i, j, g);
			}
		}
	}
	
	public byte getTileId(int x, int y) {
		if (x < 0 || y < 0) return Tile.empty.getId();
		
		if (x >= SIZE) x -= this.x * SIZE;
		if (y >= SIZE) y -= this.y * SIZE;
		
		return data[x][y];
	}
	
	public void draw(Graphics2D g) {
		if (image == null) render();
		
		g.drawImage(image, x * SIZE * Tile.SIZE, y * SIZE * Tile.SIZE, Game.w);
	}
	
	public void setTileId(int x, int y, byte d) {
		data[x][y] = d;
	}
	
	public byte[] getData() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				baos.write(data[i][j]);
			}
		}
		
		return baos.toByteArray();
	}
	
	public void setData(byte[] data) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				this.data[i][j] = data[i * SIZE + j];
			}
		}
	}
}
