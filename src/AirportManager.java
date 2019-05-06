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
    public static void main(String[] args) {
        arrivals = new SortBTree<>();
        departures = new SortBTree<>();
        //arrivals.add(new Flight("SA1", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        //arrivals.add(new Flight("AC2", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
       // departures.add(new Flight("CA3", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        //departures.add(new Flight("ZA4", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        //arrivals.displayInOrder();
       // departures.displayInOrder();
        //saveFile();
        loadFile();
        Stack<Flight> test = arrivals.saveTree();
        System.out.println(test.pop().getName());
        System.out.println(test.pop().getName());
        test = departures.saveTree();
        System.out.println(test.pop().getName());
        System.out.println(test.pop().getName());
    }

    /**
     *
     */
    public static void loadFile() {
        BufferedReader fileInput = null;
        String name = "", airline = "", status = "", location = "", date = "", time = "", tempString;
        String flightInfo;
        int category;
        try {
            File arriveFile = new File("arrivals.txt");
            fileInput = new BufferedReader(new FileReader(arriveFile));
            flightInfo = fileInput.readLine();
            while(flightInfo != null) {
                category = 0;
                tempString = "";
                for (int i = 0; i < flightInfo.length(); i++) {
                    if(flightInfo.charAt(i) != ',') {
                        tempString += flightInfo.substring(i, i+1);
                    } else {
                        //read name, airline, location, date, time, and status
                        switch (category) {
                            case (0): {
                                name = tempString;
                                break;
                            }
                            case (1): {
                                airline = tempString;
                                break;
                            }
                            case (2): {
                                location = tempString;
                                break;
                            }
                            case (3): {
                                date = tempString;
                                break;
                            }
                            case (4): {
                                time = tempString;
                                break;
                            }
                            case (5): {
                                status = tempString;
                                break;
                            }
                        }
                        category++;
                        tempString = "";
                    }
                }
                arrivals.add(new Flight(name, airline, location, date, time, status));
                flightInfo = fileInput.readLine();
            }
            fileInput.close();
            File departFile = new File("departures.txt");
            fileInput = new BufferedReader(new FileReader(departFile));
            flightInfo = fileInput.readLine();
            while(flightInfo != null) {
                category = 0;
                tempString = "";
                for (int i = 0; i < flightInfo.length(); i++) {
                    if(flightInfo.charAt(i) != ',') {
                        tempString += flightInfo.substring(i, i+1);
                    } else {
                        //read name, airline, location, date, time, and status
                        switch (category) {
                            case (0): {
                                name = tempString;
                                break;
                            }
                            case (1): {
                                airline = tempString;
                                break;
                            }
                            case (2): {
                                location = tempString;
                                break;
                            }
                            case (3): {
                                date = tempString;
                                break;
                            }
                            case (4): {
                                time = tempString;
                                break;
                            }
                            case (5): {
                                status = tempString;
                                break;
                            }
                        }
                        category++;
                        tempString = "";
                    }
                }
                departures.add(new Flight(name, airline, location, date, time, status));
                flightInfo = fileInput.readLine();
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

    public static void saveFile(){

        try {
            Flight tempFlight = null;
            File arriveFile = new File("arrivals.txt");
            File departFile = new File("departures.txt");
            PrintWriter output = new PrintWriter(arriveFile);
            Stack<Flight> arriveList = arrivals.saveTree();
            tempFlight = arriveList.pop();
            while(tempFlight != null){
                System.out.println(tempFlight.getName());
                output.print(tempFlight.getName() + "," + tempFlight.getAirline() + "," + tempFlight.getLocation() +
                        "," + tempFlight.getDate() + "," + tempFlight.getTime() + "," + tempFlight.getStatus() + ",");
                output.println("");
                tempFlight = arriveList.pop();
            }
            output.close();
            output = new PrintWriter(departFile);
            Stack<Flight> departList = departures.saveTree();
            tempFlight = departList.pop();
            while(tempFlight != null){
                System.out.println(tempFlight.getName());
                output.print(tempFlight.getName() + "," + tempFlight.getAirline() + "," + tempFlight.getLocation() +
                        "," + tempFlight.getDate() + "," + tempFlight.getTime() + "," + tempFlight.getStatus() + ",");
                output.println("");
                tempFlight = departList.pop();
            }

            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AirportPanel extends JPanel {

    }

    private class Button extends JButton{

    }
 
}
