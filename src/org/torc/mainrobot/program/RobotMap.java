package org.torc.mainrobot.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.torc.mainrobot.robot.subsystems.*;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMSpeedController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;

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
	
	public static Elevator ElevSubsystem;
	
	public static int Elev_TimeoutMs = 10;
	public static int Elev_SlotIdx = 0;
	public static int Elev_PIDLoopIdx = 0;
	
}
