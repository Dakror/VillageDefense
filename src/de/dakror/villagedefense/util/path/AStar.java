package de.dakror.villagedefense.util.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.dakror.villagedefense.game.Game;
import de.dakror.villagedefense.game.world.Tile;
import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class AStar
{
	static ArrayList<Node> openList;
	static ArrayList<Node> closedList;
	static Vector target;
	
	public static Path getPath(Vector start, Vector t)
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
			
			if (activeNode.H == 0)
			{
				break; // found way
			}
			
			closedList.add(activeNode);
			
			handleAdjacentTiles(activeNode);
		}
		
		return null;
	}
	
	private static void handleAdjacentTiles(Node node)
	{
		byte[][] neighbors = Game.world.getNeighbors((int) node.t.x, (int) node.t.y);
		
		for (int i = 0; i < neighbors.length; i++)
		{
			for (int j = 0; j < neighbors[0].length; j++)
			{
				if (i == 1 && j == 1 || neighbors[i][j] == Tile.empty.getId()) continue;
				
				Vector tile = new Vector(node.t.x + i - 1, node.t.y + j - 1);
				
				openList.add(new Node(node.G + Tile.getTileForId(neighbors[i][j]).G, tile.getDistance(target), tile, node));
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
