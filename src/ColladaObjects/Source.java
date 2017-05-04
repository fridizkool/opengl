package ColladaObjects;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Source
{
	private Node ThisNode;
	
	public String ID;
	
	public ArrayList<Float_array> floatarray;
	
	public Source(Node thisNode)
	{
		ThisNode = thisNode;
		NamedNodeMap nnm = ThisNode.getAttributes();
		ID = nnm.getNamedItem("id").getNodeValue();
		
		floatarray = new ArrayList<Float_array>();
		
		NodeList nl = ThisNode.getChildNodes();
		for(int x = 1; x < nl.getLength(); x++)
		{
			if(nl.item(x).equals("mesh"))
				floatarray.add(new Float_array(nl.item(x)));
		}
	}
	
	public String toString()
	{
		return "#" + ID;
	}
}
