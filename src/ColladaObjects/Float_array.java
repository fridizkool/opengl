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
		ThisNode = thisNode;
		NamedNodeMap nnm = ThisNode.getAttributes();
		ID = nnm.getNamedItem("id").getNodeValue();
		Count = Integer.parseInt(nnm.getNamedItem("count").getNodeValue());
		
		String s = ColladaTools.getText(ThisNode);
		
		while(s.length() > 0 && !ColladaTools.isNumeric(s.substring(0, 1)))
			s = s.substring(1);
		
		String[] floatstring = s.split("\\s+");
		Arr = new float[floatstring.length];
		for(int x = 0; x < Arr.length; x++)
		{
			//System.out.println(floatstring[x]);
			if(floatstring[x].length() > 0 && ColladaTools.isNumeric(floatstring[x]))
				Arr[x] = Float.parseFloat(floatstring[x]);
		}
	}
}
