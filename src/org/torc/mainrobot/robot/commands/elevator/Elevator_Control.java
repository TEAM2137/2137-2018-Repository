package org.torc.mainrobot.robot.commands.elevator;

import org.torc.mainrobot.program.ButtonMap;
import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Control extends Command {
	
	/**
	 * The calling Subsystem of the command.
	 */
	public Elevator elevSubsystem;
	
	public enum elevatorControlMode { jogging, position, none }
	
	elevatorControlMode controlMode = elevatorControlMode.jogging;
	
	boolean doneRunning = false;
	
	int jogInterval = 7000;
	
	public Elevator_Control(Elevator elevator) {
		// Use requires() here to declare subsystem dependencies
		elevSubsystem = elevator;
		requires(elevSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		// Check if mode change
		
		// To Position
		if (RobotMap.xController0.getAButtonPressed()) {
			controlMode = elevatorControlMode.position;
		}
		// To Jogging
		else if (RobotMap.xController0.getBButtonPressed()) {
			controlMode = elevatorControlMode.jogging;
		}
		
		if (RobotMap.xController0.getXButtonPressed()) {
			
		}
		
		switch(controlMode) {
			case jogging:
				if(RobotMap.xController0.getBumperPressed(GenericHID.Hand.kRight)) {
					System.out.println("Jogging Right Bumper");
					// TODO: Test this with the elevator.
					elevSubsystem.jogElevatorPos(jogInterval);
				}
				else if (RobotMap.xController0.getBumperPressed(GenericHID.Hand.kLeft)) {
					System.out.println("Jogging Left Bumper");
					//targetPosition -= jogInterval;
					elevSubsystem.jogElevatorPos(-jogInterval);
				}
				break;
			case position:
				if(RobotMap.xController0.getBumperPressed(GenericHID.Hand.kRight)) {
					elevSubsystem.jogElevatorPosInc(1);
				}
				else if (RobotMap.xController0.getBumperPressed(GenericHID.Hand.kLeft)) {
					elevSubsystem.jogElevatorPosInc(-1);
				}
				
				elevSubsystem.positionFind(elevSubsystem.elevatorPosition);
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
