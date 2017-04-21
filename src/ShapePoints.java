public class ShapePoints
{
	public float[] x;
	public float[] y;
	public float[] z;
	public float[] texture;
	private int points;
	private int cur;
	
	public ShapePoints(int P)
	{
		points = P;
		x = new float[points];
		y = new float[points];
		z = new float[points];
		
		cur = 0;
		for(int a = 0; a < points; a++)
		{
			x[a] = 0f;
			y[a] = 0f;
			z[a] = 0f;
		}
	}
	
	public void xyz(float X, float Y, float Z)
	{
		x[cur] = X;
		y[cur] = Y;
		z[cur] = Z;
		cur++;
	}
	
	public int getCur()
	{
		return cur;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	
}
