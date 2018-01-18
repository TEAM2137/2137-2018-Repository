package org.torc.mainrobot.robot.commands;

import org.torc.mainrobot.program.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */

/*
 * This Command is meant to move a certain distance, at a certain speed.
 * 
 * Currently (as of 12/12/2017), It's going to be real crap.
 */
public class MoveCommand extends Command {
	
	public float distance = 0F;
	
	public float speed = 0F;
	// Must be set by calling class
	public RobotDrive robDrive;
	
	public Encoder encoder;
	
	@SuppressWarnings("unused")
	private double origDist;
	
	public MoveCommand(float dist, float spd, Encoder enc) {
		// Use requires() here to declare subsystem dependencies
		requires(RobotMap.driveTrainSubSys);
		distance = dist;
		speed = spd;
		encoder = enc;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		// Set original distance for subtraction later
		origDist = encoder.getDistance();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		RobotMap.driveTrainSubSys.tankDrive(-0.5, 0.5);
		// Check for distance
		/*
		if (EncoderExtra.EncoderDistance(RobotMap.WheelDiameter, (encoder.getDistance() - origDist)) >= distance) {
			// End command
			isFinished();
		}
		*/
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		/*
		for (PWMSpeedController cont : controller) {
			cont.set(0);
		}
		*/
		RobotMap.driveTrainSubSys.stop();
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
