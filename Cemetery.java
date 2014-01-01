/********************************************************
* NAME:		Stephen Wu
* LOGIN:	cs11eeu
* ID:		A11279994
*
* File:				Cemetery.java
* Creation Date: 	25 October 2013
* Last Modified: 	25 October 2013
* Description:  
*					This is TestCemetery that implements the CemeteryController interface
*					it has limited functionality -- launches goblins at specific speed
*					depending on X-coordinate of mouse click
*
* Build:   			javac -classpath '*':'.' TestCemetery.java
* Dependencies:  	objectdraw.jar, java.awt.*, Goblin.class, Tombstone.class
* 
* Public Methods Defined:
*           		TestCemetery(int,Location, double, double, 
*           			Tombstone, CemeteryController, DrawingCanvas)
*           		static void main(String[]))
* 	    			void record(Goblin, boolean))
* 	    			void onMouseClick(Location)
* 	    			void run() 
* 
* Interfaces Implemented:
* 					CemeteryController
*
* Public Class Variables:
*   				None
*
***********************************************************************/


import objectdraw.*;
import java.awt.*;
import java.util.Random;

public class Cemetery extends WindowController implements CemeteryController 
{
	// Core variables
	private static final int SIZE = 	CemeteryController.SIZE;
	private static final double MAXV = 	CemeteryController.MAXV;
	private static final int DIM = 		20;
	private static final int INSTX = 	10;

	
	// Shooter variables
	private Line 			shooter;
	private Location 		shooterStart,
							shooterEnd;

	// Display variables
    private Text 			display;
    private int     		phantomCount = 0,
                        	vaporizedCount = 0,
                        	passedCount = 0;
	
	// Misc variables
	private Goblin 			phantom; 
	private int 			count = 0;
	private FilledOval 		theExit;
	private FilledRect 		vControl;
	private Random 			myRnd;
	private Tombstone 		tombstone;


	// Begin method called when WindowController is constructed
	public void begin() 
	{       
		// Display instructions
		new Text("Click to Create Goblins...", INSTX, INSTX, canvas);
		new Text("Click Square to change last goblin velocity...", INSTX, 2*INSTX, canvas);
		new Text("Click Circle to  exit...", INSTX, 3*INSTX, canvas);
		display = new Text ("nothing", INSTX, 4*INSTX, canvas);

		// Creating objects
		vControl = new FilledRect(SIZE - DIM, DIM, DIM, DIM, canvas);
		theExit = new FilledOval(SIZE - DIM, 2*DIM, DIM, DIM, canvas);	
		myRnd = new Random();
		tombstone = new Tombstone (new Location (SIZE, SIZE/4), -MAXV, this, canvas);


		updateDisplay();
	}

	// Creating vector and line when mouse is pressed
	public void onMousePress(Location point) 
	{
		shooterStart = new Location(point.getX(),canvas.getHeight());
		shooterEnd = new Location(point.getX(),point.getY());
      	shooter = new Line(shooterStart, shooterEnd, canvas);
      	shooter.setColor(Color.GREEN);
	}

	// Changing end point of vector and line
	public void onMouseDrag (Location point)
	{
    	shooterEnd = point;
        shooter.setEnd(shooterEnd);
	}

	// Shooting the goblin!
    public void onMouseRelease(Location point)
    {
    	//Creating the shooter more than once
    	if (shooter != null)
    	{

        // Removing shooter
        shooter.removeFromCanvas();

        //Allowing the construction of another shooter afterwards
        shooter = null;
        }

        // Shooting!
        double dx = shooterEnd.getX() - shooterStart.getX();
        double dy = shooterEnd.getY() - shooterStart.getY();
		double vx = dx/canvas.getWidth();
		double vy = dy/canvas.getWidth();

	    phantom = new Goblin(myRnd.nextInt(3), shooterStart, vx, vy, tombstone, this, canvas);
	    phantomCount++;
	    updateDisplay();
    }

    // Functionality of the filled square and filled oval
    public void onMouseClick (Location point)
    {
    	if (theExit.contains(point)) 
			System.exit(0);

		if (vControl.contains(point) && phantom != null)
		{
			double vx,vy;
			vx = (MAXV/2.0) - (myRnd.nextDouble() * MAXV);
			vy = (MAXV/2.0) - (myRnd.nextDouble() * MAXV);
			phantom.setVelocity(vx,vy); 
		}
    }

	// Records information and updates display
	public void record(Goblin wraith, boolean vaporized)
	{
		if (vaporized)
			vaporizedCount++;

		else
			passedCount++;

		updateDisplay();
		
	}

	// Updates display text
    private void updateDisplay()
    {
        display.setText("#phantoms: " + phantomCount + " #vaporized: " + vaporizedCount + " #passed: " + passedCount);
    }
	
	// Main Method
	 static public void main(String[] args)
	{
		new Cemetery().startController(SIZE, SIZE);
	}
}
