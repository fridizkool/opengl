package ColladaObjects;

import org.w3c.dom.Node;

public class Triangles extends PolyList
{
	
	public Triangles(Node thisNode)
	{
		super(thisNode);
		this.VCount = new int[this.Count];
		for(int x = 0; x < this.VCount.length; x++)
			this.VCount[x] = 3;
	}
}
