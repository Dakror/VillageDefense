package de.dakror.villagedefense.layer;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.struct.Struct;

/**
 * @author Dakror
 */
public class StructGUILayer extends Layer
{
	@Override
	public void draw(Graphics2D g)
	{
		try
		{
			if (Game.world.selectedEntity != null && Game.world.selectedEntity instanceof Struct && ((Struct) Game.world.selectedEntity).guiPoint != null) ((Struct) Game.world.selectedEntity).drawGUI(g);
		}
		catch (NullPointerException e)
		{}
	}
	
	@Override
	public void update(int tick)
	{}
	
	@Override
	public void init()
	{}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		
		if (Game.currentGame.placedStruct) return;
		
		if (Game.currentGame.state == 0 && Game.world.selectedEntity != null && Game.world.selectedEntity instanceof Struct) if (((Struct) Game.world.selectedEntity).guiPoint != null) Game.world.selectedEntity.mousePressed(e);
	}
}
