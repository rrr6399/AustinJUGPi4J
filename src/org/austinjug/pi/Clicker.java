package org.austinjug.pi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Clicker {
	GpioPinDigitalInput input;
	LedBlinker blinker;

	public Clicker() {
		blinker = new LedBlinker();
		// create GPIO controller
		GpioController gpio = GpioFactory.getInstance();
		// create GPIO input
		// need PULL_DOWN to prevent spurious readings due to floating voltage reference
		input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

		// add listener
		input.addListener((GpioPinListenerDigital) (GpioPinDigitalStateChangeEvent event) -> {
			System.out.println("State = " + event.getState() +
					", Edge = " + event.getEdge() +
					", Pin = " +  event.getPin() +
					", Pressed = " +  input.isHigh()
			);
			blinker.on(input.isHigh());
		});
	}

	public static void main(String args[]) throws Exception {
		Clicker clicker = new Clicker();
		synchronized(clicker) {
			clicker.wait();
		}
	}
}
