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
