package org.torc.mainrobot.program;

import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class ButtonMap {
	
	GenericHID mController;
	
	public enum RCButtons { elevatorUp, elevatorDown, elevLow, elevMid, 
							elevClimb, elevHigh, toggleShifters, elevStartPickup,
							grabberSpitSlow, grabberSpitFast, autonSelectRight, 
							autonSelectLeft, autonPriorityUp, autonPriorityDown,
							autonZigBaseToggle, hookRelease, rampRelease, 
							homeGrabber}
	
	public enum RCAxis { leftX, leftY, rightX, rightY,
						grabberJog }
	
	public enum GetType { normal, pressed, released }
	
	public ButtonMap(GenericHID controller) {
		mController = controller;
	}
	
	public boolean getButton(RCButtons button, GetType getType) {
		boolean toReturn = false;
		
		switch (button) {
			case elevatorUp:
				toReturn = getEasyButtonInternal(6, getType);
				break;
			case elevatorDown:
				toReturn = getEasyButtonInternal(5, getType);
				break;
			case elevLow:
				toReturn = getEasyButtonInternal(1, getType);
				break;
			case elevMid:
				toReturn = getEasyButtonInternal(2, getType);
				break;
			case elevHigh:
				toReturn = getEasyButtonInternal(4, getType);
				break;
			case elevClimb:
				toReturn = getEasyButtonInternal(4, getType);
				break;
			case toggleShifters:
				toReturn = getEasyButtonInternal(5, getType);
				break;
			case elevStartPickup:
				toReturn = getEasyButtonInternal(3, getType);
				break;
			case grabberSpitSlow:
				toReturn = getPOVInternal(225, getType) || getPOVInternal(180, getType) || getPOVInternal(135, getType);
				break;
			case grabberSpitFast:
				toReturn = getPOVInternal(315, getType) || getPOVInternal(0, getType) || getPOVInternal(45, getType);
				break;
			case autonSelectRight:
				toReturn = getPOVInternal(90, getType);
				break;
			case autonSelectLeft:
				toReturn = getPOVInternal(270, getType);
				break;
			case autonPriorityUp:
				toReturn = getPOVInternal(0, getType);
				break;
			case autonPriorityDown:
				toReturn = getPOVInternal(180, getType);
				break;
			case autonZigBaseToggle:
				toReturn = getEasyButtonInternal(1, getType);
				break;
			case hookRelease:
				toReturn = getEasyButtonInternal(6, getType);
				break;
			case rampRelease:
				toReturn = (mController.getRawAxis(3) >= 0.8);
				break;
			case homeGrabber:
				toReturn = getEasyButtonInternal(8, getType);
				break;
		}
		
		return toReturn;
	}
	
	public double getAxis(RCAxis axis) {
		double toReturn = 0;
		
		switch (axis) {
			case leftX:
				toReturn = mController.getX(Hand.kLeft);
				break;
			case leftY:
				toReturn = mController.getY(Hand.kLeft);
				break;
			case rightX:
				toReturn = mController.getX(Hand.kRight);
				break;
			case rightY:
				toReturn = mController.getY(Hand.kRight);
				break;
			case grabberJog:
				toReturn = -mController.getRawAxis(3) + mController.getRawAxis(2);
				toReturn = MathExtra.clamp(toReturn, -1, 1);
				break;
		}
		
		return toReturn;
	}
	
	private int POVWatch = -1;
	
	private boolean getPOVInternal(int position, GetType type) {
		boolean toReturn = false;
		
		int pov = mController.getPOV();
		
		if (type == GetType.pressed) {
			if (pov == position && pov != POVWatch) {
				toReturn = true;
				POVWatch = pov;
			}
		}
		else if (type == GetType.released) {
			if (POVWatch == position && pov != POVWatch) {
				toReturn = true;
				POVWatch = pov;
			}
		}
		else {
			if (pov == position) {
				toReturn = true;
				POVWatch = pov;
			}
		}
		
		return toReturn;
	}
	
	private boolean getEasyButtonInternal(int buttonId, GetType type) {
		boolean toReturn = false;
		
		if (type == GetType.pressed) {
			toReturn = mController.getRawButtonPressed(buttonId);
		}
		else if (type == GetType.released) {
			toReturn = mController.getRawButtonReleased(buttonId);
		}
		else {
			toReturn = mController.getRawButton(buttonId);
		}
		
		return toReturn;
	}
	
	public void setDualRumbleTime(double value, double time) {
		ControllerRumble cRumb = new ControllerRumble(this, value, time);
		cRumb.start();
	}
	
	public void setDualRumble(double value) {
		mController.setRumble(RumbleType.kLeftRumble, value);
		mController.setRumble(RumbleType.kRightRumble, value);
	}
}

class ControllerRumble extends Command {

	private ButtonMap controller;
	
	private double rumbleTime;
	private double rumbleVal;
	
	private int timeCount = 0;
	
	private boolean isFinished = false;
	
	/**
	 * @param cont
	 * @param time
	 * Amount of time (in seconds) for the controller to rumble.
	 */
	public ControllerRumble(ButtonMap cont, double value, double time) {
		controller = cont;
		rumbleTime = time;
		rumbleVal = value;
	}
	
	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		controller.setDualRumble(rumbleVal);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if ( (timeCount / 50) >= rumbleTime ) {
			controller.setDualRumble(0);
			isFinished = true;
		}
		timeCount++;
	}

	@Override
	protected boolean isFinished() {
		return isFinished;
	}
}
