package org.torc.mainrobot.robot.commands;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.RCAxis;
import org.torc.mainrobot.robot.subsystems.Elevator;
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
					RobotMap.driverControl.setDualRumbleTime(0.5, 0.5);
					RobotMap.operatorControl.setDualRumbleTime(0.5, 0.5);
					state = PickupStates.raiseGrabber;
				}
				break;
			case raiseGrabber:
				double jogVal = RobotMap.operatorControl.getAxis(RCAxis.grabberJog);
				/* If operator is holding jog down while command completes, 
				 * don't auto position the grabber up
				 */
				if (!(jogVal > 0.2)) {
					RobotMap.GrabberSubsystem.findGrabberPosition(GrabberPositions.up);
					RobotMap.ElevSubsystem.jogElevatorPos(Elevator.posPerInch * 3);
				}
				RobotMap.GrabberSubsystem.setGrabberIntakeSpeed(GrabberSpeeds.cubeKeep);
				isFinished = true;
				break;
		}
		
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		return isFinished;
	}
}