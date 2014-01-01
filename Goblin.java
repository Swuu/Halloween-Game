/********************************************************
* NAME:		Stephen Wu
* LOGIN:	cs11eeu
* ID:		A11279994
*
* File:				Goblin.java
* Creation Date:	25 October 2013
* Last Modified: 	25 October 2013
* Description:  	Creates a pixel image of a goblin. 
* 					The goblin is launched an bounces from the top of the 
* 					canvas. It calls back to the controller when it is either
* 					vaporized or exits the canvas.
*
*              		This is intended to be called by a CemeteryController.
*
* Build:   			javac -classpath '*':'.'Goblin.java
* Dependencies:  	objectdraw.jar, java.awt.*
* 
* Public Methods Defined:
*           		Goblin(int,Location, double, double, 
*           			Tombstone, CemeteryController, DrawingCanvas)
*           		void vaporize()
* 	    			void setVelocity(double, double))
* 	    			void run() 
* 
* Public Class Variables:
*       			None
*
***********************************************************************/

// Create a Goblin that bounces off the top row of the canvas 
// If the Goblin intersects this tombstone, it calls the enter() method of
// its  Tombstone.   When the goblin exits its run loop, it calls the record()
// method of the CemeteryController to tell it whether or not it has been
// vaporized. 

// Goblins at an initial velocities in the X and Y direction.
// Velocities are approximately pixels per ms


import objectdraw.*;
import java.awt.*;

public class Goblin extends ActiveObject
{
    
	// Variables
	private VisibleImage 		wraith;
	private double 				vx, vy; 	// Current speed of wraith pixels/ms
	private DrawingCanvas 		canvas;
	private boolean 			vaporized = false;
	private Tombstone 			tombstone;
	private CemeteryController 	myController;
	private double 				magnitude;

	private static final double SCALE = 		CemeteryController.SCALE;
	private static final double PROBABILITY = 	CemeteryController.PROBABILITY;
	private static final double MAXV = 			CemeteryController.MAXV;

	/***** Constructor 
	 *         int selection	which image to load
	 *         Location initial 	Where to create the goblin on the canvas
	 *         double velX  	initial velocity x (pixels/ms)	
	 *         double velY  	initial velocity y (pixels/ms)	
	 *         Tombstone aStone     the tombstone 
	 *         CemeteryController control   my controller 
	 *         DrawingCanvas canvas The objectdraw canvas
	 *****/ 
	public Goblin(int selection, Location initial, 
			double velX, double velY, 
			Tombstone aStone, CemeteryController control,
			DrawingCanvas aCanvas) 
	{
		// Assigning images
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		switch(selection)
		{
			case 0: wraith = new VisibleImage (toolkit.getImage("Ghost.jpg"), initial, aCanvas);
					break;
			case 1: wraith = new VisibleImage (toolkit.getImage("skeleton.gif"), initial, aCanvas);
					break;
			case 2: wraith = new VisibleImage (toolkit.getImage("pumpkin.jpg"), initial, aCanvas);
					break;
		}
		
		// Setting instance variables
		tombstone = aStone;
		canvas = aCanvas;
		myController = control;
		scaleAndPlace();
		setVelocity(velX, velY);

		start();
    }
    
	/***** Method: scaleAndPlace()
	* Scale the image so that its width is 1/SCALE of the canvas width
	* Scale the height by the same factor 
	*****/ 
	private void scaleAndPlace()
	{
		double imageWidth, imageHeight, factor;

		imageWidth = canvas.getWidth()/SCALE;
		factor = imageWidth/wraith.getWidth();
		imageHeight = wraith.getHeight()*factor;
		wraith.setSize (imageWidth, imageHeight);
	}

	/***** Method: void moveIt(double)
	*  Move the Goblin, based on elapsed time and speed.
	*  Bounce off the top edge of the canvas
	*	double elapsed -- elapsed time in ms since last update
	*****/ 
	private void moveIt(double elapsed)
	{ 
        wraith.move(elapsed*vx, elapsed*vy);
	}

	/***** Method: void setVelocity(vx,vy)
	*  Setting the velocity, accounting for maximum speed
	*****/ 
	public void setVelocity (double velX, double velY) 
	{
		if (Math.abs(velX) <= MAXV)
    		vx = velX;
    	else
    	{
    		if (velX > MAXV)
    			vx = MAXV;
    		else
    			vx = -MAXV;
    	}


    	if (Math.abs(velY) <= MAXV)
    		vy = velY;
    	else
    	{
    		if (velY > MAXV)
    			vy = MAXV;
    		else
    			vy = -MAXV;
    	}
    	
		// Magnitude deduced from the Pythagorean Theorem
		magnitude = Math.abs(Math.sqrt(Math.pow(vx,2)+Math.pow(vy,2)));
	}

	public void vaporize()
	{
		vaporized = true;
	}	

	/***** Method: public void run()
	* ActiveObject run() method
	*****/ 
	public void run() 
	{
		boolean hasNotOverlapped = true;
		double now, prevClock, elapsed;

		prevClock = System.currentTimeMillis();

		while (true)
		{
			double thresh=1;

			// Calculate and move
			now = System.currentTimeMillis();
			elapsed = now - prevClock;
			prevClock = now;
			moveIt(elapsed);

			// Bounce function
			if (wraith.getY() <= 0 && vy < 0)
			{
			this.setVelocity(vx, -vy);
			}

			// Set minimum speed
			if (magnitude < thresh) 
				magnitude = thresh;

			// Check to see if overlapped and communicate with tombstone
			if (hasNotOverlapped && tombstone.overlaps(wraith))
			{
			tombstone.enter(this);
			hasNotOverlapped = false;
			}

			// Check if wraith is off canvas and return
			if (vaporized || wraith.getX() > canvas.getWidth() || wraith.getX() < -wraith.getWidth() || wraith.getY() > canvas.getHeight())
			{
				myController.record (this, vaporized);
				wraith.removeFromCanvas();
				return;
			}

			// So there is visible movement
			pause(1/magnitude);
		}
	}
}
