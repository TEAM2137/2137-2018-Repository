package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;
import org.torc.mainrobot.tools.CLCommand;
import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraight extends CLCommand {
	
	DriveTrain driveSubsystem;
	
	private int targetTicks = 0;
	private double mainSpeed = 0;
	
	private int leftEncBase = 0;
	private int rightEncBase = 0;
	
	private int slowDownPoint = 0;
	
	public DriveStraight(DriveTrain dTrain, double inches, double mSpeed) {
		driveSubsystem = dTrain;
		requires(driveSubsystem);
		
		mainSpeed = mSpeed;
		
		targetTicks = (int) ((driveSubsystem.TicksPerRev / (driveSubsystem.WheelDiameterIn * Math.PI)) * inches);
		
		slowDownPoint = (targetTicks/3) * 2;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		leftEncBase = driveSubsystem.getEncoder(DTSide.left);
		rightEncBase = driveSubsystem.getEncoder(DTSide.right);
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		int currLeftEnc = driveSubsystem.getEncoder(DTSide.left) - leftEncBase;
		int currRightEnc = driveSubsystem.getEncoder(DTSide.right) - rightEncBase;
		
		if (currLeftEnc >= slowDownPoint || currRightEnc >= slowDownPoint) {
			//double velToSet = MathExtra.clamp(MathExtra.lerp(mainSpeed, 0, ( (currLeftEnc - slowDownPoint) / (targetTicks - slowDownPoint) )), 0.005, 1);
			double encAverage = (currLeftEnc + currRightEnc) / 2;
			//double encAverage = currRightEnc;
			double tVar = (double)(encAverage - slowDownPoint) / (double)(targetTicks - slowDownPoint);
			double velToSet = MathExtra.clamp(MathExtra.lerp(mainSpeed, 0, tVar), 0.08, 1);
			driveSubsystem.setVelocity(velToSet, velToSet);
		}
		else {
			// Set motor speeds
			driveSubsystem.setVelocity(mainSpeed, mainSpeed);
		}
		
		
		
		if (currLeftEnc >= targetTicks || currRightEnc >= targetTicks) {
			finishedCommand = true;
		}
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("Finished driving!");
		driveSubsystem.setVelocity(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
