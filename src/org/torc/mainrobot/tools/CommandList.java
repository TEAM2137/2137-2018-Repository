package org.torc.mainrobot.tools;

import java.util.ArrayList;

import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.Robot;


/**
 * A class which allows for a list of commands and optional 
 * timeouts for them to be dynamically added to, and run from.
 * NOTE: Requires InheritedPeriodic to be implemented properly.
 */
public class CommandList implements InheritedPeriodic {
	
	private ArrayList<CLCommand> comList;
	
	private int iPos = 0;
	
	private boolean started = false;
	
	public CommandList() {
		Robot.AddToPeriodic(this);
	}
	
	public void start() {
		started = true;
		comList.get(iPos).start();
	}	
	
	public void addCommand(CLCommand command) {
		comList.add(command);
	}
	
	@Override
	public void Periodic() {
		// If list started
		if (started) {
			// if the current command is finished
			if (comList.get(iPos).isFinished()) {
				iPos++;
				if(iPos > comList.size() - 1) {
					iPos = 0;
					started = false;
					return;
				}
				comList.get(iPos).start();
			}
		}
	}
}