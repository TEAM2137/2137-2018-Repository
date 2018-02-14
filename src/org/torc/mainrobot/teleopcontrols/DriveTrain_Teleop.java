package org.torc.mainrobot.teleopcontrols;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.RCAxis;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.ControlledStateMachine;

public class DriveTrain_Teleop extends ControlledStateMachine {
	
	@Override
	public void execute() {
		// ez arcade drive. ;)
		RobotMap.DriveSubsystem.arcadeDrive(-RobotMap.driverControl.getAxis(RCAxis.leftY), -RobotMap.driverControl.getAxis(RCAxis.rightX), true);
		
		// Toggle shifters high/low
		if (RobotMap.driverControl.getButton(RCButtons.toggleShifters, true)) {
			RobotMap.DriveSubsystem.setShifters(!RobotMap.DriveSubsystem.getShifters());
		}
		
	}
	
}
