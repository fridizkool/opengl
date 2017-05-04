import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Model
{
	public float angleX;
	public float angleY;
	public float angleZ;
	
	public float xp;
	public float yp;
	public float zp;
	
	public ShapePoints[] triangles;
	public ShapePoints[] quads;
	public ShapePoints[] polygons;
	public int[] polyList;
	public int currentType;
	
	public Texture[] textures;
	public int currTextureFilter;
	public boolean blendingEnabled;
	public boolean isLightOn;
	
	public float rotateSpeedX;
	public float rotateSpeedY;
	public float rotateSpeedZ;
	
	public Polylist p;
	
	private ColladaReader collada;
	
	public Model()
	{
		angleX = 0;
		angleY = 0;
		angleZ = 0;
		
		xp = 0;
		yp = 0;
		zp = -5;
		
		rotateSpeedX = 0.0f;
		rotateSpeedY = 0.0f;
		rotateSpeedZ = 0.0f;
		
		triangles = new ShapePoints[1];
		triangles[0] = new ShapePoints(3);
		currentType = GL.GL_TRIANGLES;
		
		textures = new Texture[3];
		currTextureFilter = 0;
		blendingEnabled = false;
		isLightOn = false;
	}
	
	public void createModel(String f)
	{
		collada = new ColladaReader(f);
		float[] floats = collada.getVertex();
		
//		if(floats.length % 3 == 0)
//		{
//			if(floats.length % 9 == 0)
//				triangleModel(floats);
//			else if(floats.length % 12 == 0)
//				quadModel(floats);
//			else
//				polygonModel(floats, GCF(floats.length / 3));
//		}
//		else
//			System.out.println("impossible");
//		
//		System.out.println(triangles.length);
//		System.out.println(floats.length + " : " + (floats.length / 3) + " : " + GCF(floats.length / 3));
	}
	
	public void triangleModel(float[] floats)
	{
		if(floats.length % 9 != 0)
		{
			System.out.println("Not a triangle");
			return;
		}
		System.out.println("TRIANGLE");
		triangles = new ShapePoints[floats.length / 9];
		int y = 0;
		
		for(int x = 0; x < triangles.length; x++)
		{
			triangles[x] = new ShapePoints(3);
			while(y < floats.length && triangles[x].getCur() < 3)
			{
				System.out.println("Y: x " + floats[y] + ", y " + floats[y + 1] + ", z " + floats[y + 2]);
				triangles[x].Vxyz(floats[y], floats[y + 1], floats[y + 2]);
				y += 3;
			}
			
		}
		
		currentType = GL2.GL_TRIANGLES;
	}
	
	public void quadModel(float[] floats)
	{
		if(floats.length % 12 != 0)
		{
			System.out.println("Not a quad");
			return;
		}
		System.out.println("QUAD");
		triangles = new ShapePoints[floats.length / 12];
		int y = 0;
		
		for(int x = 0; x < triangles.length; x++)
		{
			triangles[x] = new ShapePoints(4);
			while(triangles[x].getCur() < 4)
			{
				triangles[x].Vxyz(floats[y], floats[y + 1], floats[y + 2]);
				y += 3;
			}
		}
		
		currentType = GL2.GL_QUADS;
	}
	
	public void polygonModel(float[] floats, int verts)
	{
		if(floats.length % (verts * 3) != 0)
		{
			System.out.println("Not the right polygon");
			return;
		}
		System.out.println("POLYGON");
		triangles = new ShapePoints[floats.length / (verts * 3)];
		int y = 0;
		
		for(int x = 0; x < triangles.length; x++)
		{
			triangles[x] = new ShapePoints(verts);
			while(triangles[x].getCur() < 3)
			{
				triangles[x].Vxyz(floats[y], floats[y + 1], floats[y + 2]);
				y += 3;
			}
		}
		
		currentType = GL2.GL_POLYGON;
	}
	
	public void Render(GL2 gl)
	{
		gl.glLoadIdentity();
		gl.glTranslatef(xp, yp, zp);
		gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f);
		
		if(isLightOn)
			gl.glEnable(GL_LIGHTING);
		else
			gl.glDisable(GL_LIGHTING);
		if(blendingEnabled)
		{
			gl.glEnable(GL_BLEND);
			gl.glDisable(GL_DEPTH_TEST);
		}
		else
		{
			gl.glDisable(GL_BLEND);
			gl.glEnable(GL_DEPTH_TEST);
		}
		
		p = collada.getPoly();
		ShapePoints d = p.next();
		
		while(p.hasNext())
		{
			if(d.getPoints() == 3)
				gl.glBegin(GL2.GL_TRIANGLES);
			else if(d.getPoints() == 4)
				gl.glBegin(GL2.GL_QUADS);
			else
				gl.glBegin(GL2.GL_POLYGON);
			for(int b = 0; b < d.getPoints(); b++)
			{
				gl.glColor3f(d.x[b], d.y[b], d.z[b]);
				gl.glVertex3f(d.x[b], d.y[b], d.z[b]);
			}
			int a = d.getPoints();
			d = p.next();
			if(a != d.getPoints())
				gl.glEnd();
		}
		gl.glEnd();
		
		angleX += rotateSpeedX;
		angleY += rotateSpeedY;
		angleZ += rotateSpeedZ;
	}
	
	public int GCF(int l)
	{
		int fact = 1;
		for(int x = 2; x < l; x++)
			if(l % x == 0)
				fact = x;
		return fact;
	}
}
