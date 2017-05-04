package ColladaObjects;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Geometry
{
	private Node ThisNode;
	
	public String ID;
	
	public ArrayList<Mesh> mesh;
	
	public Geometry(Node thisNode)
	{
		ThisNode = thisNode;
		
		NamedNodeMap nnm = ThisNode.getAttributes();
		ID = nnm.getNamedItem("id").getNodeValue();
		
		mesh = new ArrayList<Mesh>();
		
		NodeList nl = ThisNode.getChildNodes();
		for(int x = 1; x < nl.getLength(); x++)
		{
			if(nl.item(x).equals("mesh"))
				mesh.add(new Mesh(nl.item(x)));
		}
	}
}
