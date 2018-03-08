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
	/*
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	public static Talon frontLeft, frontRight, rearLeft, rearRight;
	// A list which is supposed to be populated with the above Talons on init. 
	// TODO: Garuntee that we need this
	public static List<PWMSpeedController> talonList = new ArrayList<>();
	
	public static DoubleSolenoid shifter;
	
	public static RobotDrive myRobot;
	
	public static DriveTrain driveTrainSubSys;
	
	public static Encoder encoderL, encoderR;
	
	public static float WheelDiameter = 4F;
	*/
	
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
