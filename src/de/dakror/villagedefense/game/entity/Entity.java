package de.dakror.villagedefense.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import de.dakror.villagedefense.settings.Attributes;
import de.dakror.villagedefense.settings.Attributes.Attribute;
import de.dakror.villagedefense.settings.Resources;
import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public abstract class Entity implements Drawable
{
	protected float x, y;
	protected int width, height;
	protected boolean hovered, clicked, dead;
	protected String name;
	protected Rectangle bump;
	protected Attributes attributes;
	protected Resources resources;
	
	public Entity(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		bump = new Rectangle();
		attributes = new Attributes();
		resources = new Resources();
	}
	
	public void drawBump(Graphics2D g, boolean above)
	{
		if (bump == null || (!hovered && !clicked)) return;
		
		Color oldColor = g.getColor();
		g.setColor(clicked ? Color.black : Color.darkGray);
		if (above)
		{
			g.drawLine((int) x + bump.x, (int) y + bump.y, (int) x + bump.x, (int) y + bump.y + bump.height); // left
			g.drawLine((int) x + bump.x, (int) y + bump.y + bump.height, (int) x + bump.x + bump.width, (int) y + bump.y + bump.height); // bottom
			g.drawLine((int) x + bump.x + bump.width, (int) y + bump.y, (int) x + bump.x + bump.width, (int) y + bump.y + bump.height); // right
		}
		else
		{
			g.drawLine((int) x + bump.x, (int) y + bump.y, (int) x + bump.x + bump.width, (int) y + bump.y); // top
		}
		g.setColor(oldColor);
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	@Override
	public void update(int tick)
	{
		if (attributes.get(Attribute.HEALTH) < 1)
		{
			onDeath();
		}
		
		tick(tick);
	}
	
	protected abstract void tick(int tick);
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public void translate(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
	
	public Rectangle getBump(boolean includePos)
	{
		if (!includePos) return bump;
		else
		{
			Rectangle rect = (Rectangle) bump.clone();
			rect.translate((int) x, (int) y);
			return rect;
		}
	}
	
	public void setBump(Rectangle r)
	{
		bump = r;
	}
	
	public boolean contains(float x, float y)
	{
		return x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height;
	}
	
	public boolean mouseMoved(MouseEvent e)
	{
		return hovered = contains(e.getXOnScreen(), e.getYOnScreen());
	}
	
	public boolean mousePressed(MouseEvent e)
	{
		return clicked = contains(e.getXOnScreen(), e.getYOnScreen()) && e.getButton() == MouseEvent.BUTTON1;
	}
	
	public void setClicked(boolean b)
	{
		clicked = b;
	}
	
	public void setHovered(boolean b)
	{
		hovered = b;
	}
	
	public Vector getPos()
	{
		return new Vector(x, y);
	}
	
	public void setPos(Vector v)
	{
		x = v.x;
		y = v.y;
	}
	
	public Attributes getAttributes()
	{
		return attributes;
	}
	
	public Resources getResources()
	{
		return resources;
	}
	
	public void dealDamage(int amount)
	{
		attributes.add(Attribute.HEALTH, -amount);
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public String getName()
	{
		return name;
	}
	
	// -- abstract event methods -- //
	public abstract void onSpawn();
	
	protected abstract void onDeath();
}
