package ColladaObjects;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Mesh
{
	private Node ThisNode;
	
	public ArrayList<Source> source;
	public PolyList polylist;
	
	public Mesh(Node thisNode)
	{
		ThisNode = thisNode;
		
		source = new ArrayList<Source>();
		polylist = null;
		
		NodeList nl = ThisNode.getChildNodes();
		for(int x = 0; x < nl.getLength(); x++)
		{
			if(nl.item(x).getNodeName().equals("source"))
				source.add(new Source(nl.item(x)));
			else if(nl.item(x).getNodeName().equals("polylist"))
				polylist = new PolyList(nl.item(x));
			else if(nl.item(x).getNodeName().equals("triangles"))
				polylist = new Triangles(nl.item(x));
		}
	}
}
