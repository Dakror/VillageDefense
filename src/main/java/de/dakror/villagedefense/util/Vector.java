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

import java.awt.Point;


public class Vector {
	public float x;
	public float y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(Point p) {
		x = p.x;
		y = p.y;
	}
	
	public Vector add(Vector o) {
		x += o.x;
		y += o.y;
		
		return this;
	}
	
	public Vector sub(Vector o) {
		x -= o.x;
		y -= o.y;
		
		return this;
	}
	
	public float getLength() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public void setLength(float length) {
		float fac = length / getLength();
		x *= fac;
		y *= fac;
	}
	
	@Override
	public Vector clone() {
		return new Vector(x, y);
	}
	
	public Vector normalize() {
		setLength(1);
		
		return this;
	}
	
	public float getDistance(Vector o) {
		return clone().sub(o).getLength();
	}
	
	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}
	
	public Vector mul(float size) {
		x *= size;
		y *= size;
		
		return this;
	}
	
	public float getAngleOnXAxis() {
		return (float) Math.toDegrees(Math.atan2(y, x));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector) {
			Vector v = (Vector) obj;
			return x == v.x && y == v.y;
		}
		return false;
	}
	
	public float dot(Vector o) {
		return x * o.x + y * o.y;
	}
}
