package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.commands.UltraGrabber_Home;
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
	
	private DigitalInput endstop, cubeEye;
	
	private  UltraGrabber_Home grabberHomer;
	
	private boolean hasBeenHomed = false;
	// TODO: change temportary positions to final
	public enum GrabberPositions { up, flat, pickup, shooting }
	// TODO: change temportary speeds to final
	public enum GrabberSpeeds { none, pickup, dropping, shooting }

	public UltraGrabber(int leftVictorPort, int rightVictorPort, int angleTalonPort, int endstopPort, int cubePhotoeyePort) {
		// Add to periodic list
		org.torc.mainrobot.robot.Robot.AddToPeriodic(this);
		
		leftMotor = new VictorSP(leftVictorPort);
		rightMotor = new VictorSP(rightVictorPort);

		angleMotor = new TalonSRX(angleTalonPort);
		
		MotorControllers.TalonSRXConfig(angleMotor, 10, 0, 0, 0, 0.01, 0, 0);
		
		angleMotor.configPeakOutputForward(0.75, 10);
		angleMotor.configPeakOutputReverse(-0.75, 10);
		
		endstop = new DigitalInput(endstopPort);
		
		cubeEye = new DigitalInput(cubePhotoeyePort);
	}
	
	/**
	 * @param position
	 * @return The position of the grabber state in encoder ticks.
	 */
	private static int GetGrabberPositions(GrabberPositions position) {
		int toReturn = 0;
		switch(position) {
			case up:
				toReturn = -20;
				break;
			case pickup:
				toReturn = 75;
				break;
			case flat:
				toReturn = 60;
				break;
			case shooting:
				toReturn = 45;
				break;
		}
		// Convert degrees from lookup to encoder ticks
		return (toReturn * 4521);
	}
	
	private static double GetGrabberSpeeds(GrabberSpeeds speed) {
		double toReturn = 0;
		switch(speed) {
			case none:
				toReturn = 0;
				break;
			case pickup:
				toReturn = -0.5;
				break;
			case dropping:
				toReturn = 0.2;
				break;
			case shooting:
				toReturn = 1;
				break;
		}
		return toReturn;
	}
	
	public void findGrabberPosition(GrabberPositions position) {
		if (!hasBeenHomed) {
			hasNotHomedAlert();
			return;
		}
		System.out.println("Grabber finding position: " + position.name());
		angleMotor.set(ControlMode.Position, GetGrabberPositions(position));
	}
	
	public void setGrabberIntakeSpeed (GrabberSpeeds speed) {
		rightMotor.set(GetGrabberSpeeds(speed));
		leftMotor.set(-GetGrabberSpeeds(speed));
	}
	
	/**
	 * Jog the grabber talon motor by using PercentVBus.
	 * @param value The value to jog the grabber motor by (-1 to 1).
	 */
	public void jogGrabberPerc(double value) {
		angleMotor.set(ControlMode.PercentOutput, value);
	}
	
	public void homeGrabber() {
		if (hasBeenHomed) {
			deHome();
		}
		grabberHomer = new UltraGrabber_Home(this);
		grabberHomer.start();
	}
	
	private void hasNotHomedAlert() {
		System.out.println("Cannot move Grabber; has not homed!!");
	}
	
	/**
	 * Sets the elevator's state to "unHomed", requiring 
	 * another homing to work again.
	 */
	public void deHome() {
		hasBeenHomed = false;
		setGrabberIntakeSpeed(GrabberSpeeds.none);
		System.out.println("Grabber De-Homed!!");
	}
	
	public boolean getHomed() {
		return hasBeenHomed;
	}
	
	public boolean getEndstop() {
		return !endstop.get();
	}
	
	public boolean getCubeEye() {
		return !cubeEye.get();
	}
	
	public int getEncoder() {
		return angleMotor.getSelectedSensorPosition(0);
	}
	
	public void zeroEncoder() {
		angleMotor.setSelectedSensorPosition(0, 0, 10);
	}
	
	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public void Periodic() {
		if (!hasBeenHomed && grabberHomer != null && grabberHomer.isFinished()) {
			System.out.println("Grabber Homed!!");
			grabberHomer.free();
			grabberHomer = null;
			hasBeenHomed = true;
			findGrabberPosition(GrabberPositions.up);
		}
	}
}
