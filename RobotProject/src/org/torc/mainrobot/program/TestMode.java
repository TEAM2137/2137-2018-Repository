package org.torc.mainrobot.program;

public class TestMode {
	public static void Init() {
		System.out.println("Pack-Homing Grabber");
		RobotMap.GrabberSubsystem.homeGrabberPacked();
		// Home the hook
		RobotMap.ClimbingHook.closeHook();
	}
	public static void Periodic() {
		System.out.println("Robot Testing. Random number here: " + Math.random());
	}
}
