package org.torc.mainrobot.program;

import org.torc.mainrobot.program.ButtonMap.GetType;
import org.torc.mainrobot.program.ButtonMap.RCButtons;
import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.Robot;
import org.torc.mainrobot.tools.CommandList;
import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonSelector {
	
	private final int buttonWait = 500 / 20;
	
	public enum StartPositions { left, center, right }
	
	public enum AutonPriority { sw1tch, scale } 
	
	private AutonData aData = new AutonData();
	
	private CommandList autonList;
	
	public class AutonData {
		public StartPositions startPos = StartPositions.center;
		public AutonPriority autonPriority = AutonPriority.sw1tch;
		public boolean scaleZigBaselineOnly = false;
		public boolean scaleDo2Cube = true;
		public boolean scaleDoNull = false;
		public boolean scaleDo2CubeSpit = true;
	}
	
	/**
	 * @return If the auton get was successful or not.
	 */
	public boolean getAuton() {
		autonList = new CommandList();
		return AutonDatabase.GetAuton(autonList, aData);
	}
	
	public void startAuton() {
		if (autonList == null || autonList.getListLength() == 0) {
			System.out.println("Cannot start AutonSelector list!!");
			return;
		}
		
		RobotMap.DriveSubsystem.setShifters(false);
		
		autonList.start();
	}

	private int buttonTime = 0;
	
	public void Update() {
		
		//System.out.println("scaleZigBaselineOnly val: " + scaleZigBaseline);
		
		boolean selectLeft = (RobotMap.driverControl.getButton(RCButtons.autonSelectLeft, GetType.normal) || 
							RobotMap.operatorControl.getButton(RCButtons.autonSelectLeft, GetType.normal));
		boolean selectRight = (RobotMap.driverControl.getButton(RCButtons.autonSelectRight, GetType.normal) || 
							RobotMap.operatorControl.getButton(RCButtons.autonSelectRight, GetType.normal));
		boolean selectUp = (RobotMap.driverControl.getButton(RCButtons.autonPriorityUp, GetType.normal) || 
							RobotMap.operatorControl.getButton(RCButtons.autonPriorityUp, GetType.normal));
		boolean selectDown = (RobotMap.driverControl.getButton(RCButtons.autonPriorityDown, GetType.normal) || 
							RobotMap.operatorControl.getButton(RCButtons.autonPriorityDown, GetType.normal));
		boolean selectBaseline = (RobotMap.driverControl.getButton(RCButtons.autonZigBaseToggle, GetType.pressed) || 
								RobotMap.operatorControl.getButton(RCButtons.autonZigBaseToggle, GetType.pressed));
		boolean select2CScale = (RobotMap.driverControl.getButton(RCButtons.auton2CScaleToggle, GetType.pressed) || 
								RobotMap.operatorControl.getButton(RCButtons.auton2CScaleToggle, GetType.pressed));
		boolean select2CScaleSpit = (RobotMap.driverControl.getButton(RCButtons.auton2CScaleSpitToggle, GetType.pressed) || 
									RobotMap.operatorControl.getButton(RCButtons.auton2CScaleSpitToggle, GetType.pressed));
		boolean selectNullScale = (RobotMap.driverControl.getButton(RCButtons.autonNullScaleToggle, GetType.pressed) || 
									RobotMap.operatorControl.getButton(RCButtons.autonNullScaleToggle, GetType.pressed));
		
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
				aData.startPos = StartPositions.values()[MathExtra.clamp(aData.startPos.ordinal() - 1, 0, StartPositions.values().length - 1)];
			}
			else if (selectRight) {
				aData.startPos = StartPositions.values()[MathExtra.clamp(aData.startPos.ordinal() + 1, 0, StartPositions.values().length - 1)];
			}
			else if (selectUp) {
				aData.autonPriority = AutonPriority.values()[MathExtra.clamp(aData.startPos.ordinal() + 1, 0, AutonPriority.values().length - 1)];
			}
			else if (selectDown) {
				aData.autonPriority = AutonPriority.values()[MathExtra.clamp(aData.startPos.ordinal() - 1, 0, AutonPriority.values().length - 1)];
			}
			/*
			else if (selectBaseline) {
				System.out.println("selectBaseline invert!");
				scaleZigBaselineOnly = !scaleZigBaseline;
			}
			*/
		}
		
		// Select baseline
		if (selectBaseline) {
			aData.scaleZigBaselineOnly = !aData.scaleZigBaselineOnly;
		}
		// Select 2C enable
		if (select2CScale) {
			aData.scaleDo2Cube = !aData.scaleDo2Cube;
		}
		if (select2CScaleSpit) {
			aData.scaleDo2CubeSpit = !aData.scaleDo2CubeSpit;
		}
		if (selectNullScale) {
			aData.scaleDoNull = !aData.scaleDoNull;
		}
		
		SmartDashboard.putBoolean("AutonSelectLeft", (aData.startPos == StartPositions.left));
		SmartDashboard.putBoolean("AutonSelectCenter", (aData.startPos == StartPositions.center));
		SmartDashboard.putBoolean("AutonSelectRight", (aData.startPos == StartPositions.right));
		
		SmartDashboard.putBoolean("SwitchPriority", (aData.autonPriority == AutonPriority.sw1tch));
		SmartDashboard.putBoolean("ScalePriority", (aData.autonPriority == AutonPriority.scale));
		
		SmartDashboard.putBoolean("ScaleZigBaseline", aData.scaleZigBaselineOnly);
		SmartDashboard.putBoolean("ScaleDo2Cube", aData.scaleDo2Cube);
		SmartDashboard.putBoolean("ScaleDo2CubeSpit", aData.scaleDo2CubeSpit);
		SmartDashboard.putBoolean("ScaleDoNull", aData.scaleDoNull);
	}
	
}
