package de.dakror.villagedefense.util.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.entity.Entity;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class AStar
{
	public static ArrayList<Node> openList;
	public static ArrayList<Node> closedList;
	static Vector target;
	
	public static Path getPath(Vector start, Vector t)
	{
		try
		{
			openList = new ArrayList<>();
			closedList = new ArrayList<>();
			
			target = t;
			
			Comparator<Node> comparator = new Comparator<Node>()
			{
				@Override
				public int compare(Node o1, Node o2)
				{
					return Float.compare(o1.F, o2.F);
				}
			};
			
			openList.add(new Node(0, start.getDistance(target), start, null));
			
			Node activeNode = null;
			
			while (true)
			{
				if (openList.size() == 0)
				{
					return null; // no way
				}
				
				Collections.sort(openList, comparator);
				activeNode = openList.remove(0);
				
				closedList.add(activeNode);
				
				if (activeNode.H == 0)
				{
					break; // found way
				}
				
				handleAdjacentTiles(activeNode);
			}
			
			ArrayList<Node> path = new ArrayList<>();
			Node node = activeNode;
			path.add(node.clone());
			while (node.p != null)
			{
				path.add(node.p.clone());
				node = node.p;
			}
			
			Collections.reverse(path);
			return toPath(path);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private static void handleAdjacentTiles(Node node)
	{
		byte[][] neighbors = Game.world.getNeighbors((int) node.t.x, (int) node.t.y);
		
		for (int i = 0; i < neighbors.length; i++)
		{
			for (int j = 0; j < neighbors[0].length; j++)
			{
				if (i == j || neighbors[i][j] == Tile.empty.getId() || (i == 0 && j == 2) || (i == 2 && j == 0)) continue;
				
				Vector tile = new Vector(node.t.x + i - 1, node.t.y + j - 1);
				Node n = new Node(node.G + Tile.getTileForId(neighbors[i][j]).G, tile.getDistance(target), tile, node);
				
				if (closedList.contains(n)) continue;
				
				if (openList.contains(n) && openList.get(openList.indexOf(n)).G > n.G)
				{
					openList.get(openList.indexOf(n)).G = n.G;
					openList.get(openList.indexOf(n)).p = node;
				}
				else if (!openList.contains(n))
				{
					boolean free = true;
					for (Entity e : Game.world.entities)
					{
						if (e.getBump(true).intersects(tile.x * Tile.SIZE, tile.y * Tile.SIZE, Tile.SIZE, Tile.SIZE) && e.isMassive())
						{
							free = false;
							break;
						}
					}
					
					if (free || n.H == 0) openList.add(n);
				}
			}
		}
	}
	
	public static Path toPath(ArrayList<Node> list)
	{
		Path p = new Path();
		
		for (Node n : list)
			p.add(n.t);
		
		return p;
	}
}
