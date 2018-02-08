package org.torc.mainrobot.tools;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class MotorControllers {
	public static void TalonSRXConfig(TalonSRX talon, int timeoutMs, int slotIdx, int PIDLoopIdx) {
		
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, PIDLoopIdx, timeoutMs);
		TalonSRXSensorZero(talon, timeoutMs, slotIdx);
		talon.setSensorPhase(true);
		
		/* set the peak and nominal outputs, 12V means full */
        talon.configNominalOutputForward(0, timeoutMs);
        talon.configNominalOutputReverse(0, timeoutMs);
        talon.configPeakOutputForward(1, timeoutMs);
        talon.configPeakOutputReverse(-1, timeoutMs);

        talon.configAllowableClosedloopError(0, PIDLoopIdx, timeoutMs);
        /* set closed loop gains in slot0 */
        talon.config_kF(PIDLoopIdx, 0, timeoutMs);
        talon.config_kP(PIDLoopIdx, 0.1, timeoutMs);
        talon.config_kI(PIDLoopIdx, 0, timeoutMs); 
        talon.config_kD(PIDLoopIdx, 0, timeoutMs);
	}
	public static void TalonSRXSensorZero(TalonSRX talon, int timeoutMs, int PIDLoopIdx) {
		int absolutePosition = talon.getSelectedSensorPosition(timeoutMs) & 0xFFF; /* mask out the bottom12 bits, we don't care about the wrap arounds */
		/* use the low level API to set the quad encoder signal */
        talon.setSelectedSensorPosition(absolutePosition, PIDLoopIdx, timeoutMs);
	}
}
