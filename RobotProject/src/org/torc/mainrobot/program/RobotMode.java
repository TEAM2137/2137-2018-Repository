package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.ClimbHook;
import org.torc.mainrobot.robot.subsystems.ClimbRamp;
import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.UltraGrabber;
import org.torc.mainrobot.tools.Pneumatics;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.FitMethod;

//import org.torc.mainrobot.robot.subsystems.DriveTrain;

public class RobotMode {
	/*
	 * This Function is called when the robot first boots up.
	 * 
	 * Any initialization code necessary for Robot-level access (most/any state),
	 * should go here.
	 */
	public static void Init() {
		
		// Keep this at top of other constructor calls
		RobotMap.RobInfo = new RobotInfo(9);
		
		// Init camera server for getting webcams from dashboard
		CameraServer.getInstance().startAutomaticCapture();
		
		RobotMap.PNUPressure = new AnalogInput(0);
		
		RobotMap.driverControl = new ButtonMap(new XboxController(0));
		RobotMap.operatorControl = new ButtonMap(new XboxController(1));
		
		RobotMap.DriveSubsystem = new DriveTrain(10, 11, 22, 23, 4);
		
		RobotMap.ElevSubsystem = new Elevator(24, 12, 0);		
		
		RobotMap.GrabberSubsystem = new UltraGrabber(0, 1, 13, 1, 2, 2);
		
		RobotMap.AutonSelect = new AutonSelector();
		
		RobotMap.ClimbingHook = new ClimbHook(2);
		
		RobotMap.ClimbingRamp = new ClimbRamp(4);
		
		RobotMap.TrajConf = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);
	}
	
	/**
	 * All periodic robot things. usually calls to 
	 * functions that need to be constantly updated
	 */
	public static void Periodic() {
		
		SmartDashboard.putNumber("PSI", Pneumatics.getPSIFromAnalog(RobotMap.PNUPressure));
		
	}
}
