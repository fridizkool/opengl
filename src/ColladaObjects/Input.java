package ColladaObjects;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Input
{
	public Node ThisNode;
	public int Semantic;
	public String Source;
	public int Offset;
	
	public Input(Node thisNode)
	{
		ThisNode = thisNode;
		
		NamedNodeMap nnm = ThisNode.getAttributes();
		
		
		if(nnm.getNamedItem("semantic").equals("VERTEX"))
			Semantic = 0;
		else if(nnm.getNamedItem("semantic").equals("NORMAL"))
			Semantic = 1;
		else if(nnm.getNamedItem("semantic").equals("TEXCOORD"))
			Semantic = 2;
		
		Source = nnm.getNamedItem("source").getNodeValue();
		Offset = Integer.parseInt(nnm.getNamedItem("offset").getNodeValue());
	}
}
