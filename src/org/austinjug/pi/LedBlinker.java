package org.austinjug.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class LedBlinker {
	GpioPinDigitalOutput output;

	public LedBlinker() {
		GpioController gpio = GpioFactory.getInstance();
		output = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, PinState.LOW);
	}

	public void on() {
		output.high();
	}

	public void off() {
		output.low();
	}

	public void on(boolean on) {
		if(on) {
			on();
		} else {
			off();
		}
	}
	
	public void test() throws Exception {

		System.out.println("Setting output to high");
		output.high();  // set to high state
		Thread.sleep(4000);

		System.out.println("Setting output to low");
		output.low();  // set to low state
		Thread.sleep(4000);

		System.out.println("Toggling output");
		output.toggle(); // switch to other state
		Thread.sleep(4000);

		System.out.println("Turn on for 4 second");
		output.pulse(4000); // set state for a duration

		System.out.println("Set output to low");
		output.low();  // set to low state

		System.out.println("Done");

	}

	public static void main(String args[]) throws Exception {
		LedBlinker blinker = new LedBlinker();
		blinker.test();
		System.exit(0);
	}

}
