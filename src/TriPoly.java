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
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_PAGE_UP;
import static java.awt.event.KeyEvent.VK_PAGE_DOWN;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_SHIFT;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.GLReadBufferUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

@SuppressWarnings({ "serial", "unused" })
public class TriPoly extends GLCanvas implements GLEventListener, KeyListener, MouseMotionListener, MouseListener
{
	private GLU glu;
	
	private static float angleX = 0.0f;
	private static float angleY = 0.0f;
	private static float angleZ = 0.0f;
	private static float zp = -8.0f;
	private static float xp = -3.0f;
	private static float yp = 0.0f;
	
	private static float rotateSpeedX = 0.0f;
	private static float rotateSpeedY = 0.0f;
	private static float rotateSpeedZ = 0.0f;
	private static float zIncrement = 0.02f;
	private static float xIncrement = 0.02f;
	private static float yIncrement = 0.02f;
	
	private static float rotateSpeedXIncrement = 0.01f;
	private static float rotateSpeedYIncrement = 0.01f;
	private static float rotateSpeedZIncrement = 0.01f;
	private static boolean blendingEnabled;
	private Model m = new Model();
	
	private static int vert = 3;
	private static ShapePoints sp = new ShapePoints(3);
	
	private static Color curColor = new Color(254, 50, 128);
	
	private Texture[] textures = new Texture[3];
	private static int currTextureFilter = 0;
	private String textureFileName = "images/kitty face rawr.png";
	private float textureTop, textureBottom, textureLeft, textureRight;
	private static boolean isLightOn;
	
	private boolean moveModel = true;
	
	public TriPoly()
	{
		this.addGLEventListener(this);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		this.requestFocus();
		
//		m.createModel("aaa.dae");
//		m.createModel("plane.dae");
		m.createModel("met.dae");
//		m.createModel("cow.dae");
//		m.createModel("Table.dae");
	}
	
	@Override
	public void display(GLAutoDrawable draw)
	{
		render(draw);
	}

	@Override
	public void dispose(GLAutoDrawable auto){}

	@Override
	public void init(GLAutoDrawable auto)
	{
		GL2 gl = auto.getGL().getGL2();
		glu = new GLU();
		
		gl.glClearColor((float)0.5f, (float)0.5f, (float)0.5f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);
		
//		try
//		{
//			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource(textureFileName));
//			textures[0] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
//			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//			
//			textures[1] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
//			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//			
//			textures[2] = AWTTextureIO.newTexture(GLProfile.getDefault(), image, true);
//			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
//			
//			TextureCoords textureCoords = textures[0].getImageTexCoords();
//			textureTop = textureCoords.top();
//			textureBottom = textureCoords.bottom();
//			textureLeft = textureCoords.left();
//			textureRight = textureCoords.right();
//		}catch (GLException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
		
		float[] lightAmbientValue = {0.5f, 0.5f, 0.5f, 1.0f};
		float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] lightDiffusePosition = {0.0f, 0.0f, 2.0f, 1.0f};
		gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightAmbientValue, 0);
		gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, lightDiffuseValue, 0);
		gl.glLightfv(GL_LIGHT1, GL_POSITION, lightDiffusePosition, 0);
		gl.glEnable(GL_LIGHT1);    // Enable Light-1
		gl.glDisable(GL_LIGHTING); // But disable lighting
		isLightOn = false;
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		gl.glDisable(GL_BLEND);
		gl.glEnable(GL_DEPTH_TEST);
		blendingEnabled = false;
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
	
	public void render(GLAutoDrawable draw)
	{
		GL2 gl = draw.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
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
		
		gl.glViewport(0, 0, this.getWidth(), this.getHeight());
		
		shapeSphere(vert, 3, gl);
		
		//m.Render(gl, this.getWidth(), this.getHeight());
		
		angleX += rotateSpeedX;
		angleY += rotateSpeedY;
		angleZ += rotateSpeedZ;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		switch(keyCode)
		{
			case VK_W:
				if(!moveModel)
					rotateSpeedX -= rotateSpeedXIncrement;
				else
					m.rotateSpeedX -= rotateSpeedXIncrement;
				break;
			case VK_S:
				if(!moveModel)
					rotateSpeedX += rotateSpeedXIncrement;
				else
					m.rotateSpeedX += rotateSpeedXIncrement;
				break;
			case VK_A:
				if(!moveModel)
					rotateSpeedY -= rotateSpeedYIncrement;
				else
					m.rotateSpeedY -= rotateSpeedYIncrement;
				break;
			case VK_D:
				if(!moveModel)
					rotateSpeedY += rotateSpeedYIncrement;
				else
					m.rotateSpeedY += rotateSpeedYIncrement;
				break;
			case VK_E:
				if(!moveModel)
					rotateSpeedZ += rotateSpeedZIncrement;
				else
					m.rotateSpeedZ += rotateSpeedZIncrement;
				break;
			case VK_Q:
				if(!moveModel)
					rotateSpeedZ -= rotateSpeedZIncrement;
				else
					m.rotateSpeedZ -= rotateSpeedZIncrement;
				break;
			case VK_PAGE_UP:
				if(!moveModel)
					zp -= zIncrement;
				else
					m.zp -= zIncrement;
				break;
			case VK_PAGE_DOWN:
				if(!moveModel)
					zp += zIncrement;
				else
					m.zp += zIncrement;
				break;
			case VK_UP:
				if(!moveModel)
					yp += yIncrement;
				else
					m.yp += yIncrement;
				break;
			case VK_DOWN:
				if(!moveModel)
					yp -= yIncrement;
				else
					m.yp -= yIncrement;
				break;
			case VK_LEFT:
				if(!moveModel)
					xp -= xIncrement;
				else
					m.xp -= xIncrement;
				break;
			case VK_RIGHT:
				if(!moveModel)
					xp += xIncrement;
				else
					m.xp += xIncrement;
				break;
			case VK_L:
				if(!moveModel)
					isLightOn = !isLightOn;
				else
					m.isLightOn = !m.isLightOn;
				break;
			case VK_B:
				if(!moveModel)
					blendingEnabled = !blendingEnabled;
				else
					m.blendingEnabled = !m.blendingEnabled;
				break;
			case VK_F:
				currTextureFilter = (currTextureFilter + 1) % textures.length;
				break;
			case VK_R:
				currTextureFilter = 0;
				if(!moveModel)
				{
					isLightOn = false;
					blendingEnabled = true;
					rotateSpeedX = 0.0f;
					rotateSpeedY = 0.0f;
					rotateSpeedZ= 0.0f;
					xp = -3.0f;
					yp = 0.0f;
					zp = -8.0f;
					angleX = 0.0f;
					angleY = 0.0f;
					angleZ = 0.0f;
					vert = 3;
				}
				else
				{
					m.isLightOn = false;
					m.blendingEnabled = true;
					m.rotateSpeedX = 0.0f;
					m.rotateSpeedY = 0.0f;
					m.rotateSpeedZ= 0.0f;
					m.xp = 3.0f;
					m.yp = 0.0f;
					m.zp = -8.0f;
					m.angleX = 0.0f;
					m.angleY = 0.0f;
					m.angleZ = 0.0f;
				}
				break;
			case VK_MINUS:
				if(vert > 3)
					vert--;
				break;
			case VK_EQUALS:
				vert++;
				break;
			case VK_SHIFT:
				if(!moveModel)
				{
					rotateSpeedX = 0.0f;
					rotateSpeedY = 0.0f;
					rotateSpeedZ = 0.0f;
				}
				else
				{
					m.rotateSpeedX = 0.0f;
					m.rotateSpeedY = 0.0f;
					m.rotateSpeedZ = 0.0f;
				}
				break;
			case VK_M:
				moveModel = !moveModel;
				break;
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	public void shapeSphere(int sides, int p, GL2 gl)
	{
		gl.glBegin(GL_TRIANGLES);
		double full = 2 * Math.PI;
		double incriment = full / sides;
		ShapePoints[] points = new ShapePoints[sides * 2];
		
		//Upwards
		for(int a = 0; a < points.length; a++)
		{
			points[a] = new ShapePoints(p);
			//x coordinates
			points[a].x[0] = (float) Math.cos(incriment * a);
			points[a].x[1] = (float) Math.cos(incriment * (a + 1));
			points[a].x[2] = 0.0f;
			
			//y coordinates
			points[a].y[0] = 0.0f;
			points[a].y[1] = 0.0f;
			if(a < sides)
				points[a].y[2] = 1.0f;
			else
				points[a].y[2] = -1.0f;
			
			//z coordinates
			points[a].z[0] = (float) Math.sin(incriment * a);
			points[a].z[1] = (float) Math.sin(incriment * (a + 1));
			points[a].z[2] = 0.0f;
		}
		
		for(ShapePoints a : points)
		{
			for(int b = 0; b < 3; b++)
			{
				gl.glColor3f(a.x[b], a.y[b], a.z[b]);
				gl.glVertex3f(a.x[b], a.y[b], a.z[b]);
			}
		}
		gl.glEnd();
	}
	
	/*private void save()
	{
		try
		{
			GL2 gl = this.getGL().getContext().getGL().getGL2();
			gl.glLoadIdentity();
			gl.glBegin(GL_QUADS);
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			gl.glColor3f(0.4f, 0.2f, 0.5f);
			gl.glVertex3f(-1.0f, -1.0f, 1.0f);
			gl.glVertex3f(1.0f, -1.0f, 1.0f);
			gl.glVertex3f(1.0f, -1.0f, -1.0f);
			gl.glVertex3f(-1.0f, -1.0f, -1.0f);
			gl.glEnd();
			BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = null;
			g = bi.getGraphics();
			ByteBuffer buffer = GLBuffers.newDirectByteBuffer(this.getWidth() * this.getHeight() * 4);
			gl.glReadBuffer(GL_BACK);
			gl.glPixelStorei(GL_PACK_ALIGNMENT, 1);
			
			gl.glReadPixels(0, 0, this.getWidth(), this.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			for(int h = 0; h < this.getHeight(); h++)
			{
				for(int w = 0; w < this.getWidth(); w++)
				{
					g.setColor(new Color((buffer.get()), (buffer.get()), (buffer.get())));
					buffer.get();
					g.drawRect(w, this.getHeight() - h, 1, 1);
					if(g.getColor().getRed() != 0 || g.getColor().getGreen() != 0 || g.getColor().getBlue() != 0)
						System.out.println(g.getColor());
				}
			}
			
			ImageIO.write(bi, "png", new File("./output_image.png"));
		}catch(IOException e){e.printStackTrace();}
	}*/
	
	private void save()
	{
		try
		{
			GL2 gl = this.getGL().getContext().getGL().getGL2();
			GLReadBufferUtil glread = new GLReadBufferUtil(false, false);
			if(glread.readPixels(gl, false))
			{
				System.out.println("Read pixels");
				glread.write(new File("./output_image.png"));
			}
		}catch(GLException e){e.printStackTrace();}
	}
	
	private void openColor(int x, int y)
	{
		BufferedImage bi = null;
		try
		{
			bi = ImageIO.read(new File("./output_image.png"));
			curColor = new Color(bi.getRGB(x, y));
			this.getGL().glClearColor((float)(curColor.getRed() / 255), (float)(curColor.getRed() / 255), (float)(curColor.getRed() / 255), 1.0f);
			System.out.println("Opened\n" + curColor);
		}catch(IOException e){e.printStackTrace();}
		
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("a");
		this.save();
		this.openColor(e.getX(), e.getY());
	}

	public void mouseEntered(MouseEvent arg0){}public void mouseExited(MouseEvent arg0){}public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}
	
	public void mouseDragged(MouseEvent arg0){}public void mouseMoved(MouseEvent arg0){}
}