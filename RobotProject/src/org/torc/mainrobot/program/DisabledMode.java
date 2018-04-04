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
		
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.left);
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.right);
		
		// Reset the AutonSelector
		RobotMap.AutonSelect = new AutonSelector();
		
		/* Set drivetrain speed to 0 just in 
		 * case there is still a set value. */
		RobotMap.DriveSubsystem.setPercVBus(0, 0);
	}
	
	public static void Periodic() {
		RobotMap.AutonSelect.Update();
	}
}
