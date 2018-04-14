package org.torc.mainrobot.program;

import org.torc.mainrobot.program.AutonSelector.AutonPriority;
import org.torc.mainrobot.program.AutonSelector.StartPositions;
import org.torc.mainrobot.robot.commands.auton.UltraGrabber_SpitCube.SpitSpeeds;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.Elevator.ElevatorPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.*;
import org.torc.mainrobot.tools.CommandList;
import org.torc.mainrobot.robot.commands.Command_PauseUntil;
import org.torc.mainrobot.robot.commands.auton.*;

import edu.wpi.first.wpilibj.DriverStation;

public class AutonDatabase {
	
	private static CommandList ComList;
	
	private static AutonSelector.AutonData aData;
	
	private static char[] GameData;
	
	/**
	 * Gets the proper Auton routine in CLCommands into a givin CommandList.
	 * (Note: CLCommands are ADDED into the givin CommandList).
	 * 
	 * @param cList
	 * @param autonData
	 * @return If the auton get was successful or not.
	 */
	public static boolean GetAuton(CommandList cList, AutonSelector.AutonData autonData) {
		ComList = cList;
		
		aData = autonData;
		
		String gData = DriverStation.getInstance().getGameSpecificMessage();
		
		if (gData.length() < 3) {
			// Unsucessful or incorrect string
			return false;
		}
		
		// uppercase gData
		gData.toUpperCase();
		
		GameData = new char[3];
		GameData[0] = gData.charAt(0);
		GameData[1] = gData.charAt(1);
		GameData[2] = gData.charAt(2);
		
		autonGetStart();
		
		// Successful
		return true;
	}
	
	private static void autonGetStart() {
		System.out.println("GameData: " + GameData[0] + GameData[1] + GameData[2]);
		
		//ComList.addSequential(new TestAutonCommand("Starting auton in a couple of seconds!"));
		
		// Start Intake for all
		ComList.addParallel(new UltraGrabber_SetIntake(RobotMap.GrabberSubsystem, GrabberSpeeds.cubeKeep));
		
		switch(aData.startPos) {
			// Start C
			case left:
				autonGetBC();
				break;
			// Start A
			case center:
				// Right Plate
				// Drive a little less than 100in straight
				if (GameData[0] == 'R') {
					ComList.addSequential(new TestAutonCommand("Waiting for grabberHome!", 1000));
					ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
					ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 4));
					/*
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 50, 0.25, 0, false));
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 44, 0.25, 0, true));
					*/
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 94, 0.50, 0, true));
					ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
					
																						// -55
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, -32, 0.35, 0, true));
					
																						// -50
					ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.5, -55, true, false));
					
					ComList.addSequential(new DriveStraight_Eye(RobotMap.DriveSubsystem, RobotMap.GrabberSubsystem, RobotMap.ElevSubsystem, 0.25, 0, 1000));
					
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, -12, 0.25, 0, true));
					
					ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.5, 79, true, false));
					
					ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 4));
					
					//ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 48, 0.35, 0, true));
					ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 1000, 0.50));
					
					ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
				}
				// Left Plate
				// Zig-Zag to the plate across and deposit
				else {
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 7, 0.25, 0, false));
					ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.25, -62, true, false));
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 106, 0.50, 0, true));
					ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.25, 62, true, false));
					
					ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
					ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 5));
																						// 36
					//ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 18, 0.25, 0, true));
					ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 1250, 0.35));
					
					ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
					
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, -36, 0.45, 0, true));
					
					ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.25, -45, true, false));
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
		
		switch (aData.startPos) {
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
		
		// boolean samePlate = (GameData[0] == lookingFor && GameData[1] == lookingFor);
		
		// Switch is ours only
		if (GameData[0] == lookingFor && GameData[1] != lookingFor) {
			if (aData.autonPriority == AutonPriority.scale) {
				if (!aData.scaleZigBaselineOnly) {
					BCFunc.addBC90Scale(isRight);
				}
				else {
					BCFunc.addBCCrossLine(isRight);
				}
			}
			else {
				BCFunc.addBCSwitch(isRight);
			}
		}
		// Scale is ours only
		else if (GameData[0] != lookingFor && GameData[1] == lookingFor) {
			BCFunc.addBCStraightScale(isRight, lookingFor);
		}
		// Neither is ours
		else if (GameData[0] != lookingFor && GameData[1] != lookingFor) {
			if (!aData.scaleZigBaselineOnly) {
				BCFunc.addBC90Scale(isRight);
			}
			else {
				BCFunc.addBCCrossLine(isRight);
			}
		}
		// Both are ours.
		else if (GameData[0] == lookingFor && GameData[1] == lookingFor) {
			if (aData.autonPriority == AutonPriority.sw1tch) {
				BCFunc.addBCSwitch(isRight);
			}
			else {
				BCFunc.addBCStraightScale(isRight, lookingFor);
			}
		}
	}
	
	private static class BCFunc {
		private static void addBCSwitch(boolean isRight) {			
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 109, 0.50, 0, false));
			
			ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
			ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 5));
			
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.30, isRight?-90:90, true, true));
			//ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 2, 0.30, 0, true, true));
			ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
		}
		
		private static void addBCStraightScale(boolean isRight, char lookingFor) {
			
			if (aData.scaleDoNull) {
				addBCStraightNullScale(isRight);
				return;
			}
			
			double longSpeed = 0.75;
			
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 70, 0.75, 0, false));
			
			Elevator_Position highPos = new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.high);
			ComList.addParallel(highPos);
			
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 143, 0.50, 0, true));
			
			//ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.up));
			
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, longSpeed, isRight?-45:45, true, false));
			
																				// 6
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 10, 0.25, 0, true));
			
			ComList.addSequential(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
			
			ComList.addSequential(new Command_PauseUntil(highPos));
			
			ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.shoot));
			
			ComList.addSequential(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.up));
			
			if (!aData.scaleDo2Cube) {
				ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 1000, -0.25));
			}
			
			ComList.addSequential(new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.floor));
			
			if (!aData.scaleDo2Cube) {return;}
			
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.5, isRight?-110:110, true, false));
			
			ComList.addSequential(new DriveStraight_Eye(RobotMap.DriveSubsystem, RobotMap.GrabberSubsystem, RobotMap.ElevSubsystem, 0.25, 0, 500));
			
			ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 350, -0.5));
			
			if ((GameData[0] != lookingFor) || (!aData.scaleDo2CubeSpit)) {return;}
			
			ComList.addSequential(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
			
			ComList.addSequential(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 5));
			
			ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 500, 0.5));
			
			ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
			//ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 1000, -0.25));
		}
		
		private static void addBCStraightNullScale(boolean isRight) {
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 140, 0.75, 0, false));
			
			Elevator_Position highPos = new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.high);
			ComList.addParallel(highPos);
			
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 137, 0.50, 0, true));
			
			//ComList.addSequential(new ShiftGear(RobotMap.DriveSubsystem, true));
			
			ComList.addSequential(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
			
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.25, isRight?-90:90, true, false));
			
			ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 750, 0.25));
			
			ComList.addSequential(new Command_PauseUntil(highPos));
			
			ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
			
			//ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
			
			ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 1000, -0.30));
			
			ComList.addSequential(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.up));
			
			ComList.addSequential(new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.floor));
		}
		
		private static void addBC90Scale(boolean isRight) {
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 203, 0.50, 0, true));
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.50, isRight?-90:90, true, false));
			
			Elevator_Position elevHigh = new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.high);
			ComList.addParallel(elevHigh);
																				// 179
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 215, 0.50, 0, true));
																						 // 90:-90
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.50, isRight?110:-110, true, false));
			ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
																				// 30
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 40, 0.25, 0, true));
			ComList.addSequential(new Command_PauseUntil(elevHigh));
			ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
			
			ComList.addSequential(new PercDrive_Time(RobotMap.DriveSubsystem, 1000, -0.25));
			
			ComList.addSequential(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.up));
			
			ComList.addSequential(new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.floor));
			
			//ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 46, 0.25, isRight?90:-90, true, true));
		}
		
		private static void addBCCrossLine(boolean isRight) {
			//ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 92, 0.50, 0, true));
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 203, 0.50, 0, true));
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.50, isRight?-90:90, true, false));
																				// 179
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 107, 0.50, 0, true));
		}
	}
}
