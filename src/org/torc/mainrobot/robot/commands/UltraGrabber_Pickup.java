package org.torc.mainrobot.robot.commands;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.Robot;
import org.torc.mainrobot.robot.subsystems.Elevator.ElevatorPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberPositions;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberSpeeds;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class UltraGrabber_Pickup extends Command {
	
	public enum PickupStates { lowering, waitingForCube, raiseGrabber }
	
	public PickupStates state = PickupStates.lowering;
	
	private boolean isFinished = false;
	
	private int endStopCount = 0;
	
	private final int endStopWait = 800 / 20;
	
	public UltraGrabber_Pickup() {
		// Use requires() here to declare subsystem dependencies
		requires(RobotMap.GrabberSubsystem);
		requires(RobotMap.ElevSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		switch (state) {
			case lowering:
				RobotMap.GrabberSubsystem.findGrabberPosition(GrabberPositions.pickup);
				RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.pickup);
				RobotMap.ElevSubsystem.positionFind(ElevatorPositions.floor);
				state = PickupStates.waitingForCube;
				break;
			case waitingForCube:
				if (RobotMap.GrabberSubsystem.getCubeEye()) {
					endStopCount++;
				}
				else {
					endStopCount = 0;
				}
				
				SmartDashboard.putNumber("PickupEndstopCount", endStopCount);
				
				if (endStopCount >= endStopWait) {
					RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.cubeKeep);
					state = PickupStates.raiseGrabber;
				}
				break;
			case raiseGrabber:
				//RobotMap.ElevSubsystem.positionFind(ElevatorPositions.middle);
				RobotMap.GrabberSubsystem.findGrabberPosition(GrabberPositions.up);
				isFinished = true;
				break;
		}
		
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		return isFinished;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
