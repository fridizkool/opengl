package old;

import java.util.Random;

public class Star
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
