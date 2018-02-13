package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.DriveTrain;
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
		
		RobotMap.mainController = new ButtonMap(new XboxController(0));
		
		RobotMap.DriveSubsystem = new DriveTrain(22, 23, 10, 11, 4);
		
		RobotMap.ElevSubsystem = new Elevator(24, 0);
		
		
	}
	
	public static void Periodic() {
		// All periodic robot things. usually calls to functions that need to be 
	}
}
