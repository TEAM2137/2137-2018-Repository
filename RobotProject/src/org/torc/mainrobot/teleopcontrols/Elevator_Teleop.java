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
import org.torc.mainrobot.tools.MathExtra;

public class Elevator_Teleop extends ControlledStateMachine {
	
	// Roughly 6 inches
	// (and a smidge!!)
	static int jogInterval = (int)((double)Elevator.posPerInch * (11 / 3));
	
	private int elevHome_Time = 0;
	
	//private final int ElevHome_HomeWait = 500 / 20;
	private final int ElevHome_JogModeWait = 3000 / 20;
	
	private boolean elevPercJogMode = false;
	
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
		
		// Manual elevator homing
		/*
		if (RobotMap.operatorControl.getButton(RCButtons.homeELevator, GetType.pressed)) {
			RobotMap.ElevSubsystem.homeElevator();
		}
		*/
		
		if (RobotMap.operatorControl.getButton(RCButtons.homeELevator, GetType.normal)) {
			elevHome_Time += (elevHome_Time != -1)?1:0;
			
			if (elevHome_Time >= ElevHome_JogModeWait) {
				elevHome_Time = -1;
				RobotMap.ElevSubsystem.deHome();
				elevPercJogMode = true;
				RobotMap.operatorControl.setDualRumbleTime(1, 0.5);
			}
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.homeELevator, GetType.released)) {
			if (elevHome_Time != -1) {
				RobotMap.ElevSubsystem.homeElevator();
				elevPercJogMode = false;
			}
			elevHome_Time = 0;
		}
		
		if (RobotMap.operatorControl.getButton(RCButtons.grabberSpitSlow, GetType.normal)) {
			RobotMap.GrabberSubsystem.setCubeGrip(false);
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.dropping);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.grabberSpitFast, GetType.normal)) {
			RobotMap.GrabberSubsystem.setCubeGrip(false);
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.shooting);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.grabberSpitDrop, GetType.pressed)) {
			RobotMap.GrabberSubsystem.setCubeGrip(false);
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.deadDrop);
			operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.grabberSpitFast, GetType.released) || 
				RobotMap.operatorControl.getButton(RCButtons.grabberSpitSlow, GetType.released) ||
				RobotMap.operatorControl.getButton(RCButtons.grabberSpitDrop, GetType.released)) {
			RobotMap.GrabberSubsystem.setCubeGrip(false);
			//RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.none);
		}
				
		// Operator buttons all have override over other functions
		if (RobotMap.operatorControl.getButton(RCButtons.elevLow, GetType.pressed)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.floor);
			//operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevMid, GetType.pressed)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.middle);
			//RobotMap.GrabberSubsystem.toggleSolenoid();
			//operatorInterrupt();
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevHigh, GetType.pressed)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.high);
			//operatorInterrupt();
		}
		
		// Elevator jogging
		if (!elevPercJogMode) {
			if (RobotMap.operatorControl.getButton(RCButtons.elevatorUp, GetType.pressed)) {
				RobotMap.ElevSubsystem.jogElevatorPos(jogInterval);
			}
			else if (RobotMap.operatorControl.getButton(RCButtons.elevatorDown, GetType.pressed)) {
				RobotMap.ElevSubsystem.jogElevatorPos(-jogInterval);
			}
		}
		else {
			double jogAmt = 0;
			jogAmt = -RobotMap.operatorControl.getAxis(RCAxis.elevatorManuJog);
			
			if (MathExtra.applyDeadband(jogAmt, 0.2) == 0) {
				// Set jogAmount to keep the jogger "still"
				jogAmt = 0.2;
			}
			
			RobotMap.ElevSubsystem.jogElevatorPerc(jogAmt);
		}
		
		
		// Grabber controls
		if (RobotMap.operatorControl.getButton(RCButtons.elevStartPickup, GetType.pressed)) {
			if (pickupComm != null && pickupComm.isRunning()) {
				// Finish command
				pickupComm.completePickup();
			}
			else {
				// Start pickup command
				pickupComm = new UltraGrabber_Pickup(RobotMap.GrabberSubsystem, RobotMap.ElevSubsystem);
				pickupComm.start();
			}
		}
		
		// Grabber Re-Home
		if (RobotMap.operatorControl.getButton(RCButtons.homeGrabber, GetType.pressed)) {
			RobotMap.GrabberSubsystem.homeGrabber();
			operatorInterrupt();
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
