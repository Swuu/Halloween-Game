/********************************************************
* NAME:		Stephen Wu
* LOGIN:	cs11eeu
* ID:		A11279994
*
* File:				CemeteryController.java
* Creation Date: 	25 October 2013
* Last Modified: 	25 October 2013
* Description:  	CemeteryController interface
*
* 					It is an interface 
*
* Public Methods Defined:
*					None
* 
* Public Class Variables:
*       			None
*
***********************************************************************/


public interface CemeteryController
{
    // A callback for goblins when off screen
	public void record(Goblin wraith, boolean vaporized);

	// Defining constants
	public static final double PROBABILITY = 0.5;
	public static final int SCALE = 20;
	public static final int SIZE = 600;
	public static final double MAXV = 0.3;
}