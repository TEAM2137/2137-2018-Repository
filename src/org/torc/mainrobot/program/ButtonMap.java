package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class ButtonMap {
	
	XboxController xController;
	
	private int POVWatch = -1;
	
	public enum RCButtons { elevatorUp, elevatorDown, elevLow, elevMid, 
							elevHigh, toggleShifters, elevStartPickup,
							grabberSpitSlow, grabberSpitFast, grabberJogUp,
							grabberJogDown
							}
	public enum RCAxis { leftX, leftY, rightX, rightY }
	
	public enum GetType { normal, pressed, released }
	
	public ButtonMap(XboxController controller) {
		xController = controller;
	}
	
	public boolean getButton(RCButtons button, GetType getType) {
		boolean toReturn = false;
		
		switch (button) {
			case elevatorUp:
				if (getType == GetType.pressed) {
					toReturn = xController.getBumperPressed(Hand.kRight);
				}
				else {
					toReturn = xController.getBumper(Hand.kRight);
				}
				break;
			case elevatorDown:
				if (getType == GetType.pressed) {
					toReturn = xController.getBumperPressed(Hand.kLeft);
				}
				else {
					toReturn = xController.getBumper(Hand.kLeft);
				}
				break;
			case elevLow:
				if (getType == GetType.pressed) {
					toReturn = xController.getAButtonPressed();
				}
				else {
					toReturn = xController.getAButton();
				}
				break;
			case elevMid:
				if (getType == GetType.pressed) {
					toReturn = xController.getBButtonPressed();
				}
				else {
					toReturn = xController.getBButton();
				}
				break;
			case elevHigh:
				if (getType == GetType.pressed) {
					toReturn = xController.getYButtonPressed();
				}
				else {
					toReturn = xController.getYButton();
				}
				break;
			case toggleShifters:
				if (getType == GetType.pressed) {
					toReturn = xController.getXButtonPressed();
				}
				else {
					toReturn = xController.getXButton();
				}
				break;
			case elevStartPickup:
				if (getType == GetType.pressed) {
					toReturn = xController.getYButtonPressed();
				}
				else {
					toReturn = xController.getYButton();
				}
				break;
			case grabberSpitSlow: {
				int POVPosition = 180;
				if (getType == GetType.pressed) {
					if (xController.getPOV() == POVPosition && xController.getPOV() != POVWatch) {
						toReturn = true;
					}
				}
				else if (getType == GetType.released) {
					if (POVWatch == POVPosition && xController.getPOV() != POVPosition) {
						toReturn = true;
					}
				}
				else {
					toReturn = (xController.getPOV() == POVPosition);
				}
				updatePOVWatch();
				break;
			}
			case grabberSpitFast: {
				int POVPosition = 0;
				if (getType == GetType.pressed) {
					if (xController.getPOV() == POVPosition && xController.getPOV() != POVWatch) {
						toReturn = true;
					}
				}
				else if (getType == GetType.released) {
					if (POVWatch == POVPosition && xController.getPOV() != POVPosition) {
						toReturn = true;
					}
				}
				else {
					toReturn = (xController.getPOV() == POVPosition);
				}
				updatePOVWatch();
				break;
			}
			case grabberJogUp:
				if (getType == GetType.pressed) {
					toReturn = xController.getBumperPressed(Hand.kRight);
				}
				else {
					toReturn = xController.getBumper(Hand.kRight);
				}
				break;
			case grabberJogDown:
				if (getType == GetType.pressed) {
					toReturn = xController.getBumperPressed(Hand.kLeft);
				}
				else {
					toReturn = xController.getBumper(Hand.kLeft);
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
	
	private void updatePOVWatch() {
		POVWatch = xController.getPOV();
	}
	
	
}
