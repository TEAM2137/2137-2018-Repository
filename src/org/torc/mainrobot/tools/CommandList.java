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
	
	private ArrayList<CommandListEntry> comList = new ArrayList<CommandListEntry>();
	
	private int iPos = 0;
	
	private boolean started = false;
	
	public CommandList() {
		Robot.AddToPeriodic(this);
	}
	
	public void start() {
		started = true;
		comList.get(iPos).command.start();
	}	
	
	public void addCommand(CLCommand command, boolean sequential) {
		comList.add(new CommandListEntry(command, sequential));
	}
	
	@Override
	public void Periodic() {
		// If list started
		if (started) {
			while ( !(iPos > comList.size() - 1) && !comList.get(iPos).isSequential ) {
				comList.get(iPos).command.start();
				iPos++;
			}
			if(iPos > comList.size() - 1) {
				iPos = 0;
				started = false;
				System.out.println("Stopped Commandlist!");
				return;
			}
			// if the current command is finished
			if (comList.get(iPos).command.isFinished()) {
				iPos++;
				if(iPos > comList.size() - 1) {
					iPos = 0;
					started = false;
					System.out.println("Stopped Commandlist!");
					return;
				}
				comList.get(iPos).command.start();
			}
		}
	}
}

class CommandListEntry {
	CLCommand command;
	boolean isSequential;
	
	CommandListEntry(CLCommand comm, boolean sequential) {
		command = comm;
		isSequential = sequential;
	}
	
}