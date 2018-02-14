package org.torc.mainrobot.teleopcontrols;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.ControlledStateMachine;
import org.torc.mainrobot.robot.commands.elevator.Elevator_TriScale;

public class Elevator_Teleop extends ControlledStateMachine {
	
	public enum ElevatorStates { jogging, position, triScale }
	public ElevatorStates elevState = ElevatorStates.jogging;
	
	static int jogInterval = 200;
	
	Elevator_TriScale triScale;
	
	@Override
	protected void execute() {
		if (RobotMap.operatorControl.getButton(RCButtons.positionMode, true)) {
			elevState = ElevatorStates.position;
			System.out.println("Elevator: Position mode!");
		}
		// To Jogging
		else if (RobotMap.operatorControl.getButton(RCButtons.joggingMode, true)) {
			elevState = ElevatorStates.jogging;
			System.out.println("Elevator: Jogging mode!");
		}
		
		if (RobotMap.operatorControl.getButton(RCButtons.triTestMode, true)) {
			elevState = ElevatorStates.triScale;
			triScale = new Elevator_TriScale(RobotMap.ElevSubsystem);
			triScale.start();
		}
		
		switch(elevState) {
			case jogging:
				if(RobotMap.operatorControl.getButton(RCButtons.elevatorUp, true)) {
					RobotMap.ElevSubsystem.jogElevatorPos(jogInterval);
				}
				else if (RobotMap.operatorControl.getButton(RCButtons.elevatorDown, true)) {
					RobotMap.ElevSubsystem.jogElevatorPos(-jogInterval);
				}
				break;
			case position:
				if(RobotMap.operatorControl.getButton(RCButtons.elevatorUp, true)) {
					RobotMap.ElevSubsystem.jogElevatorPosInc(1);
				}
				else if (RobotMap.operatorControl.getButton(RCButtons.elevatorDown, true)) {
					RobotMap.ElevSubsystem.jogElevatorPosInc(-1);
				}
				break;
			case triScale:
				if (triScale != null && triScale.isFinished()) {
					elevState = ElevatorStates.position;
					triScale.free();
					triScale = null;
				}
				break;
		}
	}
}
