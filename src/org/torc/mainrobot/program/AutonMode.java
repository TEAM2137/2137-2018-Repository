package org.torc.mainrobot.program;

public class AutonMode {
	
	//static MoveCommand moveCom;
	
	public static void Init() {
		
		//RobotMap.elevatorDriver.set(ControlMode.PercentOutput, 15);
	}
	
	/*
	public static void Init() {
		
		
		//MoveCommand moveCom = new MoveCommand(8, 0.2F, RobotMap.encoderL);
		//moveCom.start();
		
		
		MotorDrive drivCom = new MotorDrive();
		drivCom.start();
		

	}
*/
	
	public static void Periodic() {
		//RobotMap.driveTrainSubSys.tankDrive(-0.5, 0.5);
		
		//System.out.println(SmartDashboard.putNumber("TestNum", 5));
		
	}
}