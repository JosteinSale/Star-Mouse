package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

// Rammen for bildet
public class GameWindow {   
    private JFrame jframe;            // Kan brukes istedenfor at GameWindow extender JFrame
    
    public GameWindow(GamePanel gamePanel, int SCREEN_WIDTH, int SCREEN_HEIGHT) {
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (Game.fullScreen) {
            jframe.setLayout(new GridBagLayout());
            jframe.add(gamePanel, new GridBagConstraints());
            jframe.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            jframe.getContentPane().setBackground(Color.BLACK);   // Trengs for sidene av skjermen
            jframe.setLocationRelativeTo(null);     // Sentrerer bildet i midten av dataskjermen
            jframe.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            jframe.setUndecorated(true);
        }
        else {
            jframe.add(gamePanel);
            jframe.pack();
            jframe.setLocationRelativeTo(null);
        }
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {}

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowLostFocus();
            }
        });
    }
}
