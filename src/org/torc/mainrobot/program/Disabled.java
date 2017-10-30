package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.command.Scheduler;

public class Disabled {
	public static void Init() {
		
	}
	
	public static void Periodic() {
		Scheduler.getInstance().run();
	}
}
