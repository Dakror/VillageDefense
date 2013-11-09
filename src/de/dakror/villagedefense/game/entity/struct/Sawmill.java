package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;

import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.settings.Researches;

/**
 * @author Dakror
 */
public class Sawmill extends Struct
{
	public Sawmill(int x, int y)
	{
		super(x, y, 4, 3);
		
		name = "Sägewerk";
	}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	public void drawGUI(Graphics2D g)
	{}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public void onUpgrade(Researches research)
	{}
	
	@Override
	public Entity clone()
	{
		return null;
	}
	
	@Override
	protected void onDeath()
	{}
}
