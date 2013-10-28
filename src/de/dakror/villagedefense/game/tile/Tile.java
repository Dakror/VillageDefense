package de.dakror.villagedefense.game.tile;

import java.awt.Point;

import de.dakror.villagedefense.game.world.World;

/**
 * @author Dakror
 */
public class Tile
{
	public static final int SIZE = 32;
	public static final int TILES = 256;
	private static Tile[] tileList = new Tile[TILES];
	
	// -- Tile List -- //
	public static Tile emtpy = new Tile(0, "Leer", "emtpy.png");
	public static Tile grass = new Tile(1, "Gras", "grass.png");
	public static Tile road = new Tile(2, "Straße", "road.png");
	
	// -- Class def -- //
	private String name = "Unnamed";
	private String tileset;
	private byte id;
	
	public Tile(int id, String name, String tileset)
	{
		register(id);
		
		this.name = name;
		this.tileset = tileset;
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
	
	public String getTileset()
	{
		return tileset;
	}
	
	/**
	 * @return textureCoords<br>
	 *         If they are out of bounds, handle them as follows:<br>
	 *         <ul>
	 *         <li>if x = 3 it's the trigger to:<br>
	 *         <ul>
	 *         <li>y = 0: Use the texture for concave edges TOPLEFT</li>
	 *         <li>y = 1: Use the texture for concave edges TOPRIGHT</li>
	 *         <li>y = 2: Use the texture for concave edges BOTTOMLEFT</li>
	 *         <li>y = 3: Use the texture for concave edges BOTTOMRIGHT</li>
	 *         </ul>
	 *         </li>
	 *         </ul>
	 */
	public Point getTexturePos(int x, int y, World w)
	{
		Point p = new Point(1, 2);
		
		byte[][] n = w.getNeighbors(x, y);
		if (n[1][0] != id) p.y--;
		if (n[1][2] != id) p.y++;
		if (n[0][1] != id) p.x--;
		if (n[2][1] != id) p.x++;
		
		if (n[0][0] != id && n[1][0] == id && n[0][1] == id && n[2][1] == id && n[1][2] == id) return new Point(3, 0);
		if (n[2][0] != id && n[1][0] == id && n[0][1] == id && n[2][1] == id && n[1][2] == id) return new Point(3, 1);
		if (n[0][2] != id && n[1][0] == id && n[0][1] == id && n[2][1] == id && n[1][2] == id) return new Point(3, 2);
		if (n[2][2] != id && n[1][0] == id && n[0][1] == id && n[2][1] == id && n[1][2] == id) return new Point(3, 3);
		
		return p;
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
