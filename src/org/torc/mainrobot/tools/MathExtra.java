package org.torc.mainrobot.tools;

public class MathExtra {
	public static float clamp(float val, float min, float max) {
	    return Math.max(min, Math.min(max, val));
	}
	public static double clamp(double val, double min, double max) {
	    return Math.max(min, Math.min(max, val));
	}
}
