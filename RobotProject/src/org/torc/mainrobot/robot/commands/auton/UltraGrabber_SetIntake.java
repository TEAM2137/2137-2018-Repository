package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.robot.subsystems.UltraGrabber;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberSpeeds;
import org.torc.mainrobot.tools.CLCommand;

public class UltraGrabber_SetIntake extends CLCommand {
	
	private UltraGrabber grabberSubsys;
	
	private GrabberSpeeds grabberSpeed;
	
	public UltraGrabber_SetIntake(UltraGrabber grabber, GrabberSpeeds speed) {
		grabberSubsys = grabber;
		
		grabberSpeed = speed;
		
		requires(grabberSubsys);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		if (grabberSpeed != GrabberSpeeds.cubeKeep) {
			grabberSubsys.setCubeGrip(false);
		}
		grabberSubsys.setGrabberIntakeSpeed(grabberSpeed);
		finishedCommand = true;
	}
	
}
