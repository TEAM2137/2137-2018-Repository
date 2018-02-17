package org.torc.mainrobot.teleopcontrols;

public class UltraGrabber_Teleop {
	
	private enum GrabberStates { homing, pickup, angling, spitting }
	
	private GrabberStates grabberState = GrabberStates.homing;
	
}
