package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;
import org.torc.mainrobot.teleopcontrols.DriveTrain_Teleop;
import org.torc.mainrobot.teleopcontrols.Elevator_Teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopMode {
	
	public static Elevator_Teleop elevTele;
	
	public static DriveTrain_Teleop driveTele;
	
	
	public static void Init() {
		//RobotMap.myRobot.setSafetyEnabled(true);
		
		elevTele = new Elevator_Teleop();
		driveTele = new DriveTrain_Teleop();
		
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.left);
		RobotMap.DriveSubsystem.zeroEncoder(DTSide.right);
		
	}
	
	public static void Periodic() {
		
		elevTele.callUpdate();
		driveTele.callUpdate();
		
		SmartDashboard.putBoolean("GrabberEndstop", RobotMap.GrabberSubsystem.getEndstop());
		SmartDashboard.putBoolean("GrabberCubeEye", RobotMap.GrabberSubsystem.getCubeEye());
		SmartDashboard.putNumber("GrabberEncoder", RobotMap.GrabberSubsystem.getEncoder());
	}
}

