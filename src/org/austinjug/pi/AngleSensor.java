package org.austinjug.pi;

/**
 * Angle Sensor
 * 12 bits
 */
public class AngleSensor extends Adc121c021Device {
	private static final int ADDRESS = 0x50;
	private static int bits  = 4096/2;
	private static final double DEGREES_PER_BIT = 270.0/bits;

	public AngleSensor() {
		super(ADDRESS);
	}

	public double getAngleDegrees() throws Exception {
		double value = this.getRawReading() * DEGREES_PER_BIT;
		return value;
	}

	public static  void main(String args[]) throws Exception {
		AngleSensor angleSensor = new AngleSensor();
		while(true) {
			Thread.sleep(200);
			double angle = angleSensor.getAngleDegrees();
			System.out.println("angle(deg) = " + angle);
		}
	}
}
