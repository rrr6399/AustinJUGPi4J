package org.austinjug.pi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * http://www.ti.com/product/ADC121C021 
 * 
 * http://www.ti.com/lit/ds/symlink/adc121c021.pdf
 *
 */
public class Adc121c021Device {
	private int i2cAddress = 0x50;
	private static final double REFERENCE_VOLTAGE = 3.1;
	private static final double RATIO = 2 * REFERENCE_VOLTAGE / 4096.0;

	public Adc121c021Device() {
	}

	public Adc121c021Device(int address) {
		this.i2cAddress = address;
	}

	public double getVoltage() throws Exception {
		return this.getRawReading() * RATIO;
	}

	public int getRawReading() throws Exception {
		// Create I2CBus
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		// Get I2C device, ADC121C021 I2C i2cAddress is 0x50(80)
		I2CDevice device = bus.getDevice(i2cAddress);

		// Select configuration register
		// Automatic conversion mode enable
		device.write(0x02, (byte) 0x20);
		Thread.sleep(500);

		// Read 2 bytes of data from address 0x00(0)
		// raw_adc msb, raw_adc lsb
		byte[] data = new byte[2];
		device.read(0x00, data, 0, 2);

		// Convert the data to 12-bits
		//int raw_adc = ((data[0] & 0x0F) * 256 + (data[1] & 0xFF));
		int raw_adc = ((data[0] & 0x0F) << 8 | (data[1] & 0xFF));

		// Output to screen
		System.out.println("Digital Value of Analog Input :" + raw_adc);
		bus.close();

		return raw_adc;
	}

	/**
	 * The automatic conversion mode configures the ADC to continually perform conversions without receiving "read" instructions
	 * from the controller over the I2C interface. The mode is activated by writing a non-zero value into the Cycle Time bits -
	 * D[7:5] - of the Configuration register (see Configuration Register). Once the ADC121C021 enters this mode, the internal
	 * oscillator is always enabled. The ADC's control logic samples the input at the sample rate set by the cycle time bits.
	 * Although the conversion result is not transmitted by the 2-wire interface, it is stored in the conversion result register and
	 * updates the various status registers of the device.
	 *
	 * In automatic conversion mode, the out-of-range alert function is active and updates after every conversion. The ADC can
	 * operate independently of the controller in automatic conversion mode. When the input signal goes "outof- range", an alert
	 * signal is sent to the controller. The controller can then read the status registers and determine the source of the alert
	 * condition. Also, comparison and updating of the VMIN and VMAX registers occurs after every conversion in automatic conversion
	 * mode. The controller can occasionally read the VMIN and/or VMAX registers to determine the sampled input extremes. These
	 * register values persist until the user resets the VMIN and VMAX registers. These two features are useful in system
	 * monitoring, peak detection, and sensing applications.
	 */
}
