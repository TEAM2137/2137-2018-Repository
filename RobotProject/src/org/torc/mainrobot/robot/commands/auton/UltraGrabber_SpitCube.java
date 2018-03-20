package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.robot.subsystems.UltraGrabber;
import org.torc.mainrobot.robot.subsystems.UltraGrabber.GrabberSpeeds;
import org.torc.mainrobot.tools.CLCommand;

public class UltraGrabber_SpitCube extends CLCommand {
	
	public enum SpitSpeeds { drop, shoot }
	
	private SpitSpeeds spitSpeed;
	
	private UltraGrabber grabberSubsys;
	
	private int eyeTime = 0;
	
	private final int eyeTimeWait = 1000 / 20;
	
	public UltraGrabber_SpitCube(UltraGrabber grabber, SpitSpeeds speed) {
		grabberSubsys = grabber;
		
		spitSpeed = speed;
		
		requires(grabberSubsys);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		grabberSubsys.setCubeGrip(false);
		grabberSubsys.setGrabberIntakeSpeed((spitSpeed == SpitSpeeds.drop) ? GrabberSpeeds.dropping : GrabberSpeeds.shooting);
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (!grabberSubsys.getCubeEye()) {
			eyeTime++;
		}
		if (eyeTime >= eyeTimeWait) {
			grabberSubsys.setGrabberIntakeSpeed(GrabberSpeeds.none);
			finishedCommand = true;
		}
	}
	
}
