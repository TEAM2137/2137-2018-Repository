package org.torc.mainrobot.robot.commands.elevator;

import org.torc.mainrobot.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Elevator_Init extends CommandGroup {
	public Elevator_Init(Elevator elevator) {
		addSequential(new Elevator_Home(elevator));
		addSequential(new Elevator_Control(elevator));
	}
}
