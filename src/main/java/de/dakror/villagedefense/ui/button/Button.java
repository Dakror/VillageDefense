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

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import de.dakror.gamesetup.ui.Component;
import de.dakror.villagedefense.ui.ClickEvent;

/**
 * @author Dakror
 */
public abstract class Button extends Component {
	ArrayList<ClickEvent> events;
	
	public Button(int x, int y, int width, int height) {
		super(x, y, width, height);
		events = new ArrayList<>();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (contains(e.getX(), e.getY()) && enabled) {
			triggerEvents();
			state = 0;
		}
	}
	
	public void addClickEvent(ClickEvent e) {
		events.add(e);
	}
	
	public void triggerEvents() {
		for (ClickEvent e1 : events)
			e1.trigger();
	}
}
