package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;

public class RobotInit {
	/*
	 * This Function is called when the robot first boots up.
	 * 
	 * Any initialization code necessary for Robot-level access (most/any state),
	 * should go here.
	 */
	public static void Init() {
		// left encoder
		RobotMap.encoderL = new Encoder(0, 1);
		RobotMap.encoderR = new Encoder(2, 3);
		
		RobotMap.shifter = new DoubleSolenoid(0, 0, 1);
	    RobotMap.frontLeft = new Talon(0);
	    RobotMap.frontRight = new Talon(1);
	    RobotMap.rearLeft = new Talon (2);
		RobotMap.rearRight = new Talon (3);
		RobotMap.myRobot = new RobotDrive(RobotMap.frontLeft, 
		RobotMap.rearLeft, 
		RobotMap.frontRight, 
		RobotMap.rearRight);
		System.out.println("CALLED ROBOTDRIVE CONSTRUCTOR");
		RobotMap.myRobot.setExpiration(0.1);
		Compressor compress = new Compressor(0);
		// Set compressor status to automatically 
		// refill the compressor tank when below 120psi
		compress.setClosedLoopControl(true);
		
	}
}
