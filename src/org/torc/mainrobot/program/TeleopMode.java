package org.torc.mainrobot.program;

import org.torc.mainrobot.program.ButtonMap.RCButtons;

import com.ctre.phoenix.ErrorCode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

