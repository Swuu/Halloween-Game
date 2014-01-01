/********************************************************
* NAME:		Stephen Wu
* LOGIN:	cs11eeu
* ID:		A11279994
*
* File:				Tombstone.java
* Creation Date: 	25 October 2013
* Last Modified: 	25 October 2013
* Description:  	Creates a pixel image of a tombstone.
* 					the tombstone moves across the cemetery at
* 					a fixed Y coordinate.
*
* 					It is an ActiveObject 
*
* Build:   			javac -classpath '*':'.' Tombstone.java
* Dependencies:  	objectdraw.jar, java.awt.*
* 
* Public Methods Defined:
*           		Tombstone(Location, double, double, 
*           			Drawable2DInterface, SpaceController, DrawingCanvas)
*           		void setVelocity(double velX)
*           		void enter(Goblin wraith)
*           		void run()
*          			boolean overlaps(Drawable2DInterface region)
* 
* Public Class Variables:
*       			None
*
***********************************************************************/


import objectdraw.*;
import java.awt.*;

import java.util.Random;
public class Tombstone extends ActiveObject
{
	// Creating class variables
	private VisibleImage 		stone;
	private Image 				stoneGraphic;
	private double 				vx; 	// Current speed of stone in pixels/ms
	private DrawingCanvas 		canvas;
	private CemeteryController 	myController;
	private Random 				myRnd;
	private static final String GRAPHIC = "tombstone.jpg";
	
	// Referring to CemeteryController
	private static final double SCALE = 		CemeteryController.SCALE;
	private static final double SIZE = 			CemeteryController.SIZE;
	private static final double PROBABILITY = 	CemeteryController.PROBABILITY;
	private static final double MAXV = 			CemeteryController.MAXV;

	
	/***** Constructor 
	 *         Location initial 	Where to create the stone on the canvas
	 *         double velX  	initial velocity x (pixels/ms)	
	 *         CemeteryController control   my controller 
	 *         DrawingCanvas canvas The objectdraw canvas
	 *****/ 
	public Tombstone(Location initial, double velX, 
			 CemeteryController control, DrawingCanvas aCanvas) 
	{
		//Load the image
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		stone = new VisibleImage (toolkit.getImage("tombstone.jpg"), initial, aCanvas); 

		canvas = aCanvas;
		scaleAndPlace();
		setVelocity(velX);
		
		myController  = control;
		myRnd = new Random();
		start();

		// For ActiveObjects, this will eventually call run()
    }
   
	/***** Method: scaleAndPlace()
	* Scale the image so that its width is 1/SCALE of the canvas width
	* Scale the height by the same factor 
	*****/ 
	private void scaleAndPlace()
	{
		// Calculate scale factor and scale stone
		double imageWidth, imageHeight;
		double factor;

		imageWidth = canvas.getWidth()/SCALE;
		factor = imageWidth/stone.getWidth();
		imageHeight = stone.getHeight();
		imageHeight *= factor;
		stone.setWidth(imageWidth);
		stone.setHeight(imageHeight);
	}	


	/***** Method: void moveIt(double)
	*  Move the tombstone, based on elapsed time and speed.
	*  roll around edges of canvas
	*	double elapsed -- elapsed time in ms since last update
	*****/ 
	private void moveIt(double elapsed)
	{ 
		// Defining local variables
		double imageWidth;
		double x,y, newX, dx;

		// Setting the functionality of moveIt
		imageWidth = stone.getWidth();
		x = stone.getLocation().getX();
		y = stone.getLocation().getY();
		
 		stone.move(elapsed*vx, 0);

		if (stone.getX() < -imageWidth)
		{
			stone.moveTo(canvas.getWidth(), SIZE/4);
		}
		
		if (stone.getX() > canvas.getWidth())
		{
			stone.moveTo(-imageWidth, SIZE/4);
		}
	}

	/***** Method: void setVelocity(vx)
	* 	Set the velocity	
	*****/ 
	public void setVelocity (double velX) 
	{
            vx = velX;
	}

	/***** Method: public void enter(Goblin wraith))
	*  
	*****/ 
	// Decide what to do when a goblin intersects us 
	public void enter(Goblin wraith)
	{	
		Random rn = new Random();
		if (rn.nextDouble() <= PROBABILITY)
			wraith.vaporize();
	}	

	/***** Method: public void run()
	* ActiveObject run() method
	*****/ 
	public void run() 
	{
        double prevClock, now, elapsed;
		double tmpvx; 
		double delay; 
		double thresh=1e-2;

		// calculate the pause delay time based upon speed
		// want to pause no more than ~10 milliseconds, update more 
		// quickly if velocity is faster
		
		tmpvx = Math.abs(vx);
		if (tmpvx < thresh) 
			tmpvx = thresh;
		delay = 1/tmpvx;
        prevClock = System.currentTimeMillis();	
        	
		// Note: infinite loop. Will exit when main program exits
		while (true)
		{
			now = System.currentTimeMillis();
			elapsed = now - prevClock;
			prevClock = now;
			moveIt(elapsed);
			pause(delay);
			
		}

	}

	// Method: determines if a Drawable2Dinterface intersects the stone
	public boolean overlaps(Drawable2DInterface region)
	{
		return stone.overlaps(region); 
	}
}
