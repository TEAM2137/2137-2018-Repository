package org.torc.mainrobot.teleopcontrols;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.GetType;
import org.torc.mainrobot.program.ButtonMap.RCAxis;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.ControlledStateMachine;
import org.torc.mainrobot.robot.commands.UltraGrabber_Pickup;
import org.torc.mainrobot.robot.commands.UltraGrabber_Pickup.PickupStates;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.Elevator.ElevatorPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberSpeeds;

public class Elevator_Teleop extends ControlledStateMachine {
	
	// Roughly 6 inches
	// (and a smidge!!)
	static int jogInterval = (int)((double)Elevator.posPerInch * (11 / 3));
	
	/*
	private enum GrabberStates { operatorControl, pickup }
	
	private GrabberStates grabberState = GrabberStates.operatorControl;
	*/
	
	UltraGrabber_Pickup pickupComm;
	
	public Elevator_Teleop() {
		// Home the systems
		RobotMap.GrabberSubsystem.homeGrabber();
		RobotMap.ElevSubsystem.homeElevator();
	}
	
	@Override
	protected void execute() {
		
		if (RobotMap.operatorControl.getButton(RCButtons.grabberSpitSlow, GetType.normal)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.dropping);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.grabberSpitFast, GetType.normal)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.shooting);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.grabberSpitFast, GetType.released) || 
				RobotMap.operatorControl.getButton(RCButtons.grabberSpitSlow, GetType.released)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.none);
		}
		
		
		// Operator buttons all have override over other functions
		if (RobotMap.operatorControl.getButton(RCButtons.elevLow, GetType.pressed)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.floor);
			//operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevMid, GetType.pressed)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.middle);
			//operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevHigh, GetType.pressed)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.high);
			//operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevatorUp, GetType.pressed)) {
			RobotMap.ElevSubsystem.jogElevatorPos(jogInterval);
			//operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevatorDown, GetType.pressed)) {
			RobotMap.ElevSubsystem.jogElevatorPos(-jogInterval);
			//operatorInterrupt();
		}
		
		
		// Grabber controls
		if (RobotMap.operatorControl.getButton(RCButtons.elevStartPickup, GetType.pressed)) {
			if (pickupComm != null && pickupComm.isRunning()) {
				// Finish command
				pickupComm.state = PickupStates.raiseGrabber;
			}
			else {
				// Start pickup command
				pickupComm = new UltraGrabber_Pickup();
				pickupComm.start();
			}
		}
		
		// Grabber controls
		double jogVal = RobotMap.operatorControl.getAxis(RCAxis.grabberJog);
		if (jogVal > 0.2 || jogVal < -0.2) {
			RobotMap.GrabberSubsystem.jogGrabberPosInc(jogVal * 2);
			//operatorInterrupt();
		}
		
	}
	
	private void operatorInterrupt() {
		if (pickupComm != null) {
			pickupComm.cancel();
			pickupComm.free();
			pickupComm = null;
			// Set intake speed to zero
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.none);
		}
	}
}
