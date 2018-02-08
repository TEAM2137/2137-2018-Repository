package org.torc.mainrobot.robot.commands.elevator;

import org.torc.mainrobot.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_TriScale extends Command {
	
	Elevator elevSubsystem;
	
	boolean finished = false;
	
	/*
	public Elevator_TriScale(Elevator elevator) {
		elevSubsystem = elevator;
		//requires(elevSubsystem);
	}
	*/
	public Elevator_TriScale(Elevator elevator) {
		elevSubsystem = elevator;
		//requires(elevSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		elevSubsystem.positionFind(Elevator.ElevatorPositions.floor);
	}

	boolean temp = true;
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (elevSubsystem.elevatorPosition == Elevator.ElevatorPositions.high) {
			System.out.println("TriScale Finished");
			finished = true;
		}
		if (elevSubsystem.cubeInput.get()) {
			if (temp) {
				temp = false;
				elevSubsystem.jogElevatorPosInc(1);
			}
		}
		else {
			temp = true;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		return finished;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
