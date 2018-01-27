package org.torc.mainrobot.program;

public class TestMode {
	public static void Init() {
		
	}
	public static void Periodic() {
		System.out.println("Robot Testing. Random number here: " + Math.random());
	}
}
