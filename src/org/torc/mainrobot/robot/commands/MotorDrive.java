package org.torc.mainrobot.robot.commands;

import org.torc.mainrobot.program.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

public class MotorDrive extends Command {
	
	public MotorDrive() {
		// Use requires() here to declare subsystem dependencies
		requires(RobotMap.driveTrainSubSys);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		RobotMap.driveTrainSubSys.tankDrive(0.5, 0.5);
		System.out.println("TankDrive Call");
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
