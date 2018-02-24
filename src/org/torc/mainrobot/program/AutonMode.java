package org.torc.mainrobot.program;

import org.torc.mainrobot.robot.commands.auton.DriveStraight_Angle;
import org.torc.mainrobot.robot.commands.auton.Elevator_Jog;
import org.torc.mainrobot.robot.commands.auton.TestAutonCommand;
import org.torc.mainrobot.robot.commands.auton.UltraGrabber_Angle;
import org.torc.mainrobot.robot.commands.auton.UltraGrabber_SetIntake;
import org.torc.mainrobot.robot.commands.auton.UltraGrabber_SpitCube;
import org.torc.mainrobot.robot.commands.auton.UltraGrabber_SpitCube.SpitSpeeds;
import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberSpeeds;
import org.torc.mainrobot.tools.CommandList;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonMode {
	
	public static DigitalInput testInp = new DigitalInput(9);
	
	public static void Init() {
		//RobotMap.DriveSubsystem.zeroGyro();
		RobotMap.GrabberSubsystem.homeGrabber();
		RobotMap.ElevSubsystem.homeElevator();
		
		RobotMap.AutonSelect.startAuton();
		
		CommandList testCom = new CommandList();
		testCom.addSequential(new TestAutonCommand("Going to start driving in a few seconds!!"));
		
		testCom.addParallel(new UltraGrabber_SetIntake(RobotMap.GrabberSubsystem, GrabberSpeeds.cubeKeep));
		testCom.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 7, 0.25, 0, true, false));
		testCom.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 122, 0.25, -62, true, false));
		testCom.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
		testCom.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 2));
		testCom.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 30, 0.25, 62, true, true));
		testCom.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
		/*
		testCom.addParallel(new UltraGrabber_SetIntake(RobotMap.GrabberSubsystem, GrabberSpeeds.cubeKeep));
		testCom.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 50, 0.25, 0, true, false));
		testCom.addParallel(new UltraGrabber_Angle(RobotMap.GrabberSubsystem, GrabberPositions.shooting));
		testCom.addParallel(new Elevator_Jog(RobotMap.ElevSubsystem, Elevator.posPerInch * 2));
		testCom.addSequential(new DriveStraight_Angle(RobotMap.DriveSubsystem, 42, 0.25, 0, true, true));
		testCom.addSequential(new UltraGrabber_SpitCube(RobotMap.GrabberSubsystem, SpitSpeeds.drop));
		*/
		testCom.start();
	}
	
	public static void Periodic() {
		
		SmartDashboard.putBoolean("InputEye", testInp.get());
		
		SmartDashboard.putNumber("RightEnc", RobotMap.DriveSubsystem.getEncoder(DTSide.right));
		SmartDashboard.putNumber("LeftEnc", RobotMap.DriveSubsystem.getEncoder(DTSide.left));
		
		SmartDashboard.putNumber("FusionAngle", RobotMap.DriveSubsystem.getGyroHeader());

	}
}