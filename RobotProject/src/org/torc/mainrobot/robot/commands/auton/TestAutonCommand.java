package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.tools.CLCommand;

public class TestAutonCommand extends CLCommand {
	
	private int waitTime = 0;
	
	private int updateCount = 0;
	
	String messageToPrint = "";
	
	public TestAutonCommand(String message, int waitTimeMs) {
		messageToPrint = message;
		
		waitTime = waitTimeMs / 20;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		System.out.println(messageToPrint);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		updateCount++;
		if (updateCount >= waitTime) {
			this.finishedCommand = true;
		}
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
