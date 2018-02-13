package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.commands.auton.TestAutonCommand;
import org.torc.mainrobot.tools.CommandList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonMode {
	
	
	
	public static void Init() {
		RobotMap.DriveSubsystem.zeroGyro();
		
		CommandList testCom = new CommandList();
		testCom.addCommand(new TestAutonCommand("FirstCommand!!"));
		testCom.addCommand(new TestAutonCommand("SecondCommand!!"));
		testCom.addCommand(new TestAutonCommand("LastCommand!!"));
		testCom.start();
	}
	
	
	static boolean gyroReset = false;
	
	//static boolean addCommand = false;
	
	public static void Periodic() {
		
		/*
		addCommand = SmartDashboard.getBoolean("AddCommand", false);
		SmartDashboard.putBoolean("AddCommand", addCommand);
		if (addCommand) {
			addCommand = false;
			SmartDashboard.putBoolean("AddCommand", addCommand);
		
			System.out.println("New TestCommand added!");
		}
		*/
		
		SmartDashboard.putNumber("FusionAngle", RobotMap.DriveSubsystem.getGyroHeader());
		
		gyroReset = SmartDashboard.getBoolean("GyroReset", false);
		SmartDashboard.putBoolean("GyroReset", gyroReset);
		if (gyroReset) {
			gyroReset = false;
			SmartDashboard.putBoolean("GyroReset", gyroReset);
			RobotMap.DriveSubsystem.zeroGyro();
			System.out.println("Test gyro zero!");
		}
		
		

	}
}