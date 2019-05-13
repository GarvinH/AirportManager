/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

//imports
// java.awt
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
// javax.swing
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
// java.io
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
// java.util and java.time
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;



public class AirportManager extends JFrame {
    static JFrame window;
    JPanel flightEditor;
    static SortBTree arrivals, departures; // trees to store the flights

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        window = new AirportManager();
        arrivals.add(new Flight("SA1", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        arrivals.add(new Flight("AC2", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        departures.add(new Flight("CA3", "United", "San Francisco", "2019/04/04", "1700", "Delayed"));
        departures.add(new Flight("ZA4", "United", "San Francisco", "2019/04/04", "1600", "Delayed"));
        //saveFile();
        //loadFile();

//        Flight tempFlight = test.pop();
//        System.out.println(tempFlight.getStatus());
//        System.out.println(changeFlightStatus(tempFlight.getName(), "Arrived"));
//        test = arrivals.saveTreeStack();
//        tempFlight = test.pop();
//        System.out.println(tempFlight.getStatus());
//        System.out.println(test.pop().getName());
//        test = departures.saveTreeStack();
//        System.out.println(test.pop().getName());
//        System.out.println(test.pop().getName());


    }

    /**
     * Constructor for the airport manager to initiate GUI
     * @author Garvin Hui
     */
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

        arrivals = new SortBTree<>();
        departures = new SortBTree<>();

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

        //this.add(new JScrollPane(new JTextField("Hello Wolrd \nt\nt\nt\nt\nt\nt\nt\nt\nt\ntt\nt\nt\nasdlkfjapjjgaporiaigp")));
        //use jlist

        this.setVisible(true);
    }

    /***************************************************INNER CLASSES**************************************************/

    /*
     * @author Garvin Hui
     */
    private class AirportTabs extends JTabbedPane {
        AirportTabs() {
            addTab("Add a Flight", flightEditor);
            addTab("Remove a Flight", new EditPanel());
        }
    }

    /*
     * @author Garvin Hui
     */
    private class EditPanel extends JPanel {
        JScrollPane arrivalPane;
        JScrollPane departPane;
        JList arriveList;
        JList departList;
        int arriveSize;
        int departSize;
        EditPanel() {
            setLayout(null);
            ArrayList<String> arriveNames = new ArrayList<String>();
            ArrayList<String> departNames = new ArrayList<String>();
            Stack<Flight> readIn = arrivals.saveTreeStack();
            Flight tempFlight = readIn.pop();
            while (tempFlight != null) {
                arriveNames.add(tempFlight.getName());
                tempFlight = readIn.pop();
            }
            readIn = departures.saveTreeStack();
            tempFlight = readIn.pop();
            while (tempFlight != null) {
                departNames.add(tempFlight.getName());
                tempFlight = readIn.pop();
            }

            arrivalPane = new JScrollPane();
            arriveList = new JList(arriveNames.toArray());
            arrivalPane.setViewportView(arriveList);
            departPane = new JScrollPane();
            departList = new JList(departNames.toArray());
            departPane.setViewportView(departList);

            arrivalPane.setBounds(10, 30, 80, 80);
            departPane.setBounds(10,150,80,80);

            add(arrivalPane);
            add(departPane);
        }

        /**
         * Displays GUI
         * @author Garvin Hui
         * @param g
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setDoubleBuffered(true);
            repaint();
        }


    }

    /*
     * @author Garvin Hui
     */
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

        /**
         * Constructor for panel of adding flights
         */
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
        /**
         *
         */
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

        /**
         *
         * @param g
         */
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

        /**
         *
         */
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
     * Loads up trees from a serialized file and stores the data into trees
     * @author Albert Quon
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
     * Saves the current trees on to serialized files
     * @author Albert Quon
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
     * @author Albert Quon
     * @return boolean value if flight is valid
     */
    public static boolean changeFlightStatus(String flight, String status) {
        Stack<Flight> arrivalStack = arrivals.saveTreeStack();
        Stack<Flight> departStack = departures.saveTreeStack();
        Flight tempFlight;
        //search for the flight by name in both stacks
        do {
            tempFlight = arrivalStack.pop();
        } while (tempFlight != null && !tempFlight.getName().equalsIgnoreCase(flight));

        //change the status
        if (tempFlight != null) {
            ((Flight)arrivals.getItem(tempFlight)).setStatus(status);
            return true;
        } else {
            do {
                tempFlight = departStack.pop();
            } while (tempFlight != null && !tempFlight.getName().equalsIgnoreCase(flight));
            if (tempFlight != null) {
                ((Flight)departures.getItem(tempFlight)).setStatus(status);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a flight from the tree, as direct removal
     * @param flight The flight name
     * @author Albert Quon
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
        } else { // departures may have the flight
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

    /**
     * Automatically removes flights that do not need to be shown
     * @author Albert Quon
     */
    public static void autoUpdate() {
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        Flight tempFlight;
        Stack<Flight> arrivalStack = arrivals.saveTreeStack();
        Stack<Flight> departStack = departures.saveTreeStack();
        String flightDate;
        int flightDay, flightMonth, flightYear, flightTime;
        int arrivalsSize = arrivals.size();
        int departSize = departures.size();
        int currentYear = localDate.getYear();
        int currentMonth = localDate.getMonthValue();
        int currentDay = localDate.getDayOfMonth();
        int currentTime = localTime.getHour() * 100 + localTime.getMinute();

        //arrivals
        for(int i = 0; i < arrivalsSize; i++) {
            tempFlight = arrivalStack.pop();
            flightDate = tempFlight.getDate();
            flightTime = Integer.parseInt(tempFlight.getTime());

            // check year
            flightYear = Integer.parseInt(flightDate.substring(0,flightDate.indexOf("/")));
            if (flightYear < currentYear) {
                arrivals.remove(tempFlight);
            } else if (currentYear - flightYear == 0) { // same year
                // check month
                flightMonth = Integer.parseInt(flightDate.substring(flightDate.indexOf("/") + 1, flightDate.lastIndexOf("/")));
                if (flightMonth < currentMonth) {
                    arrivals.remove(tempFlight);
                } else if (currentMonth - flightMonth == 0){ // same month
                    // check day
                    flightDay = Integer.parseInt(flightDate.substring(flightDate.lastIndexOf("/")+1));
                    if (flightDay < currentDay) {
                        arrivals.remove(tempFlight);
                    } else if (currentDay - flightDay == 0){ // same day
                        // check time
                        // delayed flights need 3 hours passed to be removed
                        if (tempFlight.getStatus().equalsIgnoreCase("delayed")) {
                            if (currentTime - flightTime > 300) {
                                arrivals.remove(tempFlight);
                            }
                        } else { // any other flights need 15 minutes passed to be removed
                            if (currentTime - flightTime > 15) {
                                arrivals.remove(tempFlight);
                            }
                        }

                    }
                }
            }
        }

        //departures
        for(int i = 0; i < departSize; i++) {
            tempFlight = departStack.pop();
            flightDate = tempFlight.getDate();
            flightTime = Integer.parseInt(tempFlight.getTime());

            // check year
            flightYear = Integer.parseInt(flightDate.substring(0,flightDate.indexOf("/")));
            if (flightYear < currentYear) {
                departures.remove(tempFlight);
            } else if (currentYear - flightYear == 0) { // same year
                // check month
                flightMonth = Integer.parseInt(flightDate.substring(flightDate.indexOf("/") + 1, flightDate.lastIndexOf("/")));
                if (flightMonth < currentMonth) {
                    departures.remove(tempFlight);
                } else if (currentMonth - flightMonth == 0){ // same month
                    // check day
                    flightDay = Integer.parseInt(flightDate.substring(flightDate.lastIndexOf("/")+1));
                    if (flightDay < currentDay) {
                        departures.remove(tempFlight);
                    } else if (currentDay - flightDay == 0){ // same day
                        // check time
                        // delayed flights need 3 hours passed to be removed
                        if (tempFlight.getStatus().equalsIgnoreCase("delayed")) {
                            if (currentTime - flightTime > 300) {
                                departures.remove(tempFlight);
                            }
                        } else { // any other flights need 15 minutes passed to be removed
                            if (currentTime - flightTime > 15) {
                                departures.remove(tempFlight);
                            }
                        }

                    }
                }
            }
        }


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


}