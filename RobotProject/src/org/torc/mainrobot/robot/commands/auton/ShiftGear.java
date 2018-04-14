package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.robot.Robot;
import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.tools.CLCommand;

import edu.wpi.first.wpilibj.command.Command;

public class ShiftGear extends CLCommand {
	
	private DriveTrain dTrainSubsystem;
	
	private boolean gearToSwitch;
	
	/**
	 * @param dTrain DriveTrain subsystem.
	 * @param gear Low gear is true, high is false.
	 */
	public ShiftGear(DriveTrain dTrain, boolean gear) {
		dTrainSubsystem = dTrain;
		
		gearToSwitch = gear;
		
		requires(dTrainSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		dTrainSubsystem.setShifters(gearToSwitch);
		
		finishedCommand = true;
	}
}
