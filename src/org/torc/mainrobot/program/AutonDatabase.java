package org.torc.mainrobot.program;

import org.torc.mainrobot.program.AutonSelector.AutonPriority;
import org.torc.mainrobot.program.AutonSelector.StartPositions;
import org.torc.mainrobot.robot.commands.auton.UltraGrabber_SpitCube.SpitSpeeds;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.*;
import org.torc.mainrobot.tools.CommandList;
import org.torc.mainrobot.robot.commands.auton.*;

import edu.wpi.first.wpilibj.DriverStation;

public class AutonDatabase {
	
	private static CommandList ComList;
	private static StartPositions StartPosition;
	private static AutonPriority AutonPri;
	
	private static char[] GameData;
	
	/**
	 * Gets the proper Auton routine in CLCommands into a givin CommandList.
	 * (Note: CLCommands are ADDED into the givin CommandList).
	 * 
	 * @param comList
	 * @param startPosition
	 * @param autonPri
	 */
	public static void GetAuton(CommandList cList, StartPositions sPosition, AutonPriority autonP) {
		ComList = cList;
		StartPosition = sPosition;
		AutonPri = autonP;
		String gData = DriverStation.getInstance().getGameSpecificMessage();
		GameData = new char[3];
		GameData[0] = gData.charAt(0);
		GameData[1] = gData.charAt(1);
		GameData[2] = gData.charAt(2);
		
		autonGetStart();
	}
	
	
	private static void autonGetStart() {
		System.out.println("GameData: " + GameData[0] + GameData[1] + GameData[2]);
		
		// Start Intake for all
		ComList.addParallel(new UltraGrabber_SetIntake(RobotMap.GrabberSubsystem, GrabberSpeeds.cubeKeep));
		
		switch(StartPosition) {
			// Start C
			case left:
				autonGetBC();
				break;
			// Start A
			case center:
				// Right Plate
				// Drive a little less than 100in straight
				if (GameData[0] == 'R') {
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 50, 0.25, 0, true, false));
					ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
					ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 2));
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 42, 0.25, 0, true, true));
					ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
				}
				// Left Plate
				// Zig-Zag to the plate across and deposit
				else {
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 7, 0.25, 0, true, false));
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 122, 0.25, -62, true, false));
					ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
					ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 2));
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 30, 0.25, 62, true, true));
					ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
				}
				break;
			// Start B
			case right:
				autonGetBC();
				break;
		}
	}
	
	private static void autonGetBC() {
		
		boolean isRight;
		char lookingFor;
		
		switch (StartPosition) {
			case left:
				isRight = false;
				lookingFor = 'L';
				break;
			case right:
				isRight = true;
				lookingFor = 'R';
				break;
			default:
				System.out.println("Incorrect BC position!!");
				return;
		}
		
		boolean samePlate = (GameData[0] == lookingFor && GameData[1] == lookingFor);
		
		// switch goto
		if ((GameData[0] == lookingFor && GameData[1] != lookingFor) || (samePlate && AutonPri == AutonPriority.sw1tch)) {
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 137, 0.25, 0, true, false));
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 12, 0.25, isRight?-90:90, true, true));
			// TODO: Add cube dropoff
		}
		// scale goto
		else if ((GameData[0] != lookingFor && GameData[1] == lookingFor) || (samePlate && AutonPri == AutonPriority.scale)) {
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 260, 0.25, 0, true, true));
			// TODO: Add cube dropoff
		}
		else if (GameData[0] != lookingFor && GameData[1] != lookingFor) {
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 214, 0.25, 0, true, false));
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 77, 0.25, isRight?-90:90, true, false));
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 46, 0.25, isRight?90:-90, true, true));
			// TODO: Add cube dropoff
		}
		
		
	}
}
