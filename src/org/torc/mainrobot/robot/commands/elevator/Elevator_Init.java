package org.torc.mainrobot.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Elevator_Init extends CommandGroup {
	public Elevator_Init() {
		addSequential(new Elevator_Home());
		addSequential(new Elevator_Control());
	}
}
