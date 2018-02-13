package org.torc.mainrobot.tools;

public class MathExtra {
	public static float clamp(float val, float min, float max) {
	    return Math.max(min, Math.min(max, val));
	}
	public static double clamp(double val, double min, double max) {
	    return Math.max(min, Math.min(max, val));
	}
	public static int clamp(int val, int min, int max) {
	    return Math.max(min, Math.min(max, val));
	}
	public static double applyDeadband(double value, double deadband) {
		if (Math.abs(value) > deadband) {
			if (value > 0.0) {
				return (value - deadband) / (1.0 - deadband);
			} 
			else {
				return (value + deadband) / (1.0 - deadband);
			}
		} 
		else {
			  	return 0.0;
		}
	}
}
