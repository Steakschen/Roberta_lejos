package EV3_leJOS;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * 
 * @author moritz
 *
 */
public class EV3LineFollower {
	private EV3ColorSensor colorSensor;
	private EV3LargeRegulatedMotor motorLeft;
	private EV3LargeRegulatedMotor motorRight;
	
	private float[] colorSensorSample;
	private SampleProvider redMode;
	
	private float TP;
	private float KP;
	
	private float powerLeftMotor;
	private float powerRightMotor;
	
	private float error;
	private float turn;
	private float brightValue;
	private float darkValue;
	private float currentValue;
	private float midValue;
	
	/**
	 * Constructor for the EV3LineFollower.
	 * @param colSenPort	Port of the color sensor
	 * @param motLefPort	Port of the left motor
	 * @param motRigPort	Port of the right motor
	 * @param baseSpeed		Basespeed, should be less than 300
	 */
	public EV3LineFollower(Port colSenPort, Port motLefPort, Port motRigPort, float baseSpeed) {
		colorSensor 		= new EV3ColorSensor(colSenPort);
		motorLeft 			= new EV3LargeRegulatedMotor(motLefPort);
		motorRight 			= new EV3LargeRegulatedMotor(motRigPort);
		redMode				= colorSensor.getRedMode();
		colorSensorSample	= new float [redMode.sampleSize()];
		TP					= baseSpeed;
		KP					= TP / 8;
	}
	
	/**
	 * Initializes the color values.
	 * This function allows the user to manually calibrate the light values.
	 * Calculates the midvalue.
	 */
	public void initialize() {
		/*Read the High Value*/
		LCD.drawString("Calibrate bright", 0, 0);
	    Button.waitForAnyPress();
	    brightValue = fetchCurrentValue();
	    LCD.clear();
	    
	    /*Read the Low Value*/
		LCD.drawString("Calibrate dark", 0, 0);
	    Button.waitForAnyPress();
	    darkValue = fetchCurrentValue();
	    LCD.clear();
	    
	    midValue = (brightValue + darkValue) / 2;
	    System.out.println("High: " + Float.toString(brightValue)+'\n'+"Low: "+Float.toString(darkValue));
	    Delay.msDelay(2000);
	}
	
	/**
	 * Initializes the color values.
	 * @param bright	value for brightValue
	 * @param dark		value for darkValue
	 */
	public void initialize(float bright, float dark) {
		this.brightValue 	= bright;
		this.darkValue		= dark;
	    this.midValue 		= (brightValue + darkValue) / 2;
	}
	
	/**
	 * Starts the motors. Has to be called before drive()
	 */
	public void start() {
	    motorLeft.setSpeed(TP);
	    motorRight.setSpeed(TP);
	    
	    motorLeft.forward();
	    motorRight.forward();
	}
	
	/**
	 * Function which corrects the track. P controlled.
	 */
	public void drive() {
		currentValue = fetchCurrentValue();

		error = midValue*10 - currentValue*10;

		turn = KP * error;
		powerLeftMotor = TP - turn;
		powerRightMotor = TP + turn;

		motorLeft.setSpeed(powerLeftMotor);
		motorRight.setSpeed(powerRightMotor);
	}
	
	/**
	 * Fetches the current reflected light value.
	 * @return reflected light value between 0 and 1
	 */
	private float fetchCurrentValue () {
		redMode.fetchSample(colorSensorSample, 0);
		return colorSensorSample[0];
	}
	
}
