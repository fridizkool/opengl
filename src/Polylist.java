import java.util.ArrayList;
import java.util.Iterator;

import ColladaObjects.Geometry;
import ColladaObjects.Mesh;
import ColladaObjects.PolyList;
import ColladaObjects.Source;

public class Polylist implements Iterator<ShapePoints>
{
	private Geometry Geo;
	private Mesh Me;
	private ArrayList<Source> Src;
	private PolyList polylist;
	
	private int[] vcount;
	private int[] list;
	public ShapePoints vertex;
	private ShapePoints normal;
	private ShapePoints texture;
	private ShapePoints[] polygons;
	private int cur;
	
	public static float largeX = Integer.MIN_VALUE;
	public static float largeY = Integer.MIN_VALUE;
	public static float largeZ = Integer.MIN_VALUE;
	
	public static float smallX = Integer.MAX_VALUE;
	public static float smallY = Integer.MAX_VALUE;
	public static float smallZ = Integer.MAX_VALUE;
	
	
	public Polylist(Geometry geo)
	{
		Geo = geo;
		
		Me = Geo.mesh;
		Src = Me.source;
		polylist = Me.polylist;
		
		create(polylist.VCount, polylist.Polys, Src.get(0).floatarray.Arr, Src.get(1).floatarray.Arr, Src.get(2).floatarray.Arr, polylist.Inputs.length);
	}
	
	public void create(int[] co, int[] pol, float[] v, float[] n, float[] t, int inputs)
	{
		cur = -1;
		vcount = co;
		list = pol;
		polygons = new ShapePoints[vcount.length];
		
		int current = 0;
		
		vertex = createV(v);
		normal = createN(n);
		texture = createT(t);
		System.out.println(inputs);
		for(int x = 0; x < polygons.length; x++)
		{
			polygons[x] = new ShapePoints(vcount[x]);
			for(int y = current; y < list.length && y < current + (inputs * vcount[x]); y+=inputs)
			{
				//System.out.println(y);
				if(inputs > 0)
					polygons[x].Vxyz(vertex.x[list[y]], vertex.y[list[y]], vertex.z[list[y]]);	//Create vertexes
//				if(inputs > 1)
//					polygons[x].Nxyz(normal.x[list[y + 1]], normal.y[list[y + 1]], normal.z[list[y + 1]]);	//Create normals
//				if(inputs > 2)
//					polygons[x].Tst(texture.s[list[y + 2]], texture.t[list[y + 2]]);	//Create textures
			}
			current += inputs * vcount[x];
//			System.out.println(polygons[x].toString());
		}
		
	}

	@Override
	public boolean hasNext()
	{
		return cur < (polygons.length - 1);
	}

	@Override
	public ShapePoints next()
	{
		cur++;
		if(cur >= polygons.length)
			cur = 0;
		return polygons[cur];
	}
	
	private ShapePoints createV(float[] i)	//Create points with XYZ
	{
		ShapePoints n = new ShapePoints(i.length / 3);
		int y = 0;
		for(int x = 0; x < n.getPoints(); x++)
		{
			while(y < i.length && n.getCur() < n.getPoints())
			{
				n.Vxyz(i[y], i[y + 1], i[y + 2]);
				
				if(i[y] > largeX)
					largeX = i[y];
				if(i[y + 1] > largeY)
					largeY = i[y + 1];
				if(i[y + 2] > largeZ)
					largeZ = i[y + 2];
				
				if(i[y] < smallX)
					smallX = i[y];
				if(i[y + 1] < smallY)
					smallY = i[y + 1];
				if(i[y + 2] < smallZ)
					smallZ = i[y + 2];
				
				y += 3;
			}
			
		}
		
		return n;
	}
	
	private ShapePoints createN(float[] i)	//Create points with XYZ
	{
		ShapePoints n = new ShapePoints(i.length / 3);
		int y = 0;
		for(int x = 0; x < n.getPoints(); x++)
		{
			while(y < i.length && n.getCur() < n.getPoints())
			{
				n.Vxyz(i[y], i[y + 1], i[y + 2]);
				
				y += 3;
			}
			
		}
		
		return n;
	}
	
	private ShapePoints createT(float[] i)	//Create points with ST
	{
		ShapePoints n = new ShapePoints(i.length / 2);
		int y = 0;
		for(int x = 0; x < n.getPoints(); x++)
		{
			while(y < i.length && n.getCur() < n.getPoints())
			{
				n.Tst(i[y], i[y + 1]);
				y += 2;
			}
			
		}
		
		return n;
	}
}
