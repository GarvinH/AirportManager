/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */


import java.io.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

public class AirportManager extends JFrame {

    static SortBTree arrivals, departures;

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        arrivals = new SortBTree<>();
        departures = new SortBTree<>();
        arrivals.add(new Flight("SA1", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        arrivals.add(new Flight("AC2", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        departures.add(new Flight("CA3", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        departures.add(new Flight("ZA4", "United", "San Francisco", "2019/04/04", "1600", "Delayed"));
        arrivals.displayInOrder();
       // System.out.println(removeFlight("CA3"));
        departures.displayInOrder();
       // saveFile();
        //loadFile();
        Stack<Flight> test = arrivals.saveTree();
        System.out.println(test.pop().getName());
        System.out.println(test.pop());
        test = departures.saveTree();
        System.out.println(test.pop());
        System.out.println(test.pop());
    }

    /**
     * Loads up trees from a text file and stores the data into trees
     */
    public static void loadFile() {
        ObjectInputStream fileInput;
        Flight flightInfo;
        Integer size;
        try {
            // load the arrivals
            FileInputStream arriveFile = new FileInputStream("arrivals.ser");
            fileInput = new ObjectInputStream(arriveFile);
            size = (Integer) fileInput.readObject(); // the size of the tree to indicate end of file

            for (int i = 0; i < size; i++) {
                flightInfo = (Flight) fileInput.readObject();
                arrivals.add(flightInfo);
            }

            fileInput.close();
            arriveFile.close();

            // load the departures
            FileInputStream departFile = new FileInputStream("departures.ser");
            fileInput = new ObjectInputStream(departFile);
            size = (Integer) fileInput.readObject(); // the size of the tree to indicate end of file

            for(int i = 0; i< size; i++) {
                flightInfo = (Flight) fileInput.readObject();
                departures.add(flightInfo);
            }
            fileInput.close();
            departFile.close();

        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Saves the current trees onto text files
     */
    public static void saveFile(){
        Integer size;
        try {
            Flight tempFlight = null;
            FileOutputStream arriveFile = new FileOutputStream("arrivals.ser");
            FileOutputStream departFile = new FileOutputStream("departures.ser");

            //save arrivals
            ObjectOutputStream output = new ObjectOutputStream(arriveFile);
            Stack<Flight> arriveList = arrivals.saveTree();
            size = arrivals.size(); // save the size of the tree to indicate end of file
            output.writeObject(size);
            for(int i = 0; i < size; i++) {
                tempFlight = arriveList.pop();
                output.writeObject(tempFlight);
            }
            output.close();
            arriveFile.close();

            // save departures
            output = new ObjectOutputStream(departFile);
            Stack<Flight> departList = departures.saveTree();
            size = departures.size(); // save the size of the tree to indicate end of file
            output.writeObject(size);
            for(int i = 0; i < size; i++) {
                tempFlight = departList.pop();
                output.writeObject(tempFlight);
            }
            output.close();
            departFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes a flight's current status
     * @param flight Name of the flight
     * @param status Status to be changed to
     * @return boolean value if flight is valid
     */
    public static boolean changeFlightStatus(String flight, String status) {
        Stack<Flight> arrivalStack = arrivals.saveTree();
        Stack<Flight> departStack = departures.saveTree();
        Flight tempFlight;
        do {
            tempFlight = arrivalStack.pop();
            if ((tempFlight == null) || (!tempFlight.getName().equalsIgnoreCase(flight))) {
                tempFlight = departStack.pop();
            }
        } while (tempFlight != null && !tempFlight.getName().equalsIgnoreCase(flight));

        if (tempFlight != null) {
            tempFlight.setStatus(status);
            return true;
        }
        return false;
    }

    /**
     * Changes a flight's arrival/departure time
     * @param flight Name of the flight
     * @param time Time to be changed to
     * @return boolean value if flight is valid
     */
    public static boolean changeFlightTime(String flight, String time) {
        Stack<Flight> arrivalStack = arrivals.saveTree();
        Stack<Flight> departStack = departures.saveTree();
        Flight tempFlight;
        do {
            tempFlight = arrivalStack.pop();
            if ((tempFlight == null) || (!tempFlight.getName().equalsIgnoreCase(flight))) {
                tempFlight = departStack.pop();
            }
        } while (tempFlight != null && !tempFlight.getName().equalsIgnoreCase(flight));
        if (tempFlight != null) {
            tempFlight.setTime(time);
            return true;
        }
        return false;
    }

    /**
     * Changes a flight's arrival/departure time
     * @param flight Name of the flight
     * @param date Date to be changed to
     * @return boolean value if flight is valid
     */
    public static boolean changeFlightDate(String flight, String date) {
        Stack<Flight> arrivalStack = arrivals.saveTree();
        Stack<Flight> departStack = departures.saveTree();
        Flight tempFlight;
        do {
            tempFlight = arrivalStack.pop();
            if ((tempFlight == null) || (!tempFlight.getName().equalsIgnoreCase(flight))) {
                tempFlight = departStack.pop();
            }
        } while (tempFlight != null && !tempFlight.getName().equalsIgnoreCase(flight));
        if (tempFlight != null) {
            tempFlight.setDate(date);
            return true;
        }
        return false;
    }

    /**
     * Removes a flight from the database
     * @param flight The flight name
     * @return Boolean value indicating success or failure
     */
    public static boolean removeFlight(String flight) {
        Stack<Flight> arrivalStack = arrivals.saveTree();
        Stack<Flight> departStack = departures.saveTree();
        Flight tempFlight;
        do {
            tempFlight = arrivalStack.pop();
        } while (tempFlight != null && !tempFlight.getName().equalsIgnoreCase(flight));

        if (tempFlight != null) {
            arrivals.remove(tempFlight);
            return true;
        } else {
            do {
                tempFlight = departStack.pop();
            } while (tempFlight != null && !tempFlight.getName().equalsIgnoreCase(flight));
        }

        if (tempFlight != null) {
            departures.remove(tempFlight);
            return true;
        }

        return false;
    }


    private class AirportPanel extends JPanel {

    }

    private class Button extends JButton{

    }
 
}
