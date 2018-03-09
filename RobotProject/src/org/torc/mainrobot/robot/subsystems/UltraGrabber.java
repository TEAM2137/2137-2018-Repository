package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.Robot;
import org.torc.mainrobot.tools.MathExtra;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 	ULTRA GRABBER
 *  (A.K.A, the cube grabber)
 */
public class UltraGrabber extends Subsystem implements InheritedPeriodic {
	
	private VictorSP leftMotor;
	private VictorSP rightMotor;
	
	private TalonSRX angleMotor;
	
	private DigitalInput endstop, cubeEye;
	
	private  UltraGrabber_Homing grabberHomer;
	
	private boolean hasBeenHomed = false;
	// TODO: change temportary positions to final
	public enum GrabberPositions { up, flat, pickup, shooting }
	// TODO: change temportary speeds to final
	public enum GrabberSpeeds { none, cubeKeep, pickup, dropping, shooting }
	
	public final static double angleMult = 4521;
	
										 //-20
	private final static double ticksMin = -7 * angleMult;
	private final static double ticksMax = 70 * angleMult;
	
	private final static double ticksHomePacked = -20 * angleMult;
	private boolean packedHome = false;
	
	private double targetAngle = 0;

	public UltraGrabber(int leftVictorPort, int rightVictorPort, int angleTalonPort, int endstopPort, int cubePhotoeyePort) {
		// Add to periodic list
		org.torc.mainrobot.robot.Robot.AddToPeriodic(this);
		
		leftMotor = new VictorSP(leftVictorPort);
		rightMotor = new VictorSP(rightVictorPort);

		angleMotor = new TalonSRX(angleTalonPort);
		
		MotorControllers.TalonSRXConfig(angleMotor, 10, 0, 0, 0, 0.005, 0.00001, 0);
		angleMotor.config_IntegralZone(0, 40000, 10);
		
		angleMotor.configPeakOutputForward(0.75, 10);
		angleMotor.configPeakOutputReverse(-0.75, 10);
		
		endstop = new DigitalInput(endstopPort);
		
		cubeEye = new DigitalInput(cubePhotoeyePort);
	}
	
	/**
	 * @param position
	 * @return The position of the grabber state in encoder ticks.
	 */
	public static double GetGrabberPositions(GrabberPositions position) {
		int toReturn = 0;
		switch(position) {
			case up:
				toReturn = -7;//-28;
				break;
			case pickup:
				toReturn = 70; //75
				break;
			case flat:
				toReturn = 60;
				break;
			case shooting:
				toReturn = 8;
				break;
		}
		SmartDashboard.putNumber("GrabberTarget", (toReturn * angleMult));
		//grabTarget = (toReturn * angleMult);
		// Convert degrees from lookup to encoder ticks
		return (toReturn * angleMult);
	}
	
	public static double GetGrabberSpeeds(GrabberSpeeds speed) {
		double toReturn = 0;
		switch(speed) {
			case none:
				toReturn = 0;
				break;
			case cubeKeep:
				toReturn = -0.15;
				break;
			case pickup:
				toReturn = -1; //-0.85
				break;
			case dropping:
				toReturn = 0.5;
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
		double targ = GetGrabberPositions(position);
		targetAngle = targ / angleMult;
		targetAngle = MathExtra.clamp((targetAngle * angleMult), ticksMin, ticksMax) / angleMult;
		angleMotor.set(ControlMode.Position, MathExtra.clamp(targ, ticksMin, ticksMax));
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
	
	public void jogGrabberPosInc(double angleInc) {
		if (!hasBeenHomed) {
			hasNotHomedAlert();
			return;
		}
		targetAngle += angleInc;
		targetAngle = MathExtra.clamp((targetAngle * angleMult), ticksMin, ticksMax) / angleMult;
		angleMotor.set(ControlMode.Position, MathExtra.clamp((targetAngle * angleMult), ticksMin, ticksMax));
	}
	
	public void homeGrabber() {
		if (hasBeenHomed) {
			deHome();
		}
		grabberHomer = new UltraGrabber_Homing(this);
		grabberHomer.start();
	}
	
	public void homeGrabberPacked() {
		if (hasBeenHomed) {
			deHome();
		}
		grabberHomer = new UltraGrabber_Homing(this);
		grabberHomer.start();
		packedHome = true;
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
		// Practice bot: 
		// return !cubeEye.get();
		return cubeEye.get();
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
		if (!hasBeenHomed && grabberHomer != null && grabberHomer.isHomed()) {
			System.out.println("Grabber Homed!!");
			grabberHomer = null;
			hasBeenHomed = true;
			
			if (!packedHome) {
				findGrabberPosition(GrabberPositions.up);
			}
			else {
				angleMotor.set(ControlMode.Position, ticksHomePacked);
				packedHome = false;
			}
		}
	}
}

class UltraGrabber_Homing implements InheritedPeriodic {
	
	enum HomingStates { firstMoveDown, secondMoveUp }
	
	HomingStates homingState = HomingStates.firstMoveDown;
	
	UltraGrabber grabberSubsys;
	
	private boolean started = false;
	
	private boolean isFinished = false;
	
	double firstMoveDownPerc = 0.5;
	double secondMoveUpPerc = 0.3;
	
	public UltraGrabber_Homing(UltraGrabber grabber) {
		Robot.AddToPeriodic(this);
		
		grabberSubsys = grabber;
	}

	public void start() {
		started = true;
	}
	
	public boolean isHomed() {
		return isFinished;
	}
	
	@Override
	public void Periodic() {
		if (started) {
			switch (homingState) {
			case firstMoveDown:
				grabberSubsys.jogGrabberPerc(-firstMoveDownPerc);
				if (grabberSubsys.getEndstop()) {
					System.out.println("grabber firstMoveDown Done!");
					homingState = HomingStates.secondMoveUp;
				}
				break;
			case secondMoveUp:
				grabberSubsys.jogGrabberPerc(secondMoveUpPerc);
				if (!grabberSubsys.getEndstop()) {
					System.out.println("grabber secondMoveUp Done!");
					grabberSubsys.zeroEncoder();
					grabberSubsys.jogGrabberPerc(0);
					isFinished = true;
				}
				break;
			}
		}
		
		if (isFinished) {
			started = false;
		}
	}
	
}

