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

import java.util.ArrayList;

import de.dakror.villagedefense.util.Vector;


/**
 * @author Dakror
 */
public class StructPoints {
	public ArrayList<Vector> entries = new ArrayList<>();
	public ArrayList<Vector> exits = new ArrayList<>();
	public ArrayList<Vector> attacks = new ArrayList<>();
	
	public StructPoints addEntries(Vector... points) {
		for (Vector p : points)
			entries.add(p);
		
		return this;
	}
	
	public StructPoints addExits(Vector... points) {
		for (Vector p : points)
			exits.add(p);
		
		return this;
	}
	
	public StructPoints addAttacks(Vector... points) {
		for (Vector p : points)
			attacks.add(p);
		
		return this;
	}
}
