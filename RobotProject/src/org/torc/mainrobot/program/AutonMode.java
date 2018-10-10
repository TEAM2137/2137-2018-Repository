package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class AutonMode {
	
	//public static DigitalInput testInp = new DigitalInput(9);
	
	//private static boolean autonRetry = false;
	
	private static TankModifier tMod;
	private static EncoderFollower encLeft;
	private static EncoderFollower encRight;
	
	public static void Init() {
		RobotMap.ElevSubsystem.homeElevator();
		RobotMap.GrabberSubsystem.homeGrabber();
		
		RobotMap.DriveSubsystem.setAutonDriving(true);
		
		// Garuntee cube grip for auton
		RobotMap.GrabberSubsystem.setCubeGrip(true);
		
		/*
		if (RobotMap.AutonSelect.getAuton()) {
			RobotMap.AutonSelect.startAuton();
		}
		else {
			autonRetry = true;
		}
		*/
		
		// Testing Pathfinder with crap code, organizing later:
		
		// TODO: Get actual values for practicebot
		double wheelbaseWidth = 0.5;
		// wheelDiameter is the diameter of your wheels (or pulley for a track system) in meters
		double wheelDiameter = 0.2;
		int encTicksPerRev = 1000;
		// Max velocity in m/s (pretty sure)
		double maxVel = 1;
		
		Waypoint[] points = new Waypoint[] {
			    new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
			    new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
			    new Waypoint(0, 0, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
			};
		
		Trajectory trajectory = Pathfinder.generate(points, RobotMap.TrajConf);
		
		tMod = new TankModifier(trajectory).modify(wheelbaseWidth);
		
		encLeft = new EncoderFollower(tMod.getLeftTrajectory());
		encRight = new EncoderFollower(tMod.getRightTrajectory());
		
		encLeft.configureEncoder(RobotMap.DriveSubsystem.getEncoder(DTSide.left), encTicksPerRev, wheelDiameter);
		encRight.configureEncoder(RobotMap.DriveSubsystem.getEncoder(DTSide.right), encTicksPerRev, wheelDiameter);
		
		encLeft.configurePIDVA(1.0, 0.0, 0.0, 1 / maxVel, 0);
		encRight.configurePIDVA(1.0, 0.0, 0.0, 1 / maxVel, 0);
	}
	
	public static void Periodic() {
		
		/*
		if (autonRetry) {
			if (RobotMap.AutonSelect.getAuton()) {
				RobotMap.AutonSelect.startAuton();
				autonRetry = false;
			}
			else {
				System.out.println("Failed AutonGet, retrying...");
			}
		}
		*/

		double leftOut = encLeft.calculate(RobotMap.DriveSubsystem.getEncoder(DTSide.left));
		double rightOut = encRight.calculate(RobotMap.DriveSubsystem.getEncoder(DTSide.right));
		
		//TODO: Get heading correction implemented.
		
		// Code from wiki for heading correction (use as a reference)
		/*
		 * double gyro_heading = ... your gyro code here ...    // Assuming the gyro is giving a value in degrees
		   double desired_heading = Pathfinder.r2d(left.getHeading());  // Should also be in degrees
		
		   double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
		   double turn = 0.8 * (-1.0/80.0) * angleDifference;
		   
		   setLeftMotors(l + turn);
		   setRightMotors(r - turn);
		 */
		
		RobotMap.DriveSubsystem.setPercVBus(leftOut, rightOut);
		
		
		SmartDashboard.putNumber("FusionAngle", RobotMap.DriveSubsystem.getGyroHeader());

	}
}