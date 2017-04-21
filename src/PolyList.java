import java.util.Iterator;

public class PolyList implements Iterator<ShapePoints>
{
	int[] vcount;
	int[] list;
	float[] vertex;
	float[] normal;
	float[] texture;
	ShapePoints[] polygons;
	
	public PolyList(int[] c, int[] l, float[] v, float[] n, float[] t, int inputs)
	{
		vcount = c;
		list = l;
		vertex = v;
		normal = n;
		texture = t;
		polygons = new ShapePoints[vcount.length];
		for(int x = 0; x < polygons.length; x++)
		{
			polygons[x] = new ShapePoints(vcount[x]);
		}
	}

	@Override
	public boolean hasNext()
	{
		return false;
	}

	@Override
	public ShapePoints next()
	{
		
		return null;
	}
}
