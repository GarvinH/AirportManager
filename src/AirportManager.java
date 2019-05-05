/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

import javax.swing.JScrollPane;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class AirportManager extends JFrame {
    static JFrame window;
    JPanel flightEditor;

    public static void main(String[] args) {
        window = new AirportManager();
        SortBTree<Flight> flights = new SortBTree<>();
    }

    AirportManager() {
        super("Flight Editor");

        flightEditor = new AirportPanel();
        flightEditor.add(new testScroll());
        this.add(flightEditor);

        //this.add(new JScrollPane(new JTextField("Hello Wolrd \nt\nt\nt\nt\nt\nt\nt\nt\nt\ntt\nt\nt\nasdlkfjapjjgaporiaigp")));
        //use jlist

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(640, 480);
        this.setVisible(true);
    }

    private class AirportPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setDoubleBuffered(true);
            g.setColor(Color.BLACK);
            g.fillRect(100,400, 200, 200);
            this.setPreferredSize(new Dimension(640,480));



            repaint();
        }
    }

    private class testScroll extends JScrollPane {
        testScroll() {
            JTextArea test = new JTextArea(100,100);

            JScrollPane scrollFrame = new JScrollPane(test);
            scrollFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollFrame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            this.setAutoscrolls(true);
            scrollFrame.setPreferredSize(new Dimension(100,100));
            scrollFrame.setVisible(true);
            getContentPane().add(scrollFrame);
            //this.add(scrollFrame, BorderLayout.CENTER);
        }

    }

    private class Button extends JButton{

    }
    private class MyKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }
    }

}
