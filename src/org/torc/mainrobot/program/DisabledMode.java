package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;
import org.torc.mainrobot.tools.CommandList;

import edu.wpi.first.wpilibj.command.Scheduler;

public class DisabledMode {
	public static void Init() {
		// Remove all commands
		Scheduler.getInstance().removeAll();
		// Stop all CommandLists
		CommandList.StopAllCommandLists();
		// Dehome elevator
		RobotMap.ElevSubsystem.deHome();
		// Dehome grabber
		RobotMap.GrabberSubsystem.deHome();
		
		// zero encoders for testing
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.left);
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.right);
		
		RobotMap.AutonSelect = new AutonSelector();
		
	}
	
	public static void Periodic() {
		
	}
}
