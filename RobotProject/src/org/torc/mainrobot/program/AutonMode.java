package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonMode {
	
	//public static DigitalInput testInp = new DigitalInput(9);
	
	private static boolean autonRetry = false;
	
	public static void Init() {
		RobotMap.ElevSubsystem.homeElevator();
		RobotMap.GrabberSubsystem.homeGrabber();
		
		RobotMap.DriveSubsystem.setAutonDriving(true);
		
		// Garuntee cube grip for auton
		RobotMap.GrabberSubsystem.setCubeGrip(true);
		
		if (RobotMap.AutonSelect.getAuton()) {
			RobotMap.AutonSelect.startAuton();
		}
		else {
			autonRetry = true;
		}
	}
	
	public static void Periodic() {
		
		if (autonRetry) {
			if (RobotMap.AutonSelect.getAuton()) {
				RobotMap.AutonSelect.startAuton();
				autonRetry = false;
			}
			else {
				System.out.println("Failed AutonGet, retrying...");
			}
		}
		
		SmartDashboard.putNumber("FusionAngle", RobotMap.DriveSubsystem.getGyroHeader());

	}
}