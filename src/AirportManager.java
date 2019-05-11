/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.stream.Location;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class AirportManager extends JFrame {
    static JFrame window;
    JPanel flightEditor;
    static SortBTree arrivals, departures;

    public static void main(String[] args) {
        arrivals = new SortBTree<>();
        departures = new SortBTree<>();

        arrivals.add(new Flight("SA1", "Air Canada", "San Francisco", "2019/04/04", "1700", "Delayed"));
        arrivals.add(new Flight("AC2", "British Airways", "San Francisco", "2019/04/04", "1700", "Delayed"));
        departures.add(new Flight("CA3", "WestJet", "San Francisco", "2019/04/04", "1700", "Delayed"));
        departures.add(new Flight("ZA4", "United Airlines", "San Francisco", "2019/04/04", "1600", "Delayed"));
        //arrivals.displayInOrder();
        System.out.println(removeFlight("SA2"));
        //departures.displayInOrder();
        // saveFile();
        //loadFile();
        Stack<Flight> test = arrivals.saveTreeStack();
        //System.out.println(test.pop().getName());
        //System.out.println(test.pop());
        test = departures.saveTreeStack();
        //System.out.println(test.pop().getName());
        //System.out.println(test.pop().getName());
        window = new AirportManager();


    }

    AirportManager() {
        super("Flight Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(640, 480);
        this.setResizable(false);
        //getContentPane().setLayout(null);

        /*JMenuBar menuBar = new JMenuBar();
        JMenu fileTab = new JMenu("File");
        menuBar.add(fileTab);
        setJMenuBar(menuBar);*/

        String[] dayStrings = new String[31];
        String[] monthStrings = new String[12];
        for (int i = 1; i < 32; i++) {
            dayStrings[i-1] = Integer.toString(i);
            if (i < 13) {
                monthStrings[i-1] = Integer.toString(i);
            }
        }
        JComboBox dayOptions = new JComboBox(dayStrings);
        JComboBox monthOptions = new JComboBox(monthStrings);
        JPanel dateHolder = new JPanel();
        JPanel monthHolder = new JPanel();
        //dateHolder.setLayout(null);
        dateHolder.setSize(new Dimension(50,50));
        dateHolder.add(dayOptions, BorderLayout.PAGE_START);
        dateHolder.setLocation(10,10);
        monthHolder.setSize(new Dimension(50,50));
        monthHolder.add(monthOptions, BorderLayout.PAGE_START);
        monthHolder.setLocation(70,10);

        //JTabbedPane flightTabs = new JTabbedPane();

        flightEditor = new AddPanel();
        flightEditor.setBounds(0,0,getWidth(),getHeight());
        //flightTabs.addTab("Add a Flight", flightEditor);
        //flightTabs.setMnemonicAt(0, KeyEvent.VK_1);
        add(new AirportTabs());
        //flightEditor.add(new testScroll());
        //add(flightEditor);
        //add(dayOptions, BorderLayout.PAGE_START);
        //add(dateHolder);
        //add(monthHolder);

        //this.add(new JScrollPane(new JTextField("Hello Word \nt\nt\nt\nt\nt\nt\nt\nt\nt\ntt\nt\nt\nasdlkfjapjjgaporiaigp")));
        //use jlist

        this.setVisible(true);
    }

    private class AirportTabs extends JTabbedPane {
        AirportTabs() {
            addTab("Add a Flight", flightEditor);
            addTab("Remove a Flight", new JPanel());
        }
    }

    private class AddPanel extends JPanel implements ActionListener {
        JComboBox yearOptions;
        JComboBox monthOptions;
        JComboBox dayOptions;
        JComboBox hourOptions;
        JComboBox minuteOptions;
        JTextField setLocation;
        JComboBox direction;
        JTextField flightName;
        JTextField flightCompany;
        JComboBox status;
        AddPanel() {
            setLayout(null);
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();


            String[] yearStrings = {"-", Integer.toString(year), Integer.toString(year+1)};
            String[] monthStrings = new String[13];
            String[] directionStrings = {"-","Arriving", "Departing"};
            String[] statusStrings = {"-", "On Time", "Delayed", "Cancelled"};
            monthStrings[0] = "-";
            for (int i = 1; i < 13; i++) {
                monthStrings[i] = Integer.toString(i);
            }
            String[] dayStrings = new String[32];
            dayStrings[0] = "-";
            for (int i = 1; i < 32; i++) {
                dayStrings[i] = Integer.toString(i);
            }
            String[] hourStrings = new String[25];
            hourStrings[0] = "-";
            for (int i = 0; i < 24; i++) {
                if (i < 10) {
                    hourStrings[i+1] = "0" + Integer.toString(i);
                } else {
                    hourStrings[i+1] = Integer.toString(i);
                }
            }
            String[] minuteStrings = new String[61];
            minuteStrings[0] = "-";
            for (int i = 0; i < 60; i++) {
                if (i < 10) {
                    minuteStrings[i+1] = "0" + Integer.toString(i);
                } else {
                    minuteStrings[i+1] = Integer.toString(i);
                }
            }

            dayOptions = new JComboBox(dayStrings);
            monthOptions = new JComboBox(monthStrings);
            yearOptions = new JComboBox(yearStrings);
            hourOptions = new JComboBox(hourStrings);
            minuteOptions = new JComboBox(minuteStrings);
            direction  = new JComboBox(directionStrings);
            status = new JComboBox(statusStrings);
            JButton addFlight = new JButton("Add Flight");
            JButton clear = new JButton("Clear");

            monthOptions.addActionListener(this);
            yearOptions.addActionListener(this);
            addFlight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean add = true;
                    if (yearOptions.getSelectedItem().equals("-")) {
                        yearOptions.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        yearOptions.setBorder(BorderFactory.createEmptyBorder());
                    }
                    if (monthOptions.getSelectedItem().equals("-")) {
                        monthOptions.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        monthOptions.setBorder(BorderFactory.createEmptyBorder());
                    }
                    if (dayOptions.getSelectedItem().equals("-")) {
                        dayOptions.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        dayOptions.setBorder(BorderFactory.createEmptyBorder());
                    }
                    if (hourOptions.getSelectedItem().equals("-")) {
                        hourOptions.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        hourOptions.setBorder(BorderFactory.createEmptyBorder());
                    }
                    if (minuteOptions.getSelectedItem().equals("-")) {
                        minuteOptions.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        minuteOptions.setBorder(BorderFactory.createEmptyBorder());
                    }
                    if (setLocation.getText().equals("")) {
                        setLocation.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        setLocation.setBorder(new JTextField().getBorder());
                    }
                    if (direction.getSelectedItem().equals("-")) {
                        direction.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        direction.setBorder(BorderFactory.createEmptyBorder());
                    }
                    if (flightName.getText().equals("")) {
                        flightName.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        flightName.setBorder(new JTextField().getBorder());
                    }
                    if (flightCompany.getText().equals("")) {
                        flightCompany.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        flightCompany.setBorder(new JTextField().getBorder());
                    }
                    if (status.getSelectedItem().equals("-")) {
                        status.setBorder(BorderFactory.createLineBorder(Color.RED));
                        add = false;
                    } else {
                        status.setBorder(BorderFactory.createEmptyBorder());
                    }

                    if (add) {
                        if (direction.getSelectedItem().equals("Arriving")) {
                            arrivals.add(new Flight(flightName.getText(), flightCompany.getText(), setLocation.getText(), yearOptions.getSelectedItem()+"/"+monthOptions.getSelectedItem()+"/"+dayOptions.getSelectedItem(), (String)hourOptions.getSelectedItem()+minuteOptions.getSelectedItem(), (String)status.getSelectedItem()));
                            clearInputs();
                        } else {
                            departures.add(new Flight(flightName.getText(), flightCompany.getText(), setLocation.getText(), yearOptions.getSelectedItem()+"/"+monthOptions.getSelectedItem()+"/"+dayOptions.getSelectedItem(), (String)hourOptions.getSelectedItem()+minuteOptions.getSelectedItem(), (String)status.getSelectedItem()));
                            //clearInputs();
                        }
                    }
                }
            });
            clear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clearInputs();
                    Stack<Flight> test = departures.saveTreeStack();
                    departures.displayInOrder();
                    System.out.println(test.pop().getName());
                }
            });

            setLocation = new JTextField();
            flightName = new JTextField();
            flightCompany = new JTextField();

            yearOptions.setBounds(10,60,55,25);
            monthOptions.setBounds(85, 60, 45, 25);
            dayOptions.setBounds(150,60,45,25);
            hourOptions.setBounds(10, 150, 45, 25);
            minuteOptions.setBounds(75, 150, 45, 25);
            setLocation.setBounds(10, 220, 80, 25);
            direction.setBounds(10, 285, 85, 25);
            flightName.setBounds(300, 40, 80, 25);
            flightCompany.setBounds(300, 115, 80, 25);
            status.setBounds(300, 185, 90, 25);
            addFlight.setBounds(370, 350, 100, 40);
            clear.setBounds(500, 350, 80, 40);

            add(yearOptions);
            add(monthOptions);
            add(dayOptions);
            add(hourOptions);
            add(minuteOptions);
            add(setLocation);
            add(direction);
            add(flightName);
            add(flightCompany);
            add(status);
            add(addFlight);
            add(clear);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Integer[] longMonths = {1,3,5,7,8,10,12};
            String[] longDates = new String[32];
            longDates[0] = "-";
            String[] shortDates = new String[31];
            shortDates[0] = "-";
            String[] febDates = new String[29];
            febDates[0] = "-";
            String[] leapDates = new String[30];
            leapDates[0] = "-";
            for (int i = 1; i < 32; i++) {
                if (i < febDates.length) {
                    febDates[i] = Integer.toString(i);
                }
                if (i < leapDates.length) {
                    leapDates[i] = Integer.toString(i);
                }
                if (i < shortDates.length) {
                    shortDates[i] = Integer.toString(i);
                }
                longDates[i] = Integer.toString(i);
            }
            DefaultComboBoxModel monthLength;

            JComboBox source = (JComboBox)e.getSource();
            if (source.equals(yearOptions)) {
                monthOptions.setSelectedItem("-");
                dayOptions.setSelectedItem("-");
            } else if (source.equals(monthOptions)) {
                if (!monthOptions.getSelectedItem().equals("-")) {
                    int monthNumber = Integer.parseInt((String) monthOptions.getSelectedItem());
                    dayOptions.setSelectedItem("-");
                    if (Arrays.asList(longMonths).contains(monthNumber)) {
                        monthLength = new DefaultComboBoxModel(longDates);
                        dayOptions.setModel(monthLength);
                    } else {
                        dayOptions.setSelectedItem("-");
                        if (monthNumber == 2) {
                            if (!yearOptions.getSelectedItem().equals("-")) {
                                if (Integer.parseInt((String) yearOptions.getSelectedItem()) % 4 == 0) {
                                    monthLength = new DefaultComboBoxModel(leapDates);
                                    dayOptions.setModel(monthLength);
                                } else {
                                    monthLength = new DefaultComboBoxModel(febDates);
                                    dayOptions.setModel(monthLength);
                                }
                            }
                        } else {
                            monthLength = new DefaultComboBoxModel(shortDates);
                            dayOptions.setModel(monthLength);
                        }
                    }
                }
            }
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setDoubleBuffered(true);
            repaint();
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Date", 10 ,25 );
            g.drawString("Time", 10, 115);
            g.drawString("Location", 10, 210);
            g.drawString("Arrive/Depart", 10, 275);
            g.drawString("Flight Name", 300, 25);
            g.drawString("Flight Company", 300, 100);
            g.drawString("Status", 300, 170);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Year",10,50);
            g.drawString("Month",85, 50);
            g.drawString("Day", 150, 50);
            g.drawString("Hour", 10, 140);
            g.drawString("Minute", 75, 140);
        }

        public void clearInputs() {
            yearOptions.setSelectedIndex(0);
            monthOptions.setSelectedIndex(0);
            dayOptions.setSelectedIndex(0);
            hourOptions.setSelectedIndex(0);
            minuteOptions.setSelectedIndex(0);
            setLocation.setText("");
            direction.setSelectedIndex(0);
            flightName.setText("");
            flightCompany.setText("");
            yearOptions.setBorder(BorderFactory.createEmptyBorder());
            monthOptions.setBorder(BorderFactory.createEmptyBorder());
            dayOptions.setBorder(BorderFactory.createEmptyBorder());
            hourOptions.setBorder(BorderFactory.createEmptyBorder());
            minuteOptions.setBorder(BorderFactory.createEmptyBorder());
            setLocation.setBorder(new JTextField().getBorder());
            direction.setBorder(BorderFactory.createEmptyBorder());
            flightName.setBorder(new JTextField().getBorder());
            flightCompany.setBorder(new JTextField().getBorder());
        }
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
            Stack<Flight> arriveList = arrivals.saveTreeStack();
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
            Stack<Flight> departList = departures.saveTreeStack();
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
        Stack<Flight> arrivalStack = arrivals.saveTreeStack();
        Stack<Flight> departStack = departures.saveTreeStack();
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
        Stack<Flight> arrivalStack = arrivals.saveTreeStack();
        Stack<Flight> departStack = departures.saveTreeStack();
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
     * Removes a flight from the database
     * @param flight The flight name
     * @return Boolean value indicating success or failure
     */
    public static boolean removeFlight(String flight) {
        Stack<Flight> arrivalStack = arrivals.saveTreeStack();
        Stack<Flight> departStack = departures.saveTreeStack();
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

    public static void refreshFlights(){
        Stack<Flight> arrivalStack = arrivals.saveTreeStack();
        Stack<Flight> departStack = departures.saveTreeStack();
        String currentTime;

    }


    private class testScroll extends JScrollPane {
        testScroll() {
            JTextArea test = new JTextArea(100,100);

            JScrollPane scrollFrame = new JScrollPane(test);
            scrollFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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