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


package de.dakror.villagedefense.util.path;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import de.dakror.villagedefense.util.Vector;

public class Path {
	ArrayList<Vector> nodes = new ArrayList<Vector>();
	int index;
	
	public Path(Vector... vectors) {
		nodes.addAll(Arrays.asList(vectors));
		index = 0;
	}
	
	public void add(Vector v) {
		nodes.add(v);
	}
	
	public void draw(Graphics2D g) {
		for (int i = 1; i < nodes.size(); i++) {
			Color oldColor = g.getColor();
			g.setColor(Color.orange);
			g.drawLine((int) nodes.get(i).x, (int) nodes.get(i).y, (int) nodes.get(i - 1).x, (int) nodes.get(i - 1).y);
			g.setColor(oldColor);
		}
	}
	
	public Vector getNode() {
		return nodes.get(index);
	}
	
	public void setNodeReached() {
		index++;
	}
	
	public float getLength() {
		float length = 0;
		for (int i = 0; i < nodes.size() - 1; i++) {
			length += nodes.get(i + 1).getDistance(nodes.get(i));
		}
		return length;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isPathComplete() {
		return index == nodes.size();
	}
	
	public void mul(float sc) {
		for (Vector v : nodes)
			v.mul(sc);
	}
	
	public void translate(int x, int y) {
		for (Vector v : nodes)
			v.add(new Vector(x, y));
	}
	
	public void reset() {
		index = 0;
	}
}
