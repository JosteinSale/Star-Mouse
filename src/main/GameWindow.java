package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

// Rammen for bildet
public class GameWindow {   
    private JFrame jframe;            // Kan brukes istedenfor at GameWindow extender JFrame
    
    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);                 // Bildet settes inn i rammen
        jframe.pack();
        jframe.setLocationRelativeTo(null);  // Sentrerer bildet i midten av dataskjermen
        jframe.setResizable(false);
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {}

            @Override
            public void windowLostFocus(WindowEvent e) {
                //gamePanel.getGame().windowLostFocus();
            }
            
        });
    }
}
