public class ShapePoints
{
	public float[] x;	//Vertexes
	public float[] y;
	public float[] z;
	
	public float[] s;	//Textures
	public float[] t;
	
	public float[] nx;	//Normals
	public float[] ny;
	public float[] nz;
	
	private int points;
	private int cur;
	private int tcur;
	private int ncur;
	
	public ShapePoints(int P)
	{
		points = P;
		x = new float[points];
		y = new float[points];
		z = new float[points];
		
		nx = new float[points];
		ny = new float[points];
		nz = new float[points];
		
		s = new float[points];
		t = new float[points];
		
		cur = 0;
		ncur = 0;
		tcur = 0;
		for(int a = 0; a < points; a++)
		{
			x[a] = 0f;
			y[a] = 0f;
			z[a] = 0f;
		}
	}
	
	public void Vxyz(float X, float Y, float Z)
	{
		x[cur] = X;
		y[cur] = Y;
		z[cur] = Z;
		cur++;
	}
	
	public void Tst(float S, float T)
	{
		s[tcur] = S;
		t[tcur] = T;
		tcur++;
	}
	
	public void Nxyz(float X, float Y, float Z)
	{
		nx[ncur] = X;
		ny[ncur] = Y;
		nz[ncur] = Z;
		ncur++;
	}
	
	public int getCur()
	{
		return cur;
	}
	
	public int getPoints()
	{
		return points;
	}

	public String toString()
	{
		String st = "";
		for(int a = 0; a < x.length; a++)
		{
			st += "Vertex: " + a + ": " + "X) " + x[a]
					+ " Y) " + y[a]
							+ " Z) " + z[a] + "\n";
			
			st += "Normal: " + a + ": " + "X) " + nx[a]
					+ " Y) " + ny[a]
							+ " Z) " + nz[a] + "\n";
			
			st += "Texture: " + a + ": " + "S) " + s[a]
					+ " T) " + t[a] + "\n\n";
		}
		
		return st;
	}
	
}
