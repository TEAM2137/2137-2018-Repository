package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.tools.Pneumatics;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import org.torc.mainrobot.robot.subsystems.DriveTrain;

public class RobotMode {
	/*
	 * This Function is called when the robot first boots up.
	 * 
	 * Any initialization code necessary for Robot-level access (most/any state),
	 * should go here.
	 */
	public static void Init() {
		
		// Init camera server for getting webcams from dashboard
		CameraServer.getInstance().startAutomaticCapture();
		
		RobotMap.PNUPressure = new AnalogInput(0);
		
		RobotMap.driverControl = new ButtonMap(new XboxController(0));
		RobotMap.operatorControl = new ButtonMap(new XboxController(1));
		
		RobotMap.DriveSubsystem = new DriveTrain(22, 23, 10, 11, 4);
		
		RobotMap.ElevSubsystem = new Elevator(24, 0);
		
	}
	
	/**
	 * All periodic robot things. usually calls to 
	 * functions that need to be constantly updated
	 */
	public static void Periodic() {
		
		SmartDashboard.putNumber("PSI", Pneumatics.getPSIFromAnalog(RobotMap.PNUPressure));
		
	}
}
