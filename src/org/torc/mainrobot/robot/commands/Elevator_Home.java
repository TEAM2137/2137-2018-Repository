package org.torc.mainrobot.robot.commands;

import org.torc.mainrobot.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Home extends Command {
	
	enum HomingStates { firstMoveDown, secondMoveUp }
	
	/**
	 * The calling Subsystem of the command.
	 */
	Elevator elevSubsystem; 
	
	HomingStates homingState = HomingStates.firstMoveDown;
	
	boolean doneRunning = false;
	
	double firstMoveDownPerc = 0.3;
	double secondMoveUpPerc = 0.3;
	
	public Elevator_Home(Elevator elevator) {
		// Use requires() here to declare subsystem dependencies
		elevSubsystem = elevator;
		requires(elevSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		System.out.println("Elevator_Home Init");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		
		switch (homingState) {
			case firstMoveDown:
				elevSubsystem.jogElevatorPerc(-firstMoveDownPerc);
				if (elevSubsystem.endstop.get()) {
					System.out.println("firstMoveDown Done!");
					homingState = HomingStates.secondMoveUp;
				}
				break;
			case secondMoveUp:
				elevSubsystem.jogElevatorPerc(secondMoveUpPerc);
				if (!elevSubsystem.endstop.get()) {
					System.out.println("secondMoveUp Done!");
					elevSubsystem.zeroEncoder();
					elevSubsystem.jogElevatorPerc(0);
					doneRunning = true;
				}
				break;
		}
		
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		return doneRunning;
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
