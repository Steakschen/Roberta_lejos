package EV3_leJOS;

import org.jfree.util.WaitingImageObserver;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

	
public class RobertaIR {
	
public static int control = 0;
public static float distance = 255;

		static RegulatedMotor leftMotor = Motor.D;
		static RegulatedMotor rightMotor = Motor.A;
		static EV3UltrasonicSensor sensorU;
		static DifferentialPilot pilot = new DifferentialPilot(2.1f, 2.1f ,leftMotor, rightMotor,  false );
		  
		
		public static void introMessage() {

			LCD.drawString("Roberta DriveDemo", 0, 1);
			LCD.drawString("Linker Motor - D", 0, 3);
			LCD.drawString("Rechter Motor - A", 0, 4);
			LCD.drawString("UltraschallSensor - 4", 0, 6);
			Delay.msDelay(2000);
			LCD.clear();
		}


		public static void startSequence() {

			pilot.reset();
			pilot.setAcceleration(100);

		}
		
		

		public static void main(String[] args) {
		
			sensorU = new EV3UltrasonicSensor(SensorPort.S4);
			
			introMessage();
			
			startSequence();
			
			while (!Button.ESCAPE.isDown()) {

				while (!isDistance()) {
					if(Button.ESCAPE.isDown()) System.exit(0);
					driveForward();
				}
				driveStop();
				driveChange();

			
			}
			if(Button.ESCAPE.isDown()) System.exit(0);
			
		}
		
	    
		private static boolean isDistance() {
			float [] sample;
			SampleProvider sampleProv = sensorU.getDistanceMode();
			sample = new float[sampleProv.sampleSize()];
			
			sampleProv.fetchSample(sample, 0);
            distance = sample[0];

            if (distance < 0.25) {
            	return true;
            }else {
            	 return false;
            }
           
		}

		
		
		private static void driveChange() {
			driveBackward();
			Delay.msDelay(1000);
			driveStop();
			driveLeft();
			Delay.msDelay(600);
			driveStop();
		}
		
		
		private static void driveRight() {
			//pilot.rotateRight();
			pilot.steer(100, -270);
			
		}


		private static void driveLeft() {
			//pilot.rotateLeft();
			pilot.steer(100, 270);
		}

		
		private static void driveStop() {
			pilot.stop();
		}


		private static void driveBackward() {
			pilot.backward();
			
		}


		private static void driveForward() {
			pilot.forward();
			
		}
	}
