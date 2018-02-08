package org.torc.mainrobot.program;

public class TeleopMode {
	
	public static Elevator_Teleop elevTele;
	
	public static void Init() {
		//RobotMap.myRobot.setSafetyEnabled(true);
		RobotMap.ElevSubsystem.homeElevator();
		elevTele = new Elevator_Teleop();
	}
	
	public static void Periodic() {
		elevTele.callUpdate();
		
	}
}

