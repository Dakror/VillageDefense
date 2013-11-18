package de.dakror.villagedefense.util.path;

import java.util.ArrayList;
import java.util.Comparator;

import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class AStar
{
	public static Path getPath(Vector start, Vector target)
	{
		ArrayList<Node> openList = new ArrayList<>();
		ArrayList<Node> closedList = new ArrayList<>();
		
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
		}
		
		// return null;
	}
	
	public static Path toPath(ArrayList<Node> list)
	{
		Path p = new Path();
		
		for (Node n : list)
			p.add(n.t);
		
		return p;
	}
}
