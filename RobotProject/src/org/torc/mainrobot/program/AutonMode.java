package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonMode {
	
	//public static DigitalInput testInp = new DigitalInput(9);
	
	public static void Init() {
		RobotMap.ElevSubsystem.homeElevator();
		RobotMap.GrabberSubsystem.homeGrabber();
		
		RobotMap.AutonSelect.getAuton();
		RobotMap.AutonSelect.startAuton();
		
		RobotMap.DriveSubsystem.setAutonDriving(true);
	}
	
	public static void Periodic() {
		
		SmartDashboard.putNumber("FusionAngle", RobotMap.DriveSubsystem.getGyroHeader());

	}
}