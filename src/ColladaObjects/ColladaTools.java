package ColladaObjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ColladaTools
{
	protected static String getText(Node node)
	{
		String str = "";
		
		if(!node.hasChildNodes())
			return "";
		
		NodeList nl = node.getChildNodes();
		
		for(int x = 0; x < nl.getLength(); x++)
		{
			Node n = nl.item(x);
			
			if(n.getNodeType() == Node.TEXT_NODE)
				str += n.getNodeValue();
			else if(n.getNodeType() == Node.CDATA_SECTION_NODE)
				str += n.getNodeValue();
			else if(n.getNodeType() == Node.ENTITY_REFERENCE_NODE)
				str += getText(n);
		}
		
		return str;
	}
	
	protected static boolean isNumeric(String s)
	{  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	}
}
