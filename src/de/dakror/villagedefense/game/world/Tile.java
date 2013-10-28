package de.dakror.villagedefense.game.world;

import java.awt.Point;

/**
 * @author Dakror
 */
public class Tile
{
	public static final int SIZE = 32;
	public static final int TILES = 256;
	private static Tile[] tileList = new Tile[TILES];
	
	// -- Tile List -- //
	public static Tile emtpy = new Tile(0, "Leer", new Point(0, 0));
	public static Tile grass = new Tile(1, "Gras", new Point(1, 0));
	
	// -- Class def -- //
	private String name = "Unnamed";
	private Point texturePos;
	private byte id;
	
	public Tile(int id, String name, Point texturePos)
	{
		register(id);
		
		this.name = name;
		this.texturePos = texturePos;
	}
	
	private void register(int id)
	{
		if (tileList[id] == null) tileList[id] = this;
		this.id = (byte) (id - 128);
	}
	
	public String getName()
	{
		return name;
	}
	
	public Point getTexturePos()
	{
		return texturePos;
	}
	
	public byte getId()
	{
		return id;
	}
	
	public static Tile getTileForId(byte id)
	{
		return tileList[id + 128];
	}
	
	public static Tile getTileForId(int id)
	{
		return tileList[id];
	}
}
