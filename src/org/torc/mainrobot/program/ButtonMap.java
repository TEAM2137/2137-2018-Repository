package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class ButtonMap {
	
	XboxController xController;
	
	public enum RCButtons { elevatorUp, elevatorDown, positionMode, joggingMode, triTestMode }
	public enum RCAxis { leftX, leftY, rightX, rightY }
	
	public ButtonMap(XboxController controller) {
		xController = controller;
	}
	
	public boolean getButton(RCButtons button, boolean pressed) {
		boolean toReturn = false;
		
		switch (button) {
			case elevatorUp:
				if (pressed) {
					toReturn = xController.getBumperPressed(Hand.kRight);
				}
				else {
					toReturn = xController.getBumper(Hand.kRight);
				}
				break;
			case elevatorDown:
				if (pressed) {
					toReturn = xController.getBumperPressed(Hand.kLeft);
				}
				else {
					toReturn = xController.getBumper(Hand.kLeft);
				}
				break;
			case positionMode:
				if (pressed) {
					toReturn = xController.getAButtonPressed();
				}
				else {
					toReturn = xController.getAButton();
				}
				break;
			case joggingMode:
				if (pressed) {
					toReturn = xController.getBButtonPressed();
				}
				else {
					toReturn = xController.getBButton();
				}
				break;
			case triTestMode:
				if (pressed) {
					toReturn = xController.getXButtonPressed();
				}
				else {
					toReturn = xController.getXButton();
				}
				break;
		}
		
		return toReturn;
	}
	
	public double getAxis(RCAxis axis) {
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
