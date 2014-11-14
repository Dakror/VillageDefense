package de.dakror.villagedefense.util;

import java.awt.Graphics2D;

/**
 * @author Dakror
 */
public interface Drawable {
	public void draw(Graphics2D g);
	
	public void update(int tick);
}
