package org.torc.mainrobot.teleopcontrols;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.ControlledStateMachine;
import org.torc.mainrobot.robot.commands.UltraGrabber_Pickup;
import org.torc.mainrobot.robot.commands.UltraGrabber_Pickup.PickupStates;
import org.torc.mainrobot.robot.subsystems.Elevator.ElevatorPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberSpeeds;

public class Elevator_Teleop extends ControlledStateMachine {
	
	// Roughly 6 inches
	// (and a smidge!!)
	static int jogInterval = 3137;
	
	private final int angleInc = 10;
	
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
		
		if (RobotMap.driverControl.getButton(RCButtons.grabberSpitSlow, false)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.dropping);
			operatorInterrupt();
		}
		else if (RobotMap.driverControl.getButton(RCButtons.grabberSpitFast, false)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.shooting);
			operatorInterrupt();
		}
		else if (RobotMap.driverControl.getButton(RCButtons.tempStopIntake, false)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.none);
			operatorInterrupt();
		}
		
		// Operator buttons all have override over other functions
		if (RobotMap.operatorControl.getButton(RCButtons.elevLow, true)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.floor);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevMid, true)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.middle);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevHigh, true)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.high);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevatorUp, true)) {
			RobotMap.ElevSubsystem.jogElevatorPos(jogInterval);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevatorDown, true)) {
			RobotMap.ElevSubsystem.jogElevatorPos(-jogInterval);
			operatorInterrupt();
		}
		
		// Grabber controls
		if (RobotMap.driverControl.getButton(RCButtons.elevStartPickup, true)) {
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
		else if (RobotMap.driverControl.getButton(RCButtons.grabberJogUp, true)) {
			RobotMap.GrabberSubsystem.jogGrabberPosInc(-angleInc);
			operatorInterrupt();
		}
		else if (RobotMap.driverControl.getButton(RCButtons.grabberJogDown, true)) {
			RobotMap.GrabberSubsystem.jogGrabberPosInc(angleInc);
			operatorInterrupt();
		}
		
		/*
		if (RobotMap.driverControl.getButton(RCButtons.grabberSpitSlow, true)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.dropping);
			operatorInterrupt();
		}
		else if (RobotMap.driverControl.getButton(RCButtons.grabberSpitFast, true)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.shooting);
			operatorInterrupt();
		}
		*/
		/*
		else {
			spitSpeed = 0;
		}
		
		if (pickupComm == null || !pickupComm.isRunning()) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.none);
		}
		*/
		
	}
	
	private void operatorInterrupt() {
		if (pickupComm != null) {
			pickupComm.cancel();
			pickupComm.free();
			pickupComm = null;
		}
	}
}
