package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.tools.CLCommand;

public class Elevator_Jog extends CLCommand {
	
	private final int posRange = Elevator.posPerInch;
	
	private Elevator elevSubsystem;
	
	private int posInc = 0;
	private int posToGo = 0;
	
	public Elevator_Jog(Elevator elevator, int posIncNum) {
		
		elevSubsystem = elevator;
		
		requires(elevSubsystem);
		
		posInc = posIncNum;
		
		posToGo = elevSubsystem.getEncoder() + posInc;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		elevSubsystem.jogElevatorPos(posInc);
	}
	
	@Override
	protected void execute() {
		int currentPos = elevSubsystem.getEncoder();
		if (currentPos > (posToGo - posRange) && currentPos < (posToGo + posRange)) {
			finishedCommand = true;
		}
	}
}
