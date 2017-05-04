package ColladaObjects;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Mesh
{
	private Node ThisNode;
	
	public ArrayList<Source> source;
	public ArrayList<Triangles> triangles;
	public ArrayList<PolyList> polylist;
	
	public Mesh(Node thisNode)
	{
		ThisNode = thisNode;
		
		source = new ArrayList<Source>();
		polylist = new ArrayList<PolyList>();
		triangles = new ArrayList<Triangles>();
		
		NodeList nl = ThisNode.getChildNodes();
		for(int x = 1; x < nl.getLength(); x++)
		{
			if(nl.item(x).equals("source"))
				source.add(new Source(nl.item(x)));
			
			if(nl.item(x).equals("polylist"))
				polylist.add(new PolyList(nl.item(x)));
			
			if(nl.item(x).equals("triangles"))
				polylist.add(new PolyList(nl.item(x)));
		}
	}
}
