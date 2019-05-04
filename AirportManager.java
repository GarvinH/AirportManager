/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

import StackAndQueues.Stack;
import Trees.SortBTree;

import java.io.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AirportManager extends JFrame {

    public static void main(String[] args) {
        SortBTree<Flight> flights = new SortBTree<>();
    }

    /**
     *
     */
    public static void loadFile() {
        BufferedReader fileInput = null;
        try {
            File flightFile = new File("flightFile.txt");
            fileInput = new BufferedReader(new FileReader(flightFile));
            String flight = fileInput.readLine();
            for (int i = 0; i < flight.length(); i++){
                //read location, read airline, read name, read
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                fileInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFile(SortBTree flights){
        try {
            Flight tempFlight = null;
            File flightFile = new File("flightFile.txt");
            PrintWriter output = new PrintWriter(flightFile);
            Stack<Flight> flightList = flights.saveTree();
            do {
                tempFlight = flightList.pop();
                output.print(tempFlight.getName() + "," + tempFlight.getAirline() + "," + tempFlight.getLocation() +
                        "," + tempFlight.getDate() + "," + tempFlight.getStatus());
            } while(tempFlight != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
