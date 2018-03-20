package org.torc.mainrobot.robot.commands;

import org.torc.mainrobot.tools.CLCommand;

/**
 *
 */

public class Command_PauseUntil extends CLCommand {
	
	private CLCommand listenCommand;
	
	public Command_PauseUntil(CLCommand comm) {
		listenCommand = comm;
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (listenCommand == null || listenCommand.isFinished()) {
			finishedCommand = true;
		}
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}
}
