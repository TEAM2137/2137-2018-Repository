package org.torc.mainrobot.program;

import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberSpeeds;
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
		
	}
	
	public static void Periodic() {
		
		elevTele.callUpdate();
		driveTele.callUpdate();
		
		
		if (RobotMap.driverControl.getButton(RCButtons.grabberSpitSlow, false)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.pickup);
		}
		else if (RobotMap.driverControl.getButton(RCButtons.grabberSpitFast, false)) {
			RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.shooting);
		}
		else {
			//RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.none);
		}
		
		
		/*
		if (RobotMap.driverControl.getButton(RCButtons.grabberSpitSlow, false)) {
			RobotMap.GrabberSubsystem.jogGrabberPerc(0.1);
		}
		else if (RobotMap.driverControl.getButton(RCButtons.grabberSpitFast, false)) {
			RobotMap.GrabberSubsystem.jogGrabberPerc(-0.1);
		}
		else {
			RobotMap.GrabberSubsystem.jogGrabberPerc(0);
		}
		*/
		
		SmartDashboard.putBoolean("GrabberEndstop", RobotMap.GrabberSubsystem.getEndstop());
		SmartDashboard.putBoolean("GrabberCubeEye", RobotMap.GrabberSubsystem.getCubeEye());
		SmartDashboard.putNumber("GrabberEncoder", RobotMap.GrabberSubsystem.getEncoder());
	}
}

