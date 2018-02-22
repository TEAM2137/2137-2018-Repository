package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;
import org.torc.mainrobot.tools.CLCommand;
import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStraight_Angle extends CLCommand {
	
	DriveTrain driveSubsystem;
	
	private int targetTicks = 0;
	private double mainSpeed = 0;
	
	private int leftEncBase = 0;
	private int rightEncBase = 0;
	private double gyroBase = 0;
	
	private int slowDownPoint = 0;
	
	private final double pGain = 0.045;
	
	private double angleTarget = 0;
	
	public DriveStraight_Angle(DriveTrain dTrain, double inches, double mSpeed, double angle) {
		driveSubsystem = dTrain;
		requires(driveSubsystem);
		
		mainSpeed = mSpeed;
		angleTarget = angle;
		
		targetTicks = (int) ((driveSubsystem.TicksPerRev / (driveSubsystem.WheelDiameterIn * Math.PI)) * inches);
		
		slowDownPoint = (targetTicks/3) * 2;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		leftEncBase = driveSubsystem.getEncoder(DTSide.left);
		rightEncBase = driveSubsystem.getEncoder(DTSide.right);
		
		gyroBase = driveSubsystem.getGyroHeader();
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		int currLeftEnc = driveSubsystem.getEncoder(DTSide.left) - leftEncBase;
		int currRightEnc = driveSubsystem.getEncoder(DTSide.right) - rightEncBase;
		
		double leftSpeed = 0;
		double rightSpeed = 0;
		
		if (currLeftEnc >= slowDownPoint || currRightEnc >= slowDownPoint) {
			//double velToSet = MathExtra.clamp(MathExtra.lerp(mainSpeed, 0, ( (currLeftEnc - slowDownPoint) / (targetTicks - slowDownPoint) )), 0.005, 1);
			double encAverage = (currLeftEnc + currRightEnc) / 2;
			//double encAverage = currRightEnc;
			double tVar = (double)(encAverage - slowDownPoint) / (double)(targetTicks - slowDownPoint);
			leftSpeed = MathExtra.clamp(MathExtra.lerp(mainSpeed, 0, tVar), 0.08, 1);
			rightSpeed = leftSpeed;
			
		}
		else {
			// Set motor speeds
			leftSpeed = mainSpeed;
			rightSpeed = mainSpeed;
		}
		
		double err = (driveSubsystem.getGyroHeader() - gyroBase) - angleTarget;
		SmartDashboard.putNumber("DriveStraightError", err);
		
		double offset = pGain * err;
		rightSpeed -= offset;
		leftSpeed += offset;
		
		driveSubsystem.setVelocity(leftSpeed, rightSpeed);
		
		if (currLeftEnc >= targetTicks || currRightEnc >= targetTicks) {
			finishedCommand = true;
		}
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("Finished driving!");
		driveSubsystem.setPercVBus(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
