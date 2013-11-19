package de.dakror.villagedefense.util.path;

import de.dakror.villagedefense.util.Vector;

/**
 * @author Dakror
 */
public class Node
{
	public float F, G, H;
	public Node p;
	public Vector t;
	
	public Node(float G, float H, Vector t, Node p)
	{
		this.G = G;
		this.H = H;
		F = G + H;
		this.p = p;
		this.t = t;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Node)
		{
			Node n = (Node) obj;
			return t.equals(n.t);
		}
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return t.toString();
	}
}
