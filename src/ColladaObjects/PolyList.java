package ColladaObjects;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		try
		{
			Material = nnm.getNamedItem("material").getNodeValue();
		}
		catch(NullPointerException e)
		{
			System.out.println("No material");
		}
		Count = Integer.parseInt(nnm.getNamedItem("count").getNodeValue());
		
		NodeList nl = ThisNode.getChildNodes();
		int ins = 0;
		for(int x = 0; x < nl.getLength(); x++)
		{
			if(nl.item(x).getNodeName().equals("input"))
				ins++;
		}
		System.out.println(Count);
		VCount = new int[Count];
		
		Inputs = new Input[ins];
		String s = "";
		int y = 0;
		for(int x = 0; x < nl.getLength(); x++)
		{
			s = ColladaTools.getText(nl.item(x));
			if(nl.item(x).getNodeName().equals("input"))
			{
				Inputs[y] = new Input(nl.item(x));
				y++;
			}
			else if(nl.item(x).getNodeName().equals("vcount"))
				VCount = readArr(s);
			else if(nl.item(x).getNodeName().equals("p"))
				Polys = readArr(s);
		}
	}
	
	private int[] readArr(String s)
	{
		while(s.length() > 0 && !ColladaTools.isNumeric(s.substring(0, 1)))
			s = s.substring(1);
		
		String[] floatstring = s.split("\\s+");
		int[] Arr = new int[floatstring.length];
		
		for(int x = 0; x < Arr.length; x++)
		{
			//System.out.println(floatstring[x]);
			if(floatstring[x].length() > 0 && ColladaTools.isNumeric(floatstring[x]))
				Arr[x] = Integer.parseInt(floatstring[x]);
		}
		
		return Arr;
	}
}
