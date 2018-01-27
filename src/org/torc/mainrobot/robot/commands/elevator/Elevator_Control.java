package org.torc.mainrobot.robot.commands.elevator;

import org.torc.mainrobot.program.ButtonMap;
import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Control extends Command {
	
	public enum elevatorControlMode { jogging, position, none }
	
	elevatorControlMode controlMode = elevatorControlMode.jogging;
	
	boolean doneRunning = false;
	
	public Elevator_Control() {
		// Use requires() here to declare subsystem dependencies
		requires(RobotMap.ElevSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	boolean tempBool = false;
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		// Check if mode change
		
		// To Position
		if (ButtonMap.xController0.getAButton()) {
			controlMode = elevatorControlMode.position;
			//RobotMap.elevSubsystem.positionElevator(10000);
		}
		// To Jogging
		else if (ButtonMap.xController0.getBButton()) {
			controlMode = elevatorControlMode.jogging;
		}
		
		switch(controlMode) {
			case jogging:
				int jogInterval = 7000; 
				
				if(ButtonMap.xController0.getBumper(GenericHID.Hand.kRight)) {
					if (!tempBool) {
						tempBool = true;
						System.out.println("Jogging Right Bumper");
						// TODO: In the midst of fixing this 
						//targetPosition += jogInterval;
					}
				}
				else if (ButtonMap.xController0.getBumper(GenericHID.Hand.kLeft)) {
					if (!tempBool) {
						tempBool = true;
						System.out.println("Jogging Left Bumper");
						//targetPosition -= jogInterval;
					}
				}
				else {
					tempBool = false;
				}
				//RobotMap.ElevSubsystem.positionElevator(targetPosition);
				break;
			case position:
				if(ButtonMap.xController0.getBumper(GenericHID.Hand.kRight)) {
					if (!tempBool) {
						tempBool = true;
						RobotMap.ElevSubsystem.elevatorPosition = Elevator.ElevatorPositions.values()[(int) MathExtra.clamp(RobotMap.ElevSubsystem.elevatorPosition.ordinal() + 1, 0, Elevator.ElevatorPositions.values().length-1)];
					}
				}
				else if (ButtonMap.xController0.getBumper(GenericHID.Hand.kLeft)) {
					if (!tempBool) {
						tempBool = true;
						RobotMap.ElevSubsystem.elevatorPosition = Elevator.ElevatorPositions.values()[(int) MathExtra.clamp(RobotMap.ElevSubsystem.elevatorPosition.ordinal() - 1, 0, Elevator.ElevatorPositions.values().length-1)];
					}
				}
				else {
					tempBool = false;
				}
				
				RobotMap.ElevSubsystem.positionFind(RobotMap.ElevSubsystem.elevatorPosition);
				break;
			default:
				break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return doneRunning;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		// Call next command
		
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
