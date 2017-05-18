import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ColladaObjects.Geometry;

public class ColladaReader
{	
	private Document doc;
	private File file;
	
	private ArrayList<Geometry> Geos;
	
	private ArrayList<Polylist> polylist;
	
	public ColladaReader(String f)
	{
		doc = null;
		file = new File(f);
		read();
	}
	
	private void read()
	{
		DocumentBuilder db;
		try
		{
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(file);
			
			NodeList nl = doc.getElementsByTagName("*");
			System.out.println(nl.getLength());
			Geos = new ArrayList<Geometry>();
			polylist = new ArrayList<Polylist>();
			for(int x = 0; x < nl.getLength(); x++)
			{
				if(nl.item(x).getNodeName().equals("geometry"))
				{
					System.out.println("XXXXXXXXXXXXXXXXXXXXXX");
					Geos.add(new Geometry(nl.item(x)));
					polylist.add(new Polylist(Geos.get(Geos.size() - 1)));
				}
			}
		}
		catch (ParserConfigurationException | SAXException | IOException e){e.printStackTrace();}
	}
	
	public ArrayList<Geometry> getGeos()
	{
		return Geos;
	}
	
	public ArrayList<Polylist> getPolys()
	{
		return polylist;
	}
}
