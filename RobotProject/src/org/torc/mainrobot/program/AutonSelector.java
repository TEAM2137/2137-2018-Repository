package org.torc.mainrobot.program;

import org.torc.mainrobot.program.ButtonMap.GetType;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.Robot;
import org.torc.mainrobot.tools.CommandList;
import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonSelector implements InheritedPeriodic {
	
	private final int buttonWait = 500 / 20;
	
	public enum StartPositions { left, center, right }
	
	public enum AutonPriority { sw1tch, scale, baselineOnly } 
	
	private StartPositions startPos = StartPositions.center;
	
	private AutonPriority autonPriority = AutonPriority.sw1tch;
	
	private CommandList autonList;
	
	public AutonSelector() {
		Robot.AddToPeriodic(this);
	}
	
	public void getAuton() {
		autonList = new CommandList();
		AutonDatabase.GetAuton(autonList, startPos, autonPriority);
	}
	
	public void startAuton() {
		if (autonList == null || autonList.getListLength() == 0) {
			System.out.println("Cannot start AutonSelector list!!");
			return;
		}
		autonList.start();
	}

	private int buttonTime = 0;
	
	@Override
	public void Periodic() {
		
		boolean selectLeft = (RobotMap.driverControl.getButton(RCButtons.autonSelectLeft, GetType.normal) || 
							RobotMap.operatorControl.getButton(RCButtons.autonSelectLeft, GetType.normal));
		boolean selectRight = (RobotMap.driverControl.getButton(RCButtons.autonSelectRight, GetType.normal) || 
							RobotMap.operatorControl.getButton(RCButtons.autonSelectRight, GetType.normal));
		boolean selectUp = (RobotMap.driverControl.getButton(RCButtons.autonPriorityUp, GetType.normal) || 
							RobotMap.operatorControl.getButton(RCButtons.autonPriorityUp, GetType.normal));
		boolean selectDown = (RobotMap.driverControl.getButton(RCButtons.autonPriorityDown, GetType.normal) || 
							RobotMap.operatorControl.getButton(RCButtons.autonPriorityDown, GetType.normal));
		
		
		if (selectLeft || selectRight || selectUp || selectDown) {
			buttonTime++;
		}
		else {
			buttonTime = 0;
		}
		
		boolean selectMove = false;
		
		if (buttonTime >= buttonWait) {
			//buttonTime
			selectMove = true;
			buttonTime = 0;
		}
		
		if (selectMove) {
			if (selectLeft) {
				startPos = StartPositions.values()[MathExtra.clamp(startPos.ordinal() - 1, 0, StartPositions.values().length - 1)];
			}
			else if (selectRight) {
				startPos = StartPositions.values()[MathExtra.clamp(startPos.ordinal() + 1, 0, StartPositions.values().length - 1)];
			}
			else if (selectUp) {
				autonPriority = AutonPriority.values()[MathExtra.clamp(startPos.ordinal() + 1, 0, AutonPriority.values().length - 1)];
			}
			else if (selectDown) {
				autonPriority = AutonPriority.values()[MathExtra.clamp(startPos.ordinal() - 1, 0, AutonPriority.values().length - 1)];
			}
		}
		
		SmartDashboard.putBoolean("AutonSelectLeft", (startPos == StartPositions.left));
		SmartDashboard.putBoolean("AutonSelectCenter", (startPos == StartPositions.center));
		SmartDashboard.putBoolean("AutonSelectRight", (startPos == StartPositions.right));
		
		SmartDashboard.putBoolean("SwitchPriority", (autonPriority == AutonPriority.sw1tch));
		SmartDashboard.putBoolean("ScalePriority", (autonPriority == AutonPriority.scale));
		SmartDashboard.putBoolean("BaselineOnly", (autonPriority == AutonPriority.baselineOnly));
	}
	
}
