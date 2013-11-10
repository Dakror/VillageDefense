package de.dakror.villagedefense.layer;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.CopyOnWriteArrayList;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.ui.Component;
import de.dakror.villagedefense.util.Drawable;
import de.dakror.villagedefense.util.EventListener;

/**
 * @author Dakror
 */
public abstract class Layer extends EventListener implements Drawable
{
	protected CopyOnWriteArrayList<Component> components;
	
	public Layer()
	{
		components = new CopyOnWriteArrayList<>();
	}
	
	public abstract void init();
	
	protected void drawComponents(Graphics2D g)
	{
		Component hovered = null;
		for (Component c : components)
		{
			c.draw(g);
			if (c.state == 2) hovered = c;
		}
		if (hovered != null) hovered.drawTooltip(Game.currentGame.mouse.x, Game.currentGame.mouse.y, g);
	}
	
	protected void updateComponents(int tick)
	{
		for (Component c : components)
			c.update(tick);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		for (Component c : components)
			c.mouseWheelMoved(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		for (Component c : components)
			c.mouseDragged(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		for (Component c : components)
			c.mouseMoved(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		for (Component c : components)
			c.mouseClicked(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		for (Component c : components)
			c.mousePressed(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		for (Component c : components)
			c.mouseReleased(e);
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		for (Component c : components)
			c.mouseEntered(e);
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		for (Component c : components)
			c.mouseExited(e);
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		for (Component c : components)
			c.keyTyped(e);
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		for (Component c : components)
			c.keyPressed(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		for (Component c : components)
			c.keyReleased(e);
	}
}
