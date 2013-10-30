package de.dakror.villagedefense.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Assistant
{
	public static int[] drawHorizontallyCenteredString(String s, int w, int h, Graphics2D g, int size)
	{
		Font old = g.getFont();
		g.setFont(g.getFont().deriveFont((float) size));
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(s)) / 2;
		g.drawString(s, x, h);
		g.setFont(old);
		return new int[] { x, fm.stringWidth(s) };
	}
	
	public static void drawOutline(int x, int y, int width, int height, boolean doubled, Graphics2D g)
	{
		BufferedImage gui = Game.getImage("gui/gui.png");
		
		int cornerSize = (doubled) ? 24 : 19;
		int lineThickness = (doubled) ? 17 : 12;
		int lineHeight = (doubled) ? 55 : 59;
		int lineWidth = (doubled) ? 73 : 74;
		
		// x1 y1 y2 x2
		int[] c = (doubled) ? new int[] { 856, 189, 294, 978 } : new int[] { 865, 398, 498, 982 };
		int[] m = (doubled) ? new int[] { 893, 227, 301, 985 } : new int[] { 899, 428, 505, 989 };
		
		drawImage(gui, x, y, cornerSize, cornerSize, c[0], c[1], cornerSize, cornerSize, g); // lt
		
		for (int i = 0; i < (width - cornerSize * 2) / lineWidth; i++)
			drawImage(gui, x + cornerSize + i * lineWidth, y, lineWidth, lineThickness, m[0], c[1], lineWidth, lineThickness, g); // mt
		drawImage(gui, x + cornerSize + (width - cornerSize * 2) / lineWidth * lineWidth, y, (width - cornerSize * 2) % lineWidth, lineThickness, m[0], c[1], ((width - cornerSize * 2) % lineWidth), lineThickness, g);
		
		drawImage(gui, x + width - cornerSize, y, cornerSize, cornerSize, c[3], c[1], cornerSize, cornerSize, g); // rt
		
		for (int i = 0; i < (height - cornerSize * 2) / lineHeight; i++)
			drawImage(gui, x, y + cornerSize + i * lineHeight, lineThickness, lineHeight, c[0], m[1], lineThickness, lineHeight, g); // ml
		drawImage(gui, x, y + cornerSize + (height - cornerSize * 2) / lineHeight * lineHeight, lineThickness, (height - cornerSize * 2) % lineHeight, c[0], m[1], lineThickness, ((height - cornerSize * 2) % lineHeight), g);
		
		for (int i = 0; i < (height - cornerSize * 2) / lineHeight; i++)
			drawImage(gui, x + width - lineThickness, y + cornerSize + i * lineHeight, lineThickness, lineHeight, m[3], m[1], lineThickness, lineHeight, g); // mr
		drawImage(gui, x + width - lineThickness, y + cornerSize + (height - cornerSize * 2) / lineHeight * lineHeight, lineThickness, (height - cornerSize * 2) % lineHeight, m[3], m[1], lineThickness, ((height - cornerSize * 2) % lineHeight), g);
		
		drawImage(gui, x, y + height - cornerSize, cornerSize, cornerSize, c[0], c[2], cornerSize, cornerSize, g); // lb
		
		for (int i = 0; i < (width - cornerSize * 2) / lineWidth; i++)
			drawImage(gui, x + cornerSize + i * lineWidth, y + height - lineThickness, lineWidth, lineThickness, m[0], m[2], lineWidth, lineThickness, g); // mb
		drawImage(gui, x + cornerSize + (width - cornerSize * 2) / lineWidth * lineWidth, y + height - lineThickness, (width - cornerSize * 2) % lineWidth, lineThickness, m[0], m[2], ((width - cornerSize * 2) % lineWidth), lineThickness, g);
		
		drawImage(gui, x + width - cornerSize, y + height - cornerSize, cornerSize, cornerSize, c[3], c[2], cornerSize, cornerSize, g); // rb
		
	}
	
	public static void drawContainer(int x, int y, int width, int height, boolean doubled, boolean wood, Graphics2D g)
	{
		drawShadow(x - 10, y - 10, width + 20, height + 20, g);
		Image image = Game.getImage(wood ? "gui/wood.png" : "gui/paper.png");
		
		
		Shape oldClip = g.getClip();
		g.setClip(x, y, width, height);
		
		for (int i = x; i < x + width; i += 512)
		{
			for (int j = y; j < y + height; j += 512)
			{
				g.drawImage(image, i, j, Game.w);
			}
		}
		
		g.setClip(oldClip);
		drawOutline(x, y, width, height, doubled, g);
	}
	
	public static void drawShadow(int x, int y, int width, int height, Graphics2D g)
	{
		BufferedImage shadow = Game.getImage("gui/shadow.png");
		
		int size = 32;
		
		drawImage(shadow, x, y, size, size, 0, 0, size, size, g); // lt
		drawImage(shadow, x + width - size, y, size, size, size * 2, 0, size, size, g); // rt
		drawImage(shadow, x, y + height - size, size, size, 0, size * 2, size, size, g); // lb
		drawImage(shadow, x + width - size, y + height - size, size, size, size * 2, size * 2, size, size, g); // rb
		
		for (int i = x + size; i <= x + width - size * 2; i += size)
			drawImage(shadow, i, y, size, size, size, 0, size, size, g);// t
		drawImage(shadow, x + width - size - (width - size * 2) % size, y, (width - size * 2) % size, size, size, 0, (width - size * 2) % size, size, g);
		
		for (int i = x + size; i <= x + width - size * 2; i += size)
			drawImage(shadow, i, y + height - size, size, size, size, size * 2, size, size, g); // b
		drawImage(shadow, x + width - size - (width - size * 2) % size, y + height - size, (width - size * 2) % size, size, size, size * 2, (width - size * 2) % size, size, g);
		
		for (int i = y + size; i <= y + height - size * 2; i += size)
			drawImage(shadow, x, i, size, size, 0, size, size, size, g); // l
		drawImage(shadow, x, y + height - size - (height - size * 2) % size, size, (height - size * 2) % size, 0, size, size, (height - size * 2) % size, g);
		
		for (int i = y + size; i <= y + height - size * 2; i += size)
			drawImage(shadow, x + width - size, i, size, size, size * 2, size, size, size, g); // r
		drawImage(shadow, x + width - size, y + height - size - (height - size * 2) % size, size, (height - size * 2) % size, size * 2, size, size, (height - size * 2) % size, g);
		
		drawImage(shadow, x + size, y + size, width - size * 2, height - size * 2, size, size, size, size, g); // m
	}
	
	public static void drawImage(Image img, int x, int y, int width, int height, int sx, int sy, int swidth, int sheight, Graphics2D g)
	{
		g.drawImage(img, x, y, x + width, y + height, sx, sy, sx + swidth, sy + sheight, Game.w);
	}
	
	public static void drawResource(Resources resources, Resource r, int x, int y, int size, int space, Graphics2D g)
	{
		drawImage(Game.getImage("icons.png"), x, y, 24, 24, r.getIconX() * 24, r.getIconY() * 24, 24, 24, g);
		Font old = g.getFont();
		g.setFont(g.getFont().deriveFont((float) size));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(resources.get(r) + "", x + space, y + fm.getAscent() + 2);
		g.setFont(old);
	}
}
