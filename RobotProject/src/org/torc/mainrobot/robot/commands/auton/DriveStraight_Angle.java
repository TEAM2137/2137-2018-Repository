package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;
import org.torc.mainrobot.tools.CLCommand;
import org.torc.mainrobot.tools.MathExtra;

public class DriveStraight_Angle extends CLCommand {
	
	DriveTrain driveSubsystem;
	
	private int targetTicks = 0;
	private double mainSpeed = 0;
	private double angleTarget = 0;
	private boolean relative = true;
	private boolean speedRamp = true;
	
	private int leftEncBase = 0;
	private int rightEncBase = 0;
	private double gyroBase = 0;
	
	private int slowDownPoint = 0;
	
	private boolean goEndCount = false;
	private int endCount = 0;
	private int endCountAmt = 500/20;
	private double distInch = 0;
	
	// PID Stuff
	private double errSum = 0;
	private double dLastPos = 0;
	
	private final double pGain = 0.022;//0.045;
	private final double iGain = 0;
	private final double dGain = 0;//0.68;
	
	public DriveStraight_Angle(DriveTrain dTrain, double inches, double mSpeed, double angle, boolean isRelative, boolean rampSpeed) {
		driveSubsystem = dTrain;
		
		requires(driveSubsystem);
		
		mainSpeed = mSpeed;
		angleTarget = angle;
		
		relative = isRelative;
		speedRamp = rampSpeed;
		
		distInch = inches;
		
		targetTicks = (int) (RobotMap.DriveSubsystem.TicksPerInch * inches); //(int) ((driveSubsystem.TicksPerRev / (driveSubsystem.WheelDiameterIn * Math.PI)) * inches);
		
		slowDownPoint = getSlowDownPoint();
	}
	
	private int getSlowDownPoint() {
		int retVal = 0;
		
		if (mainSpeed <= 0.5) {
			retVal = (targetTicks / 3) * 2;
		}
		else {
			// TODO: Test this speed ramp
			retVal = (targetTicks / 4) * 3;
		}
		
		return retVal;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		leftEncBase = driveSubsystem.getEncoder(DTSide.left);
		rightEncBase = driveSubsystem.getEncoder(DTSide.right);
		
		gyroBase = driveSubsystem.getGyroHeader();
		
		dLastPos = gyroBase;
		
		// Shift to high gear
		driveSubsystem.setShifters(false);
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		
		// Endcount for wait after
		if (goEndCount) {
			endCount++;
			if (endCount >= endCountAmt) {
				finishedCommand = true;
			}
			return;
		}
		
		int currLeftEnc = driveSubsystem.getEncoder(DTSide.left) - leftEncBase; //Math.abs(driveSubsystem.getEncoder(DTSide.left) - leftEncBase);
		int currRightEnc = driveSubsystem.getEncoder(DTSide.right) - rightEncBase; //Math.abs(driveSubsystem.getEncoder(DTSide.right) - rightEncBase);
		
		double leftSpeed = 0;
		double rightSpeed = 0;
		
		double gyroVal = driveSubsystem.getGyroHeader();
		
		if (speedRamp && (currLeftEnc >= slowDownPoint || currRightEnc >= slowDownPoint)) {
			//double velToSet = MathExtra.clamp(MathExtra.lerp(mainSpeed, 0, ( (currLeftEnc - slowDownPoint) / (targetTicks - slowDownPoint) )), 0.005, 1);

			//double encAverage = (currLeftEnc + currRightEnc) / 2;
			double encAverage = (currLeftEnc >= slowDownPoint)?currLeftEnc:currRightEnc;
			
			//double encAverage = currRightEnc;
			double tVar = (double)(encAverage - slowDownPoint) / (double)(targetTicks - slowDownPoint);
			if (mainSpeed >= 0) {
				leftSpeed = MathExtra.clamp(MathExtra.lerp(mainSpeed, 0, tVar), 0.095, 1);
			}
			else {
				leftSpeed = MathExtra.clamp(MathExtra.lerp(mainSpeed, 0, tVar), -1, -0.095);
			}
			rightSpeed = leftSpeed;
		}
		else {
			// Set motor speeds
			leftSpeed = mainSpeed;
			rightSpeed = mainSpeed;
		}
		
		double err = MathExtra.clamp((gyroVal - gyroBase) - angleTarget, -20, 20);
		
		// Add to error sum for Integral
		errSum += err;
		
		double offset = (pGain * err) + (errSum * iGain) + (dGain * (gyroVal - dLastPos));
		rightSpeed += offset;
		leftSpeed -= offset;
		
		dLastPos = gyroVal;
		
		//driveSubsystem.setVelocity(leftSpeed, rightSpeed);
		driveSubsystem.setPercVBus(leftSpeed, rightSpeed);
		
		if (currLeftEnc >= targetTicks || currRightEnc >= targetTicks) {
			finishedCommand = true;
			//driveSubsystem.setShifters(true);
			//driveSubsystem.setVelocity(0, 0);
			//goEndCount = true;
		}
		
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("Finished driving " + distInch + " Inches!");
		
		
		if (speedRamp) {
			driveSubsystem.setVelocity(0, 0);
		}
		else {
			driveSubsystem.setPercVBus(0, 0);
		}
		
		
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
