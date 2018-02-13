package org.torc.mainrobot.teleopcontrols;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.program.ButtonMap.RCAxis;
import org.torc.mainrobot.robot.ControlledStateMachine;

public class DriveTrain_Teleop extends ControlledStateMachine {
	
	@Override
	public void execute() {
		// ez arcade drive. ;)
		RobotMap.DriveSubsystem.arcadeDrive(RobotMap.mainController.getAxis(RCAxis.leftY), RobotMap.mainController.getAxis(RCAxis.rightX), true);
	}
	
}
