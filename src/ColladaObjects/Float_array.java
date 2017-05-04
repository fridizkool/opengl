package ColladaObjects;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Float_array
{
	private Node ThisNode;
	
	public String ID;
	public int Count;
	public float[] Arr;
	
	public Float_array(Node thisNode)
	{
		NamedNodeMap nnm = ThisNode.getAttributes();
		ID = nnm.getNamedItem("id").getNodeValue();
		Count = Integer.parseInt(nnm.getNamedItem("id").getNodeValue());
	}
}
