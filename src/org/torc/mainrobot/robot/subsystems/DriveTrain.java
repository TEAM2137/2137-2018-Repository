package org.torc.mainrobot.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveTrain extends Subsystem {

	DifferentialDrive roboDrive;
	
	TalonSRX rightMaster, rightSlave, leftMaster, leftSlave;

	PigeonIMU drivePigeon;

	public DriveTrain(int rightMasterPort, int rightSlavePort, int leftMasterPort, int leftSlavePort) {
		rightMaster = new TalonSRX(rightMasterPort);
		rightSlave = new TalonSRX(rightSlavePort);
		leftMaster = new TalonSRX(leftMasterPort);
		leftSlave = new TalonSRX(leftSlavePort);		
		// Set slaves as followers
		rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());
		leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
		
		drivePigeon = new PigeonIMU(4);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
	
}
