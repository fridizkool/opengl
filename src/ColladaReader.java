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

@SuppressWarnings("unused")
public class ColladaReader
{	
	private Document doc;
	private File file;
	
	private float[] vertex;
	private float[] normal;
	private float[] texture;
	private int[] polyList;
	
	private float transX;
	private float transY;
	private float transZ;
	
	private float angleX;
	private float angleY;
	private float angleZ;
	
	private Color diffuseColor;
	private Color specularColor;
	private float shininess;
	
	private Polylist polylist;
	
	public ColladaReader(String f)
	{
		doc = null;
		vertex = new float[1];
		normal = new float[1];
		texture = new float[1];
		polyList = new int[1];
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
		DocumentBuilder db;
		try
		{
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(file);
			readHelper();
		}
		catch (ParserConfigurationException | SAXException | IOException e){e.printStackTrace();}
		
			
	}
	
	private void readHelper()
	{
		NodeList nl = doc.getElementsByTagName("float_array");
		readVertex(nl.item(0));
		readNormal(nl.item(1));
		readTexture(nl.item(2));
		
		if(doc.getElementsByTagName("polylist").getLength() == 0)
			return;
		nl = doc.getElementsByTagName("polylist");
		nl = nl.item(0).getChildNodes();
		
		
		int inputs = 0;
		int[] count = new int[1];
		int[] p = new int[1];
		String s;
		String[] st;
		
		for(int x = 0; x < nl.getLength(); x++)
		{
			Node n = nl.item(x);
			
			if(n.getNodeName().equals("input"))
				inputs++;
			else if(n.getNodeName().equals("vcount"))
			{
				s = getText(n);
				while(s.length() > 0 && !isNumeric(s.substring(0, 1)))
					s = s.substring(1);
				st = s.split("\\s+");
				
				count = new int[st.length];
				for(int y = 0; y < count.length; y++)
					if(isNumeric(st[y]) && st[y].length() > 0)
						count[y] = Integer.parseInt(st[y]);
			}
			else if(n.getNodeName().equals("p"))
			{
				s = getText(n);
				while(s.length() > 0 && !isNumeric(s.substring(0, 1)))
					s = s.substring(1);
				st = s.split("\\s+");
				
				p = new int[st.length];
				for(int y = 0; y < p.length; y++)
					if(isNumeric(st[y]) && st[y].length() > 0)
						p[y] = Integer.parseInt(st[y]);
			}
		}
		
		polylist = new Polylist(count, p, vertex, normal, texture, inputs);
		
		
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
			if(floatstring[x].length() > 0 && isNumeric(floatstring[x]))
				normal[x] = Float.parseFloat(floatstring[x]);
			//System.out.println(normal[x]);
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
	
	public static float[] read(String f)
	{
		String s = "";
		Document doc;
		float[] points = new float[1];
		try
		{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(new File(f));
			
			NodeList nl = doc.getElementsByTagName("float_array");
			s = getText(nl.item(1));
			
			if(!isNumeric(s.substring(0, 1)))
				s = s.substring(1);
			
			String[] floatstring = s.split("[ \n]");
			points = new float[floatstring.length];
			System.out.println(points.length);
			for(int x = 0; x < points.length; x++)
			{
				if(isNumeric(floatstring[x]))
					points[x] = Float.parseFloat(floatstring[x]);
			}
		}
		catch (SAXException | IOException | ParserConfigurationException e){e.printStackTrace();}
		
		return points;
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
	
	public Polylist getPoly()
	{
		return polylist;
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
