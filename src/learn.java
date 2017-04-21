import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class learn
{

	final private static int CANVAS_WIDTH = 800;
	final private static int CANVAS_HEIGHT = 600;
	private static final int FPS = 60;
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
	         public void run()
	         {
	        	GLCanvas canvas = new TriPoly();
	        	canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
				final JFrame frame = new JFrame("Da game");
				frame.getContentPane().add(canvas);
				
				frame.addWindowListener(new WindowAdapter()
				{
		            public void windowClosing(WindowEvent e)
		            {
		               // Use a dedicate thread to run the stop() to ensure that the
		               // animator stops before program exits.
		               new Thread()
		               {
		                  public void run()
		                  {
		                     if (animator.isStarted()) animator.stop();
		                     System.exit(0);
		                  }
		               }.start();
		            }
		         });
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
				animator.start();
	         }
		});
	}
	
}
