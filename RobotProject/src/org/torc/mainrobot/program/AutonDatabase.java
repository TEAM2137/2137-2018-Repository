package org.torc.mainrobot.program;

import org.torc.mainrobot.program.AutonSelector.AutonPriority;
import org.torc.mainrobot.program.AutonSelector.StartPositions;
import org.torc.mainrobot.robot.commands.auton.UltraGrabber_SpitCube.SpitSpeeds;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.Elevator.ElevatorPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.*;
import org.torc.mainrobot.tools.CommandList;
import org.torc.mainrobot.robot.commands.auton.*;
import org.torc.mainrobot.robot.commands.*;

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
		
		/*
		ComList.addSequential(new TestAutonCommand("Waitingcom..."));
		Elevator_Position eleJog = new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.middle);
		ComList.addParallel(eleJog);
		ComList.addSequential(new Command_PauseUntil(eleJog));
		ComList.addSequential(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.pickup));
		*/
		
		//Low gear
		
		RobotMap.DriveSubsystem.setShifters(true);
		
		//ComList.addSequential(new TestAutonCommand("waiting"));
		
		//ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 12, 0.25, 0, true, true));
		
		autonGetStart();
		
		/*
		ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 100, 0.5, 0, true, false));
		ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.5, -90, true, true));
		ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 50, 0.5, 0, true, true));
		*/
	}
	
	
	private static void autonGetStart() {
		System.out.println("GameData: " + GameData[0] + GameData[1] + GameData[2]);
		
		//ComList.addSequential(new TestAutonCommand("Starting auton in a couple of seconds!"));
		
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
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 50, 0.55, 0, true, false));
					ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
					ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 2));
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 42, 0.55, 0, true, true));
					ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
				}
				// Left Plate
				// Zig-Zag to the plate across and deposit
				else {
					
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 7, 0.55, 0, true, false));
					ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.55, -62, true, true));
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 102, 0.55, 0, true, false));
					ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.55, 62, true, true));
					
					ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
					ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 3));
																						// 36
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 12, 0.55, 0, true, true));
					ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
					
					/*
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 7, 0.25, 0, true, false));
																									//-62
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 122, 0.35, -70, true, false));
					ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
					ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 3));
																						// 42
					ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 48, 0.25, 70, true, true));
					//ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 20, 30, true));
					
					ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
					*/
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
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 145, 0.65, 0, true, false));
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.65, isRight?-90:90, true, true));
			
			ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
			ComList.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 3));
			
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 17, 0.55, 0, true, true));
			ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
		}
		// scale goto
		else if ((GameData[0] != lookingFor && GameData[1] == lookingFor) || (samePlate && AutonPri == AutonPriority.scale)) {
			
			addCrossLine();
			
			/*
			
			//ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 260, 0.25, 0, true, true));
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 216, 0.75, 0, true, true));
			
			//ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.up));
			
			Elevator_Position highPos = new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.high);
			ComList.addParallel(highPos);
			
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 41.94, 0.55, isRight?-45:45, true, true));
			ComList.addSequential(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
			
			ComList.addSequential(new Command_PauseUntil(highPos));
			
			ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
			
			ComList.addSequential(new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.floor));
			
			*/
		}
		// 90-across code
		else if (GameData[0] != lookingFor && GameData[1] != lookingFor) {
			
			addCrossLine();
			
			/*

			// 90-across auton
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 196, 0.75, 0, true, false));
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.75, isRight?-90:90, true, true));
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 186, 0.65, 0, true, false));
			ComList.addSequential(new Position_Angle(RobotMap.DriveSubsystem, 0.65, isRight?90:-90, true, true));
			
			Elevator_Position elevHigh = new Elevator_Position(RobotMap.ElevSubsystem, ElevatorPositions.high);
			ComList.addParallel(elevHigh);
			
			ComList.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
			ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 43, 0.45, 0, true, true));
			ComList.addSequential(new Command_PauseUntil(elevHigh));
			ComList.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.shoot));
			
			//ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 46, 0.25, isRight?90:-90, true, true));
			
			*/
		}
		
		
	}
	
	private static void addCrossLine() {
		ComList.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 92, 0.55, 0, true, true));
	}
}
