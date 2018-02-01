package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class ButtonMap {
	
	XboxController xController;
	
	public enum RControllerButtons { elevatorUp, elevatorDown }
	public enum RControllerAxis { leftX, leftY, rightX, rightY }
	
	public ButtonMap(XboxController controller) {
		xController = controller;
	}
	
	public boolean getButton(RControllerButtons button, boolean pressed) {
		boolean toReturn = false;
		
		switch (button) {
			case elevatorUp:
				toReturn = xController.getBumper(Hand.kRight);
				break;
			case elevatorDown:
				toReturn = xController.getBumper(Hand.kLeft);
				break;
		}
		
		return toReturn;
	}
	
	public double getAxis(RControllerAxis axis) {
		double toReturn = 0;
		
		switch (axis) {
			case leftX:
				toReturn = xController.getX(Hand.kLeft);
				break;
			case leftY:
				toReturn = xController.getY(Hand.kLeft);
				break;
			case rightX:
				toReturn = xController.getX(Hand.kRight);
				break;
			case rightY:
				toReturn = xController.getY(Hand.kRight);
				break;
		}
		
		return toReturn;
	}
	
	
	
	
}
