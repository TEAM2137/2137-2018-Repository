package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.command.Scheduler;

public class DisabledMode {
	public static void Init() {
		Scheduler.getInstance().removeAll();
		// Dehome elevator
		RobotMap.ElevSubsystem.deHome();
	}
	
	public static void Periodic() {
		
	}
}
