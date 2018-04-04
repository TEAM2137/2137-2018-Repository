package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.subsystems.DriveTrain;
import org.torc.mainrobot.robot.subsystems.DriveTrain.DTSide;
import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.Elevator.ElevatorPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberSpeeds;
import org.torc.mainrobot.robot.subsystems.UltraGrabber;
import org.torc.mainrobot.tools.CLCommand;
import org.torc.mainrobot.tools.MathExtra;

public class DriveStraight_Eye extends CLCommand {
	
	private DriveTrain driveSubsystem;
	
	private UltraGrabber grabberSubsystem;
	
	private Elevator elevSubsystem;
	
	private double mainSpeed = 0;
	private double angleTarget = 0;
	
	private double gyroBase = 0;
	
	private boolean goEndCount = false;
	private int endCount = 0;
	private int endCountAmt = 500/20;
	
	private int cubeTime = 0;
	private final int CubeWait = 0;
	
	// PID Stuff
	private double errSum = 0;
	private double dLastPos = 0;
	
	private final double pGain = 0.022;//0.045;
	private final double iGain = 0;
	private final double dGain = 0;//0.68;
	
	public DriveStraight_Eye(DriveTrain dTrain, UltraGrabber grabber, Elevator elev, double mSpeed, double angle) {
		driveSubsystem = dTrain;
		
		elevSubsystem = elev;
		
		grabberSubsystem = grabber;
		
		requires(driveSubsystem);
		
		mainSpeed = mSpeed;
		angleTarget = angle;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		
		gyroBase = driveSubsystem.getGyroHeader();
		
		dLastPos = gyroBase;
		// Shift to high gear
		driveSubsystem.setShifters(false);
		// Start pickup angle
		grabberSubsystem.setCubeGrip(false);
		grabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.pickup);
		grabberSubsystem.findGrabberPosition(GrabberPositions.pickup);
		
		elevSubsystem.positionFind(ElevatorPositions.floor);
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		
		// Endcount for wait after
		if (goEndCount) {
			endCount++;
			if (endCount >= endCountAmt) {
				finishedCommand = true;
			}
			return;
		}
		
		double leftSpeed = 0;
		double rightSpeed = 0;
		
		double gyroVal = driveSubsystem.getGyroHeader();
		
		leftSpeed = mainSpeed;
		rightSpeed = mainSpeed;
		
		double err = MathExtra.clamp((gyroVal - gyroBase) - angleTarget, -20, 20);
		
		// Add to error sum for Integral
		errSum += err;
		
		double offset = (pGain * err) + (errSum * iGain) + (dGain * (gyroVal - dLastPos));
		rightSpeed += offset;
		leftSpeed -= offset;
		
		dLastPos = gyroVal;
		
		//driveSubsystem.setVelocity(leftSpeed, rightSpeed);
		driveSubsystem.setPercVBus(leftSpeed, rightSpeed);
		
		// Get photoeye
		if (grabberSubsystem.getCubeEye()) {
			cubeTime++;
			if (cubeTime >= CubeWait) {
				cubeTime = 0;
				
				driveSubsystem.setVelocity(0, 0);
				grabberSubsystem.setCubeGrip(true);
				grabberSubsystem.findGrabberPosition(GrabberPositions.up);
				grabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.cubeKeep);
				
				finishedCommand = true;
			}
		}
		else {
			cubeTime = 0;
		}
		
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("Finished DriveStraight_Eye!");
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
