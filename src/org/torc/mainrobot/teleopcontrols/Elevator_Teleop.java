package org.torc.mainrobot.teleopcontrols;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.ControlledStateMachine;
import org.torc.mainrobot.robot.subsystems.Elevator.ElevatorPositions;

public class Elevator_Teleop extends ControlledStateMachine {
	
	// Roughly 6 inches
	// (and a smidge!!)
	static int jogInterval = 3137;
	
	@Override
	protected void execute() {
		
		if (RobotMap.operatorControl.getButton(RCButtons.elevLow, true)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.floor);
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevMid, true)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.middle);
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevHigh, true)) {
			RobotMap.ElevSubsystem.positionFind(ElevatorPositions.high);
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevatorUp, true)) {
			RobotMap.ElevSubsystem.jogElevatorPos(jogInterval);
		}
		else if (RobotMap.operatorControl.getButton(RCButtons.elevatorDown, true)) {
			RobotMap.ElevSubsystem.jogElevatorPos(-jogInterval);
		}
		
	}
}
