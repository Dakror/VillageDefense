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

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;

public class ScreenManager {
	private GraphicsDevice vc;
	
	public ScreenManager() {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = e.getDefaultScreenDevice();
	}
	
	public void setFullScreen(Window w) {
		vc.setFullScreenWindow(w);
	}
	
	public Graphics2D getGraphics() {
		Window window = vc.getFullScreenWindow();
		if (window != null) {
			BufferStrategy s = window.getBufferStrategy();
			return (Graphics2D) s.getDrawGraphics();
		}
		return null;
	}
	
	public void update() {
		Window window = vc.getFullScreenWindow();
		if (window != null) {
			BufferStrategy s = window.getBufferStrategy();
			if (!s.contentsLost()) s.show();
		}
	}
	
	public Window getFullScreenWindow() {
		return vc.getFullScreenWindow();
	}
	
	public void restoreScreen() {
		Window window = vc.getFullScreenWindow();
		if (window != null) window.dispose();
		vc.setFullScreenWindow(null);
	}
}
