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
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_PAGE_UP;
import static java.awt.event.KeyEvent.VK_PAGE_DOWN;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

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
public class Stars extends GLCanvas implements GLEventListener, KeyListener
{
	private GLU glu;
	
	private static float speedX = 0.01f;
	private static float zp = -15.0f;
	private static float xp = 0.0f;
	private static float yp = 0.0f;
	private static float zIncrement = 0.02f;
	private static float xIncrement = 0.02f;
	private static float yIncrement = 0.02f;
	private static float tileIncrement = 1.0f;
	private static float tilt = 90.0f;
	private static float starSpinAngle = 0.0f;
	private static boolean blendingEnabled;
	
	private static boolean twinkleOn = true;
	public static final int numStars = 50;
	private Star[] stars = new Star[numStars];
	
	private Texture[] textures = new Texture[3];
	private static int currTextureFilter = 0;
	private String textureFileName = "images/star.bmp";
	private float textureTop, textureBottom, textureLeft, textureRight;
	private static boolean isLightOn;
	
	public Stars()
	{
		this.addGLEventListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
	}
	
	@Override
	public void display(GLAutoDrawable draw)
	{
		render(draw);
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
		
		textures[0].enable(gl);
		textures[0].bind(gl);
		
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		gl.glEnable(GL_BLEND);
		gl.glDisable(GL_DEPTH_TEST);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		blendingEnabled = true;
		for(int i = 0; i < stars.length; i++)
		{
			stars[i] = new Star();
			stars[i].distance = ((float)i / numStars) * 5.0f;
		}
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
		
		for (int i = 0; i < stars.length; i++)
		{
			gl.glLoadIdentity();
			gl.glTranslatef(xp, yp, zp);
			gl.glRotatef(tilt, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(stars[i].angle, 0.0f, 1.0f, 0.0f);
			gl.glTranslatef(stars[i].distance, 0.0f, 0.0f);
			gl.glRotatef(-stars[i].angle, 0.0f, 1.0f, 0.0f);
			gl.glRotatef(-tilt, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(starSpinAngle, 0.0f, 0.0f, 1.0f);
			gl.glColor4ub(stars[i].r, stars[i].g, stars[i].b, (byte)255);
			gl.glBegin(GL_QUADS);
			gl.glTexCoord2f(textureLeft, textureBottom);
			gl.glVertex3f(-1.0f, -1.0f, 0.0f);
			gl.glTexCoord2f(textureRight, textureBottom);
			gl.glVertex3f(1.0f, -1.0f, 0.0f);
			gl.glTexCoord2f(textureRight, textureTop);
			gl.glVertex3f(1.0f, 1.0f, 0.0f);
			gl.glTexCoord2f(textureLeft, textureTop);
			gl.glVertex3f(-1.0f, 1.0f, 0.0f);
			gl.glEnd();
			
			if(twinkleOn)
			{
				gl.glColor4ub(stars[(numStars - i) - 1].r, stars[(numStars - i) - 1].g, stars[(numStars - i) - 1].b, (byte)255);
				gl.glBegin(GL_QUADS);
				gl.glTexCoord2f(textureLeft, textureBottom);
				gl.glVertex3f(-1.0f, -1.0f, 0.0f);
				gl.glTexCoord2f(textureRight, textureBottom);
				gl.glVertex3f(1.0f, -1.0f, 0.0f);
				gl.glTexCoord2f(textureRight, textureTop);
				gl.glVertex3f(1.0f, 1.0f, 0.0f);
				gl.glTexCoord2f(textureLeft, textureTop);
				gl.glVertex3f(-1.0f, 1.0f, 0.0f);
				gl.glEnd();
			}
			
			starSpinAngle += 0.01f;
			stars[i].angle += (float)i / numStars;
			stars[i].distance -= 0.01f;
			if(stars[i].distance < 0.0f)
			{
				stars[i].distance += 5.0f;
				stars[i].setRandomRGB();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		switch(keyCode)
		{
			case VK_W:
				tilt -= tileIncrement;
				break;
			case VK_S:
				tilt += tileIncrement;
				break;
			case VK_A:
				speedX -= 0.01;
				break;
			case VK_D:
				speedX += 0.01;
				break;
			case VK_PAGE_UP:
				zp -= zIncrement;
				break;
			case VK_PAGE_DOWN:
				zp += zIncrement;
				break;
			case VK_UP:
				yp += yIncrement;
				break;
			case VK_DOWN:
				yp -= yIncrement;
				break;
			case VK_LEFT:
				xp -= xIncrement;
				break;
			case VK_RIGHT:
				xp += xIncrement;
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
				twinkleOn = true;
				currTextureFilter = 0;
				xp = 0.0f;
				yp = 0.0f;
				zp = -5.0f;
				break;
			case VK_T:
				twinkleOn = !twinkleOn;
				break;
			
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}

class Star
{
    // public access for simplicity
    public byte r, g, b;   // RGB values for the star
    public float distance; // distance from the center
    public float angle;    // current angle about the center

    private Random rand = new Random();

    // Constructor
    public Star()
    {
       angle = 0.0f;
       r = (byte)rand.nextInt(256);
       g = (byte)rand.nextInt(256);
       b = (byte)rand.nextInt(256);
    }

    // Set the RGB color of this star to some random values
    public void setRandomRGB()
    {
       r = (byte)rand.nextInt(256);
       g = (byte)rand.nextInt(256);
       b = (byte)rand.nextInt(256);
    }
}

