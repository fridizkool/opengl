package ColladaObjects;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Source
{
	private Node ThisNode;
	
	public String ID;
	
	public Float_array floatarray;
	
	public Source(Node thisNode)
	{
		ThisNode = thisNode;
		NamedNodeMap nnm = ThisNode.getAttributes();
		ID = nnm.getNamedItem("id").getNodeValue();
		NodeList nl = ThisNode.getChildNodes();
		
		int x = 0;
		while(!nl.item(x).getNodeName().equals("float_array"))
			x++;
		
		floatarray = new Float_array(nl.item(x));
	}
	
	public String toString()
	{
		return "#" + ID;
	}
}
