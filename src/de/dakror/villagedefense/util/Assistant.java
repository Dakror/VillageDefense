package de.dakror.villagedefense.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * @author Dakror
 */
public class Assistant
{
	public static int drawHorizontallyCenteredString(String s, int w, int h, Graphics2D g, int size)
	{
		Font old = g.getFont();
		g.setFont(g.getFont().deriveFont((float) size));
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(s)) / 2;
		g.drawString(s, x, h);
		int nx = x + fm.stringWidth(s);
		g.setFont(old);
		return nx;
	}
}
