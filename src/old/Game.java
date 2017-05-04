package old;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.*;

@SuppressWarnings("serial")
public class Game extends GLCanvas implements GLEventListener
{
	private GLU glu;
	
	private float anglePyramid = 0;
	private float speedPyramid = 2.0f;
	private float angleCube = 0;
	private float speedCube = -1.5f;
	
	public Game()
	{
		this.addGLEventListener(this);
	}
	
	@Override
	public void display(GLAutoDrawable draw)
	{
		render(draw);
	}

	@Override
	public void dispose(GLAutoDrawable auto)
	{
		
	}

	@Override
	public void init(GLAutoDrawable auto)
	{
		GL2 gl = auto.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);
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
		
		//Pyramid
		gl.glLoadIdentity();
		gl.glTranslatef(-1.6f, 0.0f, -6.0f);
		gl.glRotatef(anglePyramid, -0.2f, 1.0f, 0.0f);
		
		gl.glBegin(GL_TRIANGLES);
		
		//Front
		gl.glColor3f(1.0f, 0.0f, 0.0f);	//Red
		gl.glVertex3f(-1.0f, 0.0f, 1.0f);
		gl.glColor3f(0.0f, 1.0f, 0.0f);	//Green
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f);	//Blue
		gl.glVertex3f(1.0f, 0.0f, 1.0f);
		
		//Left
		gl.glColor3f(1.0f, 0.0f, 0.0f);	//Red
		gl.glVertex3f(-1.0f, 0.0f, 1.0f);
		gl.glColor3f(0.0f, 1.0f, 0.0f);	//Green
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f);	//Blue
		gl.glVertex3f(0.0f, 0.0f, -1.0f);
		
		//Right
		gl.glColor3f(1.0f, 0.0f, 0.0f);	//Red
		gl.glVertex3f(0.0f, 0.0f, -1.0f);
		gl.glColor3f(0.0f, 1.0f, 0.0f);	//Green
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f);	//Blue
		gl.glVertex3f(1.0f, 0.0f, 1.0f);
		
		//Bottom
		gl.glColor3f(1.0f, 0.0f, 0.0f);	//Red
		gl.glVertex3f(-1.0f, 0.0f, 1.0f);
		gl.glColor3f(0.0f, 1.0f, 0.0f);	//Green
		gl.glVertex3f(0.0f, 0.0f, -1.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f);	//Blue
		gl.glVertex3f(1.0f, 0.0f, 1.0f);
		
		gl.glEnd();
		
		//Cube
		gl.glLoadIdentity();
		gl.glTranslatef(1.6f, 0.0f, -7.0f);
		gl.glRotatef(angleCube, 1.0f, 1.0f, 1.0f);
		
		gl.glBegin(GL_QUADS);
		//Front
		gl.glColor3f(1.0f, 0.0f, 0.0f);	//Red
		gl.glVertex3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(1.0f, 0.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(0.0f, 1.0f, 1.0f);
		
		//Back
		gl.glColor3f(0.0f, 1.0f, 0.0f);	//Green
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		
		//Top
		gl.glColor3f(0.0f, 0.0f, 1.0f);	//Blue
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.0f, 1.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, 0.0f);
		
		//Bottom
		gl.glColor3f(1.0f, 1.0f, 0.0f);	//Yellow
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(1.0f, 0.0f, 1.0f);
		gl.glVertex3f(1.0f, 0.0f, 0.0f);
		
		//Left
		gl.glColor3f(1.0f, 0.0f, 1.0f);	//Purple
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(0.0f, 1.0f, 1.0f);
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		
		//Right
		gl.glColor3f(1.0f, 0.0f, 1.0f);	//Cyan
		gl.glVertex3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 0.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(1.0f, 1.0f, 0.0f);
		
		gl.glEnd();
	   
		angleCube += speedCube;
		anglePyramid += speedPyramid;
	}
}
