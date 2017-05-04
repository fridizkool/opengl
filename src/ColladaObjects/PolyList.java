package ColladaObjects;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class PolyList
{
	private Node ThisNode;
	
	public String Material;
	public int Count;
	public int[] VCount;
	public int[] Polys;
	public Input[] Inputs;
	
	public PolyList(Node thisNode)
	{
		ThisNode = thisNode;
		NamedNodeMap nnm = ThisNode.getAttributes();
		Material = nnm.getNamedItem("material").getNodeValue();
		Count = Integer.parseInt(nnm.getNamedItem("count").getNodeValue());
		
		
	}
}
