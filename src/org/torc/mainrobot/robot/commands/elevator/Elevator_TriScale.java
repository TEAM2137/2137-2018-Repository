package org.torc.mainrobot.robot.commands.elevator;

import org.torc.mainrobot.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_TriScale extends Command {
	
	Elevator elevSubsystem;
	
	public Elevator_TriScale(Elevator elevator) {
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
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
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
