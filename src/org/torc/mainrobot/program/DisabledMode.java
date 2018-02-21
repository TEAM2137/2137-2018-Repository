package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;

import edu.wpi.first.wpilibj.command.Scheduler;

public class DisabledMode {
	public static void Init() {
		Scheduler.getInstance().removeAll();
		// Dehome elevator
		RobotMap.ElevSubsystem.deHome();
		// Dehome grabber
		RobotMap.GrabberSubsystem.deHome();
		
		// zero encoders for testing
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.left);
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.right);
		
	}
	
	public static void Periodic() {
		
	}
}
