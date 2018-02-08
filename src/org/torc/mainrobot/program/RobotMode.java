package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.XboxController;

//import org.torc.mainrobot.robot.subsystems.DriveTrain;

public class RobotMode {
	/*
	 * This Function is called when the robot first boots up.
	 * 
	 * Any initialization code necessary for Robot-level access (most/any state),
	 * should go here.
	 */
	public static void Init() {
		/*
		// left encoder
		RobotMap.encoderL = new Encoder(0, 1);
		RobotMap.encoderR = new Encoder(2, 3);
		
		RobotMap.shifter = new DoubleSolenoid(0, 0, 1);
	    RobotMap.frontLeft = new Talon(0);
	    RobotMap.frontRight = new Talon(1);
	    RobotMap.rearLeft = new Talon (2);
		RobotMap.rearRight = new Talon (3);
		// Add talons to list
		RobotMap.talonList.add(RobotMap.frontLeft);
		RobotMap.talonList.add(RobotMap.frontRight);
		RobotMap.talonList.add(RobotMap.rearLeft);
		RobotMap.talonList.add(RobotMap.rearRight);
		
		RobotMap.myRobot = new RobotDrive(RobotMap.frontLeft, 
		RobotMap.rearLeft, 
		RobotMap.frontRight, 
		RobotMap.rearRight);
		System.out.println("CALLED ROBOTDRIVE CONSTRUCTOR");
		// Flip left motors
		RobotMap.myRobot.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		RobotMap.myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		// TODO: Look into setSafetyEnabled
		RobotMap.myRobot.setSafetyEnabled(false);
		RobotMap.driveTrainSubSys = new DriveTrain();
		
		// TODO: Reset expiration
		//RobotMap.myRobot.setExpiration(0.1);
		Compressor compress = new Compressor(0);
		// Set compressor status to automatically 
		// refill the compressor tank when below 120psi
		compress.setClosedLoopControl(true);
		*/		
		
		RobotMap.mainController = new ButtonMap(new XboxController(0));
		
		RobotMap.ElevSubsystem = new Elevator();
		
		
	}
	
	public static void Periodic() {
		// All periodic robot things. usually calls to functions that need to be 
	}
}
