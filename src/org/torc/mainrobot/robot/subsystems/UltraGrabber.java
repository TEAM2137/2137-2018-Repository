package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * 	ULTRA GRABBER
 *  (A.K.A, the cube grabber)
 */
public class UltraGrabber extends Subsystem implements InheritedPeriodic {
	
	private VictorSP leftMotor;
	private VictorSP rightMotor;
	
	private TalonSRX angleMotor;
	
	private DigitalInput endstop;

	UltraGrabber(int leftVictorPort, int rightVictorPort, int angleTalonPort, int endstopPort) {
		leftMotor = new VictorSP(leftVictorPort);
		rightMotor = new VictorSP(rightVictorPort);

		angleMotor = new TalonSRX(angleTalonPort);
		
		MotorControllers.TalonSRXConfig(angleMotor, 10, 0, 0);
		
		endstop = new DigitalInput(endstopPort);
		
	}
	
	/**
	 * Jog the grabber talon motor by using PercentVBus.
	 * @param value The value to jog the grabber motor by (-1 to 1).
	 */
	public void jogGrabberPerc(double value) {
		angleMotor.set(ControlMode.PercentOutput, value);
	}
	
	public boolean getEndstop() {
		return endstop.get();
	}
	
	public void zeroEncoder() {
		angleMotor.setSelectedSensorPosition(0, 0, 10);
	}
	
	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public void Periodic() {
		
	}
}
