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
	
	public Polylist[] Polys;
	
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
		Polys = new Polylist[collada.getPolys().size()];
		Polys = collada.getPolys().toArray(Polys);
	}
	
	public void Render(GL2 gl, int width, int height)
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
		
		ShapePoints d = null;
		float ratio = width / height;
//		gl.glFrustum(Polys[0].smallX * ratio, Polys[0].largeX * ratio, Polys[0].smallY, Polys[0].largeY, Polys[0].largeZ, Polys[0].smallZ);
		for(Polylist po : Polys)
		{
			d = po.next();
			while(po.hasNext())
			{
				if(d.getPoints() == 3)
					gl.glBegin(GL2.GL_TRIANGLES);
				else if(d.getPoints() == 4)
					gl.glBegin(GL2.GL_QUADS);
				else
					gl.glBegin(GL2.GL_POLYGON);
				
				for(int b = 0; b < d.getPoints(); b++)
				{
					gl.glColor3f(d.x[b] * ratio, d.y[b] * ratio, d.z[b] * ratio);
					gl.glVertex3f(d.x[b] * ratio, d.y[b] * ratio, d.z[b] * ratio);
				}
				d = po.next();
//				if(a != d.getPoints())
					gl.glEnd();
			}
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
