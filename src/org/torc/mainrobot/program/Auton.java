package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.commands.*;

public class Auton {
	
	//static MoveCommand moveCom;
	
	public static void Init() {
		
		/*
		MoveCommand moveCom = new MoveCommand(8, 0.2F, RobotMap.encoderL);
		moveCom.start();
		*/
		
		// Test Auton Code
		MotorDrive drivCom = new MotorDrive();
		drivCom.start();
		

	}
	
	public static void Periodic() {
		//RobotMap.driveTrainSubSys.tankDrive(-0.5, 0.5);
	}
}
