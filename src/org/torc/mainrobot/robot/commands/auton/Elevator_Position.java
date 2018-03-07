package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.Elevator.ElevatorPositions;
import org.torc.mainrobot.tools.CLCommand;

public class Elevator_Position extends CLCommand {
	
	private final int posRange = Elevator.posPerInch;
	
	private Elevator elevSubsystem;
	
	private ElevatorPositions position;
	
	private int posToGo = 0;
	
	public Elevator_Position(Elevator elevator, ElevatorPositions pos) {
		
		elevSubsystem = elevator;
		
		requires(elevSubsystem);
		
		position = pos;
		
		posToGo = Elevator.GetElevatorPositions(position);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		elevSubsystem.positionFind(position);
	}
	
	@Override
	protected void execute() {
		int currentPos = elevSubsystem.getEncoder();
		if (currentPos > (posToGo - posRange) && currentPos < (posToGo + posRange)) {
			finishedCommand = true;
		}
	}
}
