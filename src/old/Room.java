package old;
import static com.jogamp.opengl.GL2.*;
import static com.jogamp.opengl.GL.*;
import java.awt.*;
import java.awt.event.*;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_PAGE_UP;
import static java.awt.event.KeyEvent.VK_PAGE_DOWN;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.*;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

@SuppressWarnings({ "serial", "unused" })
public class Room extends GLCanvas implements GLEventListener, KeyListener
{
	private GLU glu;
	
	private static float posX = 0.0f;
	private static float posZ = 0.0f;
	private static float zp = -5.0f;
	private static float xp = 0.0f;
	private static float yp = 0.0f;
//	private static float rotateSpeedX = 0.0f;
//	private static float rotateSpeedY = 0.0f;
//	private static float zIncrement = 0.02f;
//	private static float xIncrement = 0.02f;
//	private static float yIncrement = 0.02f;
	private static boolean blendingEnabled;
	
	Sector sector;
	private float headingY = 0;
	private float lookUpAngle = 0.0f;
	private float moveIncrement = 0.05f;
	private float turnIncrement = 1.5f;
	private float lookUpIncrement = 1.5f;
	
	private float walkBias = 0;
	private float walkBiasAngle = 0;
	
	private Texture[] textures = new Texture[3];
	private static int currTextureFilter = 0;
	private String textureFileName = "images/mud.png";
	private float textureTop, textureBottom, textureLeft, textureRight;
	private static boolean isLightOn;
	
	public Room()
	{
		this.addGLEventListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
	}
	
	@Override
	public void display(GLAutoDrawable draw)
	{
		GL2 gl = draw.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		render(gl);
	}

	@Override
	public void dispose(GLAutoDrawable auto)
	{}

	@Override
	public void init(GLAutoDrawable auto)
	{
		GL2 gl = auto.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(0.2f, 0.1f, 0.4f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);
		
		try
		{
			setupWorld();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource(textureFileName));
			textures[0] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			
			textures[1] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			
			textures[2] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, true);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			
			TextureCoords textureCoords = textures[0].getImageTexCoords();
			textureTop = textureCoords.top();
			textureBottom = textureCoords.bottom();
			textureLeft = textureCoords.left();
			textureRight = textureCoords.right();
		}catch (GLException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
		
		float[] lightAmbientValue = {0.5f, 0.5f, 0.5f, 1.0f};
		float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] lightDiffusePosition = {0.0f, 0.0f, 2.0f, 1.0f};
		gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightAmbientValue, 0);
		gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, lightDiffuseValue, 0);
		gl.glLightfv(GL_LIGHT1, GL_POSITION, lightDiffusePosition, 0);
		gl.glEnable(GL_LIGHT1);    // Enable Light-1
		gl.glDisable(GL_LIGHTING); // But disable lighting
		isLightOn = false;
		
		gl.glEnable(GL_TEXTURE_2D);
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		gl.glEnable(GL_BLEND);
		gl.glDisable(GL_DEPTH_TEST);
		blendingEnabled = true;
	}

	@Override
	public void reshape(GLAutoDrawable auto, int x, int y, int 	width, int height)
	{
		GL2 gl = auto.getGL().getGL2();
		if (height == 0) height = 1;
		float aspect = (float)width/height;
		
		gl.glViewport(0, 0, width, height);
		
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0, aspect, 0.1, 100.0);
		
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		
	}
	
	public void render(GL2 gl)
	{	
		gl.glLoadIdentity();
		
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
		
		gl.glRotatef(lookUpAngle, 1.0f, 0, 0);
		gl.glRotatef(360.0f - headingY, 0, 1.0f, 0);
		gl.glTranslatef(-posX, -walkBias - 0.25f, -posZ);

		textures[currTextureFilter].enable(gl);
		textures[currTextureFilter].bind(gl);
		for(int i = 0; i < sector.triangles.length; i++)
		{
			gl.glBegin(GL_TRIANGLES);
			gl.glNormal3f(0.0f, 0.0f, 1.0f);
			float textureHeight = textureTop - textureBottom;
			float u = sector.triangles[i].vertices[0].u, v = (sector.triangles[i].vertices[0].v * textureHeight - textureBottom);
			gl.glTexCoord2f(u, v);
			gl.glVertex3f(sector.triangles[i].vertices[0].x, sector.triangles[i].vertices[0].y, sector.triangles[i].vertices[0].z);
			
			u = sector.triangles[i].vertices[1].u;
			v = sector.triangles[i].vertices[1].v * textureHeight - textureBottom;
			gl.glTexCoord2f(u, v);
			gl.glVertex3f(sector.triangles[i].vertices[1].x, sector.triangles[i].vertices[1].y, sector.triangles[i].vertices[1].z);
			
			u = sector.triangles[i].vertices[2].u;
			v = sector.triangles[i].vertices[2].v * textureHeight - textureBottom;
			gl.glTexCoord2f(u, v);
			gl.glVertex3f(sector.triangles[i].vertices[2].x, sector.triangles[2].vertices[1].y, sector.triangles[i].vertices[2].z);
			
			gl.glEnd();
		}
	}
	
	private void setupWorld() throws IOException
	{
		BufferedReader in = null;
		
		try
		{
			in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("models/world.txt")));
			String line = in.readLine();
			while((line = in.readLine()) != null)
			{
				if(line.trim().length() == 0 || line.trim().startsWith("//"))
					continue;
				if(line.startsWith("NUMPOLLIES"))
				{
					int t = Integer.parseInt(line.substring(line.indexOf("NUMPOLLIES") + "NUMPOLLIES".length() + 1));
					sector = new Sector(t);
					break;
				}
				
				for(int i = 0; i < sector.triangles.length; i++)
				{
					for(int vert = 0; vert < 3; vert++)
					{
						line = in.readLine();
						while((line = in.readLine()) != null)
						{
							if(line.trim().length() == 0 || line.trim().startsWith("//"))
								continue;
							break;
						}
						if(line != null)
						{
							Scanner scanner = new Scanner(line);
							sector.triangles[i].vertices[vert].x = scanner.nextFloat();
							sector.triangles[i].vertices[vert].y = scanner.nextFloat();
							sector.triangles[i].vertices[vert].z = scanner.nextFloat();
							sector.triangles[i].vertices[vert].u = scanner.nextFloat();
							sector.triangles[i].vertices[vert].v = scanner.nextFloat();
							scanner.close();
						}
					}
				}
			}
		}
		finally
		{
			if(in != null)
				in.close();
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		switch(keyCode)
		{
			case VK_W:
				posX -= (float)Math.sin(Math.toRadians(headingY)) * moveIncrement;
				posZ -= (float)Math.cos(Math.toRadians(headingY)) * moveIncrement;
				walkBiasAngle = (walkBiasAngle >= 359.0f) ? 0.0f : walkBiasAngle + 10.0f;
				walkBias = (float)Math.sin(Math.toRadians(walkBiasAngle)) / 20.0f;
//				rotateSpeedX -= rotateSpeedXIncrement;
				break;
			case VK_S:
				posX += (float)Math.sin(Math.toRadians(headingY)) * moveIncrement;
				posZ += (float)Math.cos(Math.toRadians(headingY)) * moveIncrement;
				walkBiasAngle = (walkBiasAngle <= 1.0f) ? 359.0f : walkBiasAngle - 10.0f;
				walkBias = (float)Math.sin(Math.toRadians(walkBiasAngle)) / 20.0f;
//				rotateSpeedX += rotateSpeedXIncrement;
				break;
			case VK_A:
				headingY += turnIncrement;
//				rotateSpeedY -= rotateSpeedYIncrement;
				break;
			case VK_D:
				headingY -= turnIncrement;
//				rotateSpeedY += rotateSpeedYIncrement;
				break;
			case VK_PAGE_UP:
				lookUpAngle -= lookUpIncrement;
//				zp -= zIncrement;
				break;
			case VK_PAGE_DOWN:
				lookUpAngle += lookUpIncrement;
//				zp += zIncrement;
				break;
			case VK_UP:
//				yp += yIncrement;
				break;
			case VK_DOWN:
//				yp -= yIncrement;
				break;
			case VK_LEFT:
//				xp -= xIncrement;
				break;
			case VK_RIGHT:
//				xp += xIncrement;
				break;
			case VK_L:
				isLightOn = !isLightOn;
				break;
			case VK_B:
				blendingEnabled = !blendingEnabled;
				break;
			case VK_F:
				currTextureFilter = (currTextureFilter + 1) % textures.length;
				break;
			case VK_R:
				isLightOn = false;
				blendingEnabled = true;
				currTextureFilter = 0;
//				rotateSpeedX = 0.0f;
//				rotateSpeedY = 0.0f;
				xp = 0.0f;
				yp = 0.0f;
				zp = -5.0f;
				break;
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}

class Sector {
   Triangle[] triangles;

   // Constructor
   public Sector(int numTriangles) {
      triangles = new Triangle[numTriangles];
      for (int i = 0; i < numTriangles; i++) {
         triangles[i] = new Triangle();
      }
   }
}

class Triangle {
   Vertex[] vertices = new Vertex[3];

   public Triangle() {
      vertices[0] = new Vertex();
      vertices[1] = new Vertex();
      vertices[2] = new Vertex();
   }
}

class Vertex {
   float x, y, z; // 3D x,y,z location
   float u, v; // 2D texture coordinates

   public String toString() {
      return "(" + x + "," + y + "," + z + ")" + "(" + u + "," + v + ")";
   }
}