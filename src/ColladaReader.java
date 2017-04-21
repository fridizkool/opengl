import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ColladaReader
{	
	private Document doc;
	private File file;
	
	private float[] vertex;
	private float[] normal;
	private float[] texture;
	private int[] polyCount;
	private int[] polys;
	private PolyList pl;
	
	private float transX;
	private float transY;
	private float transZ;
	
	private float angleX;
	private float angleY;
	private float angleZ;
	
	private Color diffuseColor;
	private Color specularColor;
	private float shininess;
	
	
	
	public ColladaReader(String f)
	{
		doc = null;
		vertex = new float[1];
		normal = new float[1];
		texture = new float[1];
		polys = new int[1];
		file = new File(f);
		
		angleX = 0;
		angleY = 0;
		angleZ = 0;
		
		transX = 0;
		transY = 0;
		transZ = 0;
		
		diffuseColor = Color.white;
		specularColor = Color.white;
		shininess = 0;
		
		read();
	}
	
	private void read()
	{
		try
		{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(file);
			
			NodeList nl = doc.getElementsByTagName("float_array");
			if(nl.getLength() > 0)
			{
				readVertex(nl.item(0));
				if(nl.getLength() > 1)
				{
					readNormal(nl.item(1));
					if(nl.getLength() > 2)
						readTexture(nl.item(2));
				}
			}
			
			nl = doc.getElementsByTagName("polylist");
			int count = 0;
			for(int x = 0; x < nl.getLength(); x++)
			{
				if(nl.item(x).getNodeName().equals("Input"))
					count++;
				else if(nl.item(x).getNodeName().equals("vcount"));
					
			}
			
		}
		catch (SAXException | IOException | ParserConfigurationException e){e.printStackTrace();}
	}
	
	private void readVertex(Node n)
	{
		String s = getText(n);
		while(s.length() > 0 && !isNumeric(s.substring(0, 1)))
			s = s.substring(1);
		
		String[] floatstring = s.split("\\s+");
		vertex = new float[floatstring.length];
		for(int x = 0; x < vertex.length; x++)
		{
			//System.out.println(floatstring[x]);
			if(floatstring[x].length() > 0 && isNumeric(floatstring[x]))
				vertex[x] = Float.parseFloat(floatstring[x]);
		}
	}
	
	private void readNormal(Node n)
	{
		String s = getText(n);
		while(s.length() > 0 && !isNumeric(s.substring(0, 1)))
			s = s.substring(1);
		
		String[] floatstring = s.split("\\s+");
		normal = new float[floatstring.length];
		for(int x = 0; x < normal.length; x++)
		{
			//System.out.println(floatstring[x]);
			if(floatstring[x].length() > 0 && isNumeric(floatstring[x]))
				normal[x] = Float.parseFloat(floatstring[x]);
		}
	}
	
	private void readTexture(Node n)
	{
		String s = getText(n);
		while(s.length() > 0 && !isNumeric(s.substring(0, 1)))
			s = s.substring(1);
		
		String[] floatstring = s.split("\\s+");
		texture = new float[floatstring.length];
		for(int x = 0; x < texture.length; x++)
		{
			//System.out.println(floatstring[x]);
			if(floatstring[x].length() > 0 && isNumeric(floatstring[x]))
				texture[x] = Float.parseFloat(floatstring[x]);
		}
	}

	private void readVCount(Node n)
	{
		String s = getText(n);
		while(s.length() > 0 && !isNumeric(s.substring(0, 1)))
			s = s.substring(1);
		
		String[] floatstring = s.split("\\s+");
		polyCount = new int[floatstring.length];
		for(int x = 0; x < texture.length; x++)
		{
			//System.out.println(floatstring[x]);
			if(floatstring[x].length() > 0 && isNumeric(floatstring[x]))
				texture[x] = Float.parseFloat(floatstring[x]);
		}
	}
	
	public float[] getVertex()
	{
		return vertex;
	}
	
	public float[] getNormal()
	{
		return normal;
	}
	
	public float[] getTexture()
	{
		return texture;
	}
	
	private static String getText(Node node)
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
	
	public static boolean isNumeric(String s)
	{  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	}
}
