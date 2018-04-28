package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.tools.CLCommand;
import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Position_Angle extends CLCommand {
	
	DriveTrain driveSubsystem;
	
	private double mainSpeed = 0;
	
	private double gyroBase = 0;
	
	private double errSum = 0;
	private double dLastPos = 0;
	
	private final double pGain = 0.024;//0.24;//0.045;
	private final double iGain = 0;
	private final double dGain = 0;//1.5;//0.68;
	
	private final double endRange = 3;
	private final int endWait = 250 / 20;
			
	private int endCount = 0;
	
	private double angleTarget = 0;
	
	private boolean relative = true;
	
	private boolean kMoving = false;
	
	public Position_Angle(DriveTrain dTrain, double mSpeed, double angle, boolean isRelative, boolean keepMoving) {
		driveSubsystem = dTrain;
		
		requires(driveSubsystem);
		
		mainSpeed = mSpeed;
		angleTarget = angle;
		
		relative = isRelative;
		
		kMoving = keepMoving;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		gyroBase = driveSubsystem.getGyroHeader();
		
		dLastPos = gyroBase;
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		
		double leftSpeed = 0;
		double rightSpeed = 0;
		
		double gyroVal = driveSubsystem.getGyroHeader() - (relative?gyroBase:0);
		
		double err = MathExtra.clamp(gyroVal - angleTarget, -20, 20);
		SmartDashboard.putNumber("DriveStraightError", err);
		
		// Add to error sum for Integral
		errSum += err;
		
		double offset = (pGain * err) + (errSum * iGain) + (dGain * (gyroVal - dLastPos));
		
		// Clamp offset
		double minClamp = 0.16;
		if (offset > 0) {
			offset = (offset>=minClamp)?offset:minClamp;
		}
		else {
			offset = (offset<=-minClamp)?offset:-minClamp;
		}

		rightSpeed = offset;
		leftSpeed = -offset;
		
		// If kMoving, only let the min for the motor values be 0;
		if (kMoving) {
			rightSpeed = (rightSpeed>0)?rightSpeed:0;
			leftSpeed = (leftSpeed>0)?leftSpeed:0;
		}
		
		dLastPos = gyroVal;
		
		driveSubsystem.setVelocity(leftSpeed, rightSpeed);
		//driveSubsystem.setPercVBus(leftSpeed, rightSpeed);
		
		if (gyroVal > (angleTarget - endRange) && gyroVal < (angleTarget + endRange) ) {
			endCount++;
		}
		else {
			endCount = 0;
		}
		
		if (endCount >= endWait) {
			finishedCommand = true;
		}
		
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("Finished turning!");
		driveSubsystem.setVelocity(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
