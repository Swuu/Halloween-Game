/********************************************************
* TestCemetery.java 
*
* Author: Philip Papadopoulos
* Creation Date: 21 October 2013
* Last Modified: 24 October 2013
* Description:  
*	This is TestCemetery that implements the CemeteryController interface
*	it has limited functionality -- launches goblins at specific speed
*	depending on X-coordinate of mouse click
*
* Build:   javac -classpath '*':'.' TestCemetery.java
* Dependencies:  objectdraw.jar, java.awt.*, Goblin.class, Tombstone.class
* 
* Public Methods Defined:
*           TestCemetery(int,Location, double, double, 
*           	Tombstone, CemeteryController, DrawingCanvas)
*           static void main(String[]))
* 	    void record(Goblin, boolean))
* 	    void onMouseClick(Location)
* 	    void run() 
* 
* Interfaces Implemented:
* 	CemeteryController
*
* Public Class Variables:
*       None
*
***********************************************************************/
import objectdraw.*;
import java.awt.*;
import java.util.Random;

public class TestCemetery extends WindowController implements CemeteryController 
{
	private Goblin phantom; 
	private int count = 0;
	private FilledOval theExit;
	private FilledRect vControl;
	private Random myRnd;
	private Tombstone tombstone;

	private static final int SIZE = CemeteryController.SIZE;
	private static final int DIM = 20;
	private static final int INSTX = 10;
	private static final double MAXV = CemeteryController.MAXV;
	
	// Begin method called when WindowController is constructed

	public void begin() 
	{       
		// display instructions
		new Text("Click to Create Goblins...", INSTX, INSTX, canvas);
		new Text("Click Square to change last goblin velocity...", 
				INSTX, 2*INSTX, canvas);
		new Text("Click Circle to  exit...", INSTX, 3*INSTX, canvas);
		vControl = new FilledRect(SIZE - DIM, DIM, DIM, DIM, canvas);
		theExit = new FilledOval(SIZE - DIM, 2*DIM, DIM, DIM, canvas);	
		myRnd = new Random();
		tombstone = new Tombstone (new Location (SIZE, SIZE/4), -MAXV, this, canvas);
	}
	 

	public void onMouseClick(Location point) 
	{
		// make a new Goblin.  Vertical speed is not random. 
		// note that the Tombstone is passed  as a null.  the
		// final program needs to create a non-null Tombstone 

		double x = point.getX();
		double curwidth = canvas.getWidth();
		if (theExit.contains(point)) 
			System.exit(0);
		if (vControl.contains(point) && phantom != null)
		{
			double vx,vy;
			vx = (MAXV/2.0) - (myRnd.nextDouble() * MAXV);
			vy = (MAXV/2.0) - (myRnd.nextDouble() * MAXV);
			phantom.setVelocity(vx,vy); 
		}
		else
		{

			double vy = (0.25 + 0.5 * x/curwidth)*MAXV;
	     		phantom = new Goblin(count++ % 3, 
					new Location(x,canvas.getHeight()), 
					0.0, -vy,
					tombstone, this, canvas);
		}
	}

	// Method: record() 
	// Implement for CemeteryController Interface Specification
	public void record(Goblin wraith, boolean vaporized)
	{

		// does nothing in testing
	}

	// Main Method
	 static public void main(String[] args)
	{
		new TestCemetery().startController(SIZE, SIZE);
	}
}
