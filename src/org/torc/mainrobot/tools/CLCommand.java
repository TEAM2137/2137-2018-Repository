package org.torc.mainrobot.tools;

import edu.wpi.first.wpilibj.command.Command;

public class CLCommand extends Command {
	
	/**
	 * Used to tell the command that is running if it is finshed.
	 * This is used instead of personally implementing the orginal isFinished()
	 * method.
	 */
	protected boolean finishedCommand = false;
	
	@Override
	public final boolean isFinished() {
		// TODO Auto-generated method stub
		return finishedCommand;
	}
	
	
}
