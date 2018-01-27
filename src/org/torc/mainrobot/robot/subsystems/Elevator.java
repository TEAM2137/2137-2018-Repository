package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.commands.elevator.Elevator_Init;
import org.torc.mainrobot.tools.MathExtra;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator extends Subsystem implements InheritedPeriodic {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	public enum ElevatorPositions { floor, middle, high }
	
	public ElevatorPositions elevatorPosition = ElevatorPositions.floor;
	
	public DigitalInput endstop;
	
	private TalonSRX elevator;
	
	private boolean maxLimit = false;
	private boolean minLimit = false;
	
	int targetPosition = 0;
	
	public boolean hasBeenHomed = false;
	
	private static int GetElevatorPositions(ElevatorPositions position) {
		int toReturn = 0;
		switch(position) {
			case floor:
				toReturn = 44135;
				break;
			case middle:
				toReturn = 96199;
				break;
			case high:
				toReturn = 146892;
				break;
		}
		return toReturn;
	}
	
	public Elevator() {
		// Add to periodic list
		org.torc.mainrobot.robot.Robot.AddToPeriodic(this);
		
		elevator = new TalonSRX(3);
		MotorControllers.TalonSRXConfig(elevator, RobotMap.Elev_TimeoutMs, RobotMap.Elev_SlotIdx, RobotMap.Elev_PIDLoopIdx);
		endstop = new DigitalInput(4);
		
        elevator.configPeakOutputForward(0.5, RobotMap.Elev_TimeoutMs);
        elevator.configPeakOutputReverse(-0.5, RobotMap.Elev_TimeoutMs);
	}
	
	public void jogElevatorPerc(double controllerVal) {
		elevator.set(ControlMode.PercentOutput, MathExtra.clamp(controllerVal, (minLimit ? 0 : -0.3), (maxLimit ? 0 : 0.3)));
	}
	
	public void positionFind(ElevatorPositions position) {
		elevator.set(ControlMode.Position, GetElevatorPositions(position));
	}
	public void zeroEncoder() {
		MotorControllers.TalonSRXSensorZero(elevator, RobotMap.Elev_TimeoutMs, RobotMap.Elev_PIDLoopIdx);
	}
	
	public void printEncoder() {
		//System.out.println(elevator.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("ElevatorEncoder", elevator.getSelectedSensorPosition(0));
	}
	
	public int getEncoder() {
		return elevator.getSelectedSensorPosition(0);
	}
	
	public void positionElevator(double position) {
		elevator.set(ControlMode.Position, position);
	}
	
	void checkSoftLimits() {
		// Check for position max
		if (elevator.getSelectedSensorPosition(0) >= 155880) {
			// Reposition to keep below the gear
			positionElevator(155000);
			maxLimit = true;
		}
		else {
			maxLimit = false;
		}
		// Check for position min
		if (hasBeenHomed) {
			if (elevator.getSelectedSensorPosition(0) <= -1000) {
				// Reposition to keep below the gear
				positionElevator(0);
				maxLimit = true;
			}
			else {
				maxLimit = false;
			}
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void Periodic() {
		printEncoder();
		//checkSoftLimits();

	}
	
}
