package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.tools.CLCommand;

/**
 *
 */

public class PercDrive_Time extends CLCommand {
	
	DriveTrain dTrainSubsystem;
	
	private double timeToWait = 0;
	
	private double drivePerc = 0;
	
	private double timeCounter = 0;
	
	public PercDrive_Time(DriveTrain dTrainSub, double timeMs, double perc) {
		dTrainSubsystem = dTrainSub;
		
		requires(dTrainSubsystem);
		
		timeToWait = timeMs / 20;
		drivePerc = perc;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		dTrainSubsystem.setPercVBus(drivePerc, drivePerc);
		
		if (timeCounter >= timeToWait) {
			finishedCommand = true;
		}
		
		timeCounter++;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		dTrainSubsystem.setPercVBus(0, 0);
	}
}