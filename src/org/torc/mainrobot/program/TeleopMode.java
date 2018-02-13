package org.torc.mainrobot.program;

import org.torc.mainrobot.teleopcontrols.DriveTrain_Teleop;
import org.torc.mainrobot.teleopcontrols.Elevator_Teleop;

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

