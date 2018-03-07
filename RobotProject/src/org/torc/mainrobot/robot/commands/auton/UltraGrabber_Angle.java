package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.robot.subsystems.UltraGrabber;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberPositions;
import org.torc.mainrobot.tools.CLCommand;

public class UltraGrabber_Angle extends CLCommand {
	
	private final double searchPosRange = 10 * UltraGrabber.angleMult;
	
	private UltraGrabber grabberSubsys;
	
	private GrabberPositions grabberPos;
	private double grabberPosNum;
	
	public UltraGrabber_Angle(UltraGrabber grabber, GrabberPositions position) {
		grabberSubsys = grabber;
		
		grabberPos = position;
		grabberPosNum = UltraGrabber.GetGrabberPositions(grabberPos);
		
		requires(grabberSubsys);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		grabberSubsys.findGrabberPosition(grabberPos);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		int encoderVal = grabberSubsys.getEncoder();
		if (encoderVal > (grabberPosNum - searchPosRange) && encoderVal < (grabberPosNum + searchPosRange)) {
			finishedCommand = true;
		}
	}
	
	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("DoneAngle!!");
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
