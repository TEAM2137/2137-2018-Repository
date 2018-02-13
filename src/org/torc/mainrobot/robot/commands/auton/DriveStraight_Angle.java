package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;
import org.torc.mainrobot.tools.CLCommand;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DriveStraight_Angle extends CLCommand {
	
	private double targetAngle, baseVelocity, distance;
	
	private float kP = 0.1F;
	
	public DriveStraight_Angle(double targAngle, double baseVel, double dist) {
		// Use requires() here to declare subsystem dependencies
		requires(RobotMap.DriveSubsystem);
		
		targetAngle = targAngle;
		baseVelocity = baseVel;
		distance = dist;
	}

	
	
	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.left);
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.right);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double error = RobotMap.DriveSubsystem.getGyroHeader() - targetAngle;
		SmartDashboard.putNumber("Error", error);
		double offset = kP * error;
		SmartDashboard.putNumber("Offset", offset);
		
		SmartDashboard.putNumber("LeftMasterEncoder", RobotMap.DriveSubsystem.getEncoder(DTSide.left));
		SmartDashboard.putNumber("RightMasterEncoder", RobotMap.DriveSubsystem.getEncoder(DTSide.right));
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
