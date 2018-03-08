package org.torc.mainrobot.teleopcontrols;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.GetType;
import org.torc.mainrobot.program.ButtonMap.RCAxis;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.ControlledStateMachine;

public class DriveTrain_Teleop extends ControlledStateMachine {
	
	private int rampTime = 0;
	
	private final int rampWait = 500 / 20;
	
	@Override
	public void execute() {
		// ez halo drive. ;)
		RobotMap.DriveSubsystem.haloDrive(-RobotMap.driverControl.getAxis(RCAxis.leftY), RobotMap.driverControl.getAxis(RCAxis.rightX), true);
		
		// Toggle shifters high/low
		if (RobotMap.driverControl.getButton(RCButtons.toggleShifters, GetType.pressed)) {
			RobotMap.DriveSubsystem.setShifters(!RobotMap.DriveSubsystem.getShifters());
		}
		
		// Open hook
		if (RobotMap.driverControl.getButton(RCButtons.hookRelease, GetType.pressed)) {
			RobotMap.ClimbingHook.openHook();
		}
		
		// Open ramp
		if (RobotMap.driverControl.getButton(RCButtons.rampRelease, GetType.normal)) {
			rampTime++;
		}
		else {
			rampTime = 0;
		}
		
		if (rampTime >= rampWait) {
			rampTime = 0;
			RobotMap.ClimbingRamp.openRamp();
		}
	}
	
}
