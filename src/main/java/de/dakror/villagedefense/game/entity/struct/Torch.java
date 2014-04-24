package de.dakror.villagedefense.game.entity.struct;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import de.dakror.gamesetup.util.Helper;
import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.settings.Researches;
import de.dakror.villagedefense.settings.Resources.Resource;

/**
 * @author Dakror
 */
public class Torch extends Struct
{
	int frame;
	
	public Torch(int x, int y)
	{
		super(x, y, 1, 2);
		
		frame = 0;
		
		name = "Fackel";
		
		description = "Entzündet hindurch fliegende Projektile.";
		
		buildingCosts.set(Resource.GOLD, 25);
		buildingCosts.set(Resource.IRONINGOT, 10);
		buildingCosts.set(Resource.COAL, 5);
		
		setBump(new Rectangle2D.Float(.25f, 1.5f, 0.5f, 0.5f));
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		Helper.drawImage(Game.getImage("creature/torch.png"), (int) x, (int) y, width, height, (frame % 4) * 32, 0, 32, 64, g);
	}
	
	@Override
	protected BufferedImage createImage()
	{
		return Game.getImage("creature/torch.png").getSubimage(0, 0, 32, 64); // default image
	}
	
	@Override
	public Shape getAttackArea()
	{
		return new Rectangle2D.Float(x, 1.f * Tile.SIZE + y, Tile.SIZE, 0.6f * Tile.SIZE);
	}
	
	@Override
	protected void tick(int tick)
	{
		super.tick(tick);
		
		if (tick % 3 == 0) frame = (frame + 1) % 4;
	}
	
	@Override
	public void initGUI()
	{}
	
	@Override
	protected void onMinedUp()
	{}
	
	@Override
	public void onUpgrade(Researches research, boolean initial)
	{}
	
	@Override
	public Entity clone()
	{
		return new Torch((int) x / Tile.SIZE, (int) y / Tile.SIZE);
	}
	
	@Override
	protected void onDeath()
	{}
	
}
