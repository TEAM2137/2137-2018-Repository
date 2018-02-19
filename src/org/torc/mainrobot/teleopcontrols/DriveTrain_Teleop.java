package org.torc.mainrobot.teleopcontrols;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.GetType;
import org.torc.mainrobot.program.ButtonMap.RCAxis;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.ControlledStateMachine;

public class DriveTrain_Teleop extends ControlledStateMachine {
	
	@Override
	public void execute() {
		// ez halo drive. ;)
		RobotMap.DriveSubsystem.haloDrive(-RobotMap.driverControl.getAxis(RCAxis.leftY), -RobotMap.driverControl.getAxis(RCAxis.rightX));
		
		// Toggle shifters high/low
		if (RobotMap.driverControl.getButton(RCButtons.toggleShifters, GetType.pressed)) {
			RobotMap.DriveSubsystem.setShifters(!RobotMap.DriveSubsystem.getShifters());
		}
		
	}
	
}
