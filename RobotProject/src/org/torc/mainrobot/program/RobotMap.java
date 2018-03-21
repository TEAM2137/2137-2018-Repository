package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.*;

import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	public static DriveTrain DriveSubsystem;
	
	public static Elevator ElevSubsystem;
	
	public static UltraGrabber GrabberSubsystem;
	
	public static ButtonMap driverControl;
	public static ButtonMap operatorControl;
	
	public static AnalogInput PNUPressure;
	
	public static PigeonIMU mainPigeon;
	
	public static AutonSelector AutonSelect;
	
	public static ClimbRamp ClimbingRamp;
	
	public static ClimbHook ClimbingHook;
	
	public static RobotInfo RobInfo;
	
}
