package ColladaObjects;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Geometry
{
	private Node ThisNode;
	
	public String ID;
	
	public Mesh mesh;
	
	public Geometry(Node thisNode)
	{
		ThisNode = thisNode;
		
		NamedNodeMap nnm = ThisNode.getAttributes();
		ID = nnm.getNamedItem("id").getNodeValue();
		
		NodeList nl = ThisNode.getChildNodes();
		int x = 0;
		while(!nl.item(x).getNodeName().equals("mesh"))
			x++;
		System.out.println(nl.item(x));
		mesh = new Mesh(nl.item(x));
	}
}
