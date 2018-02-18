package org.torc.mainrobot.robot.commands;

import org.torc.mainrobot.robot.subsystems.Elevator;
import org.torc.mainrobot.robot.subsystems.UltraGrabber;

import edu.wpi.first.wpilibj.command.Command;

public class UltraGrabber_Home extends Command {
	
	enum HomingStates { firstMoveDown, secondMoveUp }
	
	/**
	 * The calling Subsystem of the command.
	 */
	UltraGrabber UGSubsystem;
	
	HomingStates homingState = HomingStates.firstMoveDown;
	
	boolean doneRunning = false;
	
	double firstMoveDownPerc = 0.5;
	double secondMoveUpPerc = 0.1;
	
	public UltraGrabber_Home(UltraGrabber grabber) {
		// Use requires() here to declare subsystem dependencies
		UGSubsystem = grabber;
		requires(UGSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		System.out.println("UltraGrabber_Home Init");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		
		switch (homingState) {
			case firstMoveDown:
				UGSubsystem.jogGrabberPerc(-firstMoveDownPerc);
				if (UGSubsystem.getEndstop()) {
					System.out.println("firstMoveDown Done!");
					homingState = HomingStates.secondMoveUp;
				}
				break;
			case secondMoveUp:
				UGSubsystem.jogGrabberPerc(secondMoveUpPerc);
				if (!UGSubsystem.getEndstop()) {
					System.out.println("secondMoveUp Done!");
					UGSubsystem.zeroEncoder();
					UGSubsystem.jogGrabberPerc(0);
					doneRunning = true;
				}
				break;
		}
		
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	public boolean isFinished() {
		return doneRunning;
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
