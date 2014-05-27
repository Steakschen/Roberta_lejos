import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;


public class LineFollowerMain {

	public static void main(String[] args) {
		EV3LineFollower lineFollower = new EV3LineFollower(SensorPort.S1, MotorPort.D, MotorPort.A, 300);
		lineFollower.initialize();
		lineFollower.start();
	    while (Button.readButtons() != Button.ID_ENTER) {
	    	lineFollower.drive();
	    }
	}

}
