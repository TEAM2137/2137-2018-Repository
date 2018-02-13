package org.torc.mainrobot.program;

public class TeleopMode {
	
	public static Elevator_Teleop elevTele;
	
	public static DriveTrain_Teleop driveTele;
	
	public static void Init() {
		//RobotMap.myRobot.setSafetyEnabled(true);
		RobotMap.ElevSubsystem.homeElevator();
		elevTele = new Elevator_Teleop();
		driveTele = new DriveTrain_Teleop();
	}
	
	public static void Periodic() {
		elevTele.callUpdate();
		driveTele.callUpdate();
	}
}

