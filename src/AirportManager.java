/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

import Trees.SortBTree;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AirportManager extends JFrame {

    public static void main(String[] args) {
        SortBTree<Flight> flights = new SortBTree<>();
    }

    private class AirportPanel extends JPanel {

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
