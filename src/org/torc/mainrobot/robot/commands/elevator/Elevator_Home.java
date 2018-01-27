package org.torc.mainrobot.robot.commands.elevator;

import org.torc.mainrobot.program.RobotMap;
import edu.wpi.first.wpilibj.command.Command;

public class Elevator_Home extends Command {
	
	enum HomingStates { firstMoveDown, secondMoveUp }
	
	HomingStates homingState = HomingStates.firstMoveDown;
	
	boolean doneRunning = false;
	
	double firstMoveDownPerc = 0.05;
	double secondMoveUpPerc = 0.05;
	
	public Elevator_Home() {
		// Use requires() here to declare subsystem dependencies
		requires(RobotMap.ElevSubsystem);
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
				RobotMap.ElevSubsystem.jogElevatorPerc(-firstMoveDownPerc);
				if (RobotMap.ElevSubsystem.endstop.get()) {
					System.out.println("firstMoveDown Done!");
					homingState = HomingStates.secondMoveUp;
				}
				break;
			case secondMoveUp:
				RobotMap.ElevSubsystem.jogElevatorPerc(secondMoveUpPerc);
				if (!RobotMap.ElevSubsystem.endstop.get()) {
					System.out.println("secondMoveUp Done!");
					RobotMap.ElevSubsystem.zeroEncoder();
					RobotMap.ElevSubsystem.jogElevatorPerc(0);
					doneRunning = true;
				}
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
		RobotMap.ElevSubsystem.hasBeenHomed = true;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
