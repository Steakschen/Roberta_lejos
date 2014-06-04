package EV3_leJOS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;

import lejos.hardware.BrickFinder;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

public class Control extends JFrame {

public Control() {
      super("My Controller");
      RemoteEV3 ev3 = (RemoteEV3) BrickFinder.getDefault();
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JButton turn = new JButton("Turn");
      JButton forward = new JButton("Forward");
      JButton backward = new JButton("Backward");
      JButton left = new JButton("Left");
      JButton right = new JButton("Right");
      
      this.getContentPane().add(turn);
      this.getContentPane().add(forward);
      this.getContentPane().add(backward);
      this.getContentPane().add(left);
      this.getContentPane().add(right);
      
      final RMIRegulatedMotor leftMotor = ev3.createRegulatedMotor("D", 'L');
      final RMIRegulatedMotor rightMotor = ev3.createRegulatedMotor("A", 'L');
      
      turn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            try {
               leftMotor.rotate(500,true);
               rightMotor.rotate(-500);
            } catch (RemoteException e) {
               e.printStackTrace();
            }
         }         
      });
      
      
 
       addWindowListener(new WindowAdapter() {
           @Override
           public void windowClosing(WindowEvent e) {
              try {
               leftMotor.close();
               rightMotor.close();
            } catch (RemoteException e1) {
               e1.printStackTrace();
            }
           }
       });
   }
   
   public static void main(String[] args) {
      Control control = new Control();
        control.pack();
        control.setVisible(true);
   }
}