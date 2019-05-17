/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

//imports
//java.awt
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
//javax.swing
import javax.swing.*;
//java.io
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
//java.time
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
//java.lang
import java.lang.Long;
//java.util
import java.util.Arrays;
import java.util.ArrayList;

public class AirportManager extends JFrame {
    static JFrame window;//Main frame used by airport
    static SortBTree arrivals, departures;//trees to store flights

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        window = new AirportManager();
    }

    /**
     * Constructor for the airport manager to initiate GUI
     * @Author Garvin Hui
     */
    AirportManager() {
        super("Flight Editor");//Title for frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {//save when the frame is closed
                super.windowClosing(e);
                saveFile();
            }
        });
        this.setSize(640, 480);
        this.setResizable(false);

        arrivals = new SortBTree<>();
        departures = new SortBTree<>();
        loadFile();//loads files in when program runs

        add(new AirportTabs());

        this.setVisible(true);
    }

    /***************************************************INNER CLASSES**************************************************/

    /**
     * this class stores each panel for the main frame (add and edit flights)
     * @author Garvin Hui
     */
    private class AirportTabs extends JTabbedPane {
        AirportTabs() {
            addTab("Add a Flight", new AddPanel());
            addTab("Remove a Flight", new EditPanel());
        }
    }

    /**
     * This panel is GUI for the editing flights panel
     * @Author Garvin Hui
     */
    private class EditPanel extends JPanel {
        JScrollPane arrivalPane;//stores all arriving flights into a scroll pane
        JScrollPane departPane;//stores all departing flights into another scroll panel
        JList arriveList;//stores all arriving flights as String value
        JList departList;//stores all departing flights as String Value
        int arriveSize;//This and departSize are used to compare with arrivals/departures tree size to check when to update the lists
        int departSize;
        JFrame editFrame;//secondary frame for editing or viewing flight info
        JPanel changePanel;//the panel used for editFrame
        PriorityQueue<Flight> itemPQueue;//stores temporarily the arriving or departing flights by date
        ArrayList<String> arriveNames;//This and departNames stores flight info as String to be displayed onto screen
        ArrayList<String> departNames;
        DefaultListModel<String> arriveModel;//this and departModel saves arriveNames/departNames into a model to be used by list
        DefaultListModel<String> departModel;
        EditPanel() {
            setLayout(null);

            JButton viewEdit = new JButton("Edit/View");

            //this size is used to compare to tree size to determine if GUI needs a refresh
            arriveSize = arrivals.size();
            departSize = departures.size();

            //this is used to store the string that will be displayed onto the GUI list
            arriveNames = new ArrayList<String>();
            departNames = new ArrayList<String>();

            //this is used by JList to follow a set of elements
            arriveModel = new DefaultListModel<String>();
            departModel = new DefaultListModel<String>();

            //setting up arrival list
            arrivalPane = new JScrollPane();
            arriveList = new JList(arriveModel);
            arriveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//allows only 1 flight to be selected at a given time
            arriveList.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (!departList.isSelectionEmpty()) {
                        departList.clearSelection();//allows only 1 flight to be selected at a given time
                    }
                }
            });
            arriveList.setFont(new Font("monospaced", Font.PLAIN, 12));
            arrivalPane.setViewportView(arriveList);//setting scrollpane to display list

            //setting up depature list
            departPane = new JScrollPane();
            departList = new JList(departModel);
            departList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//allows only 1 flight to be selected at a given time
            departList.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (!arriveList.isSelectionEmpty()) {
                        arriveList.clearSelection();//allows only 1 flight to be selected at a given time
                    }
                }
            });
            departList.setFont(new Font("monospaced", Font.PLAIN, 12));
            departPane.setViewportView(departList);

            //setting where components will be on screen
            arrivalPane.setBounds(10, 30, 600, 130);
            departPane.setBounds(10, 200,600,130);
            viewEdit.setBounds(500, 350, 100, 40);

            viewEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (!arriveList.isSelectionEmpty()) {
                        //using the flight info displayed on GUI, find the flight to be edited from arrivals
                        editFrame = new JFrame();
                        PriorityQueue readIn = savePQueue(arrivals);
                        Flight tempFlight = (Flight)readIn.dequeue();
                        while (!tempFlight.getName().equals(arriveList.getSelectedValue().toString().substring(0,arriveList.getSelectedValue().toString().indexOf(" ")))) {
                            tempFlight = (Flight)readIn.dequeue();
                        }
                        //creating secondary GUI and hiding primary GUI
                        changePanel = new ChangePanel(tempFlight.getDate().substring(0,4), tempFlight.getDate().substring(5,7), tempFlight.getDate().substring(8,10), tempFlight.getTime().substring(0,2), tempFlight.getTime().substring(2,4), tempFlight.getLocation(), "Arriving", tempFlight.getName(), tempFlight.getAirline(), tempFlight.getStatus());
                        editFrame.setVisible(true);
                        editFrame.add(changePanel);
                        editFrame.setSize(640,480);
                        editFrame.setResizable(false);
                        editFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        window.setVisible(false);
                    } else if (!departList.isSelectionEmpty()) {
                        //using the flight info displayed on GUI, find the flight to be edited from departures
                        editFrame = new JFrame();
                        PriorityQueue readIn = savePQueue(departures);
                        Flight tempFlight = (Flight)readIn.dequeue();
                        while (!tempFlight.getName().equals(departList.getSelectedValue().toString().substring(0,departList.getSelectedValue().toString().indexOf(" ")))) {
                            tempFlight = (Flight)readIn.dequeue();
                        }
                        //creating secondary GUI and hiding primary GUI
                        changePanel = new ChangePanel(tempFlight.getDate().substring(0,4), tempFlight.getDate().substring(5,7), tempFlight.getDate().substring(8,10), tempFlight.getTime().substring(0,2), tempFlight.getTime().substring(2,4), tempFlight.getLocation(), "Departing", tempFlight.getName(), tempFlight.getAirline(), tempFlight.getStatus());
                        editFrame.setVisible(true);
                        editFrame.add(changePanel);
                        editFrame.setSize(640,480);
                        editFrame.setResizable(false);
                        editFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        window.setVisible(false);
                    }
                }
            });

            //these ensure that any loaded flights will be displayed at startup
            updateArrivals();
            updateDepartures();

            add(arrivalPane);
            add(departPane);
            add(viewEdit);
        }

        /**
         * Panel used by editWindow allowing for changes to flight to be made by GUI
         * @Author Garvin Hui
         */
        private class ChangePanel extends JPanel {
            String year;
            String month;
            String day;
            String hour;
            String minute;
            String location;
            String direction;
            String name;
            String company;
            String status;

            /**
             * Constructor that takes in the selected flight from list and creates GUI for user to view info and possibly make changes
             * @param year year of flight
             * @param month month of flight
             * @param day day of flight
             * @param hour hour of flight
             * @param minute minute of flight
             * @param location where flight is going
             * @param direction flight is arriving/departing
             * @param name flight name
             * @param company company of airlines
             * @param status on time/delayed/canceled
             * @Author Garvin Hui
             */
            ChangePanel(String year, String month, String day, String hour, String minute, String location, String direction, String name, String company, String status) {
                setLayout(null);
                this.year = year;
                this.month = month;
                this.day = day;
                this.hour = hour;
                this.minute = minute;
                this.location = location;
                this.direction = direction;
                this.name = name;
                this.company = company;
                this.status = status;

                JButton save = new JButton("Save/Close");
                save.setBounds(460,380,130,40);
                save.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {//if flight is cancelled, you can't edit; only close
                        window.setVisible(true);
                        editFrame.setVisible(false);
                        editFrame.dispose();
                    }
                });

                if (!status.equals("Cancelled")) {//only non-cancelled flights may be edited
                    String[] statusStrings = {"On Time", "Delayed", "Cancelled"};//if flight isn't cancelled, now you can edit flight
                    JComboBox stat = new JComboBox(statusStrings);
                    stat.setSelectedItem(status);
                    stat.setBounds(300,185,90,25);
                    for (ActionListener a: save.getActionListeners()) {//removing old actionListeners
                        save.removeActionListener(a);
                    }
                    save.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            LocalDateTime flightTime = LocalDateTime.of(Integer.parseInt(year), Month.of(Integer.parseInt(month)), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
                            LocalDateTime currentTime = LocalDateTime.now();
                            if (currentTime.isBefore(flightTime)) {//you can only edit a flight if the current time is not past the scheduled time
                                changeFlightStatus(name, (String) stat.getSelectedItem());//updating flight status
                                updateArrivals();
                                updateDepartures();
                            }//also ensures that attempting to modify a flight after deleted doesn't work
                            //closing off secondary frame and reopening main frame
                            window.setVisible(true);
                            editFrame.setVisible(false);
                            editFrame.dispose();
                        }
                    });
                    add(stat);
                }

                add(save);
            }

            /**
             * Refreshing screen and drawing strings onto panel (for ChangePanel)
             * @param g graphics
             * @Author Garvin Hui
             */
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setDoubleBuffered(true);
                repaint();
                //Titles
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("Date", 10 ,25 );
                g.drawString("Time", 10, 115);
                g.drawString("Location", 10, 210);
                g.drawString("Arrive/Depart", 10, 275);
                g.drawString("Flight Name", 300, 25);
                g.drawString("Flight Company", 300, 100);
                g.drawString("Status", 300, 170);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
                //info
                g.drawString("Year",10,50);
                g.drawString(year, 10, 70);
                g.drawString("Month",85, 50);
                g.drawString(month, 85,70);
                g.drawString("Day", 150, 50);
                g.drawString(day, 150,70);
                g.drawString("Hour", 10, 140);
                g.drawString(hour, 10, 160);
                g.drawString("Minute", 75, 140);
                g.drawString(minute, 75, 160);
                g.drawString(location, 10, 230);
                g.drawString(direction, 10, 295);
                g.drawString(name, 300, 45);
                g.drawString(company, 300, 120);
                if (status.equals("Cancelled")) {//draw this because the drop down menu does not appear if flight cancelled
                    g.drawString(status, 300, 190);
                }
                g.drawString("NOTE: Flights may not be edited", 300, 250);
                g.drawString("after current time has passed", 300, 270);
                g.drawString("time scheduled in flight manager OR", 300, 290);
                g.drawString("if flight has been set to \"Cancelled\"", 300, 310);
            }
        }

        /**
         * Refreshes and draws strings onto panel (for EditPanel)
         * @param g graphics
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setDoubleBuffered(true);
            repaint();

            //drawing current date/time on bottom left of the panel
            LocalDateTime currentTime = LocalDateTime.now();
            String currentDay;
            String currentHour;
            String currentMinute;
            if (currentTime.getDayOfMonth() < 10) {
                currentDay = "0"+Integer.toString(currentTime.getDayOfMonth());
            } else {
                currentDay = Integer.toString(currentTime.getDayOfMonth());
            }
            if (currentTime.getHour() < 10) {
                currentHour = "0"+Integer.toString(currentTime.getHour());
            } else {
                currentHour = Integer.toString(currentTime.getHour());
            }
            if (currentTime.getMinute() < 10) {
                currentMinute = "0"+Integer.toString(currentTime.getMinute());
            } else {
                currentMinute = Integer.toString(currentTime.getMinute());
            }

            //auto removes flights when time is past
            autoUpdate();

            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Arrivals", 280, 25);
            g.drawString("Departures", 260, 190);
            //draws current date and time
            g.drawString(currentTime.getYear() +"/"+ currentTime.getMonth() +"/"+ currentDay +"  "+ currentHour+":"+currentMinute, 10, 400);

            //update either JLists if size of list doesn't match size of tree size
            if (arriveSize != arrivals.size()) {//if sizes don't match for either departures or arrivals, update list for GUI
                updateArrivals();
            }

            if (departSize != departures.size()) {
                updateDepartures();
            }
        }

        /**
         * updates the list for arrivals for GUI on EditPanel
         * @Author Garvin Hui
         */
        private void updateArrivals() {
            itemPQueue = savePQueue(arrivals);
            Flight tempFlight = itemPQueue.dequeue();
            arriveModel.removeAllElements();//recreate a new list every time (flight could be added in middle priority)
            arriveSize = 0;//reset size of list
            for (int i = 0; i < arrivals.size(); i++) {
                arriveNames.add(String.format("%-8s", tempFlight.getName()) + tempFlight.getDate() + "  " + tempFlight.getTime().substring(0, 2) + ":" + tempFlight.getTime().substring(2, 4) + "  " + String.format("%-11s", tempFlight.getStatus()) + tempFlight.getLocation());
                arriveModel.addElement(arriveNames.get(arriveNames.size() - 1));
                arriveSize++;
                tempFlight = itemPQueue.dequeue();
            }
        }

        /**
         * updates the list for departures for GUI on EditPanel
         * @Author Garvin Hui
         */
        private void updateDepartures() {
            //save a tree as a priority queue and add flights
            itemPQueue = savePQueue(departures);
            Flight tempFlight = itemPQueue.dequeue();
            departModel.removeAllElements();
            departSize = 0;
            for (int i = 0; i < departures.size(); i++) {
                //adding values to list to be displayed onto GUI
                departNames.add(String.format("%-8s", tempFlight.getName()) + tempFlight.getDate() + "  " + tempFlight.getTime().substring(0,2) + ":" + tempFlight.getTime().substring(2,4) + "  " + String.format("%-11s", tempFlight.getStatus()) + tempFlight.getLocation());
                departModel.addElement(departNames.get(departNames.size()-1));
                departSize++;
                tempFlight = itemPQueue.dequeue();
            }
        }

    }

    /**
     * this class is a panel for adding flights with GUI
     * @Author Garvin Hui
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
         * Constructor to make panel visible
         * @Author Garvin Hui
         */
        AddPanel() {
            setLayout(null);
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();

            //creating strings for JComboBox to be used by JComboBox
            String[] yearStrings = {"-", Integer.toString(year), Integer.toString(year+1)};
            String[] monthStrings = new String[13];
            String[] directionStrings = {"-","Arriving", "Departing"};
            String[] statusStrings = {"-", "On Time", "Delayed", "Cancelled"};
            monthStrings[0] = "-";
            for (int i = 1; i < 13; i++) {
                if (i < 10) {
                    monthStrings[i] = "0"+Integer.toString(i);
                } else {
                    monthStrings[i] = Integer.toString(i);
                }

            }
            String[] dayStrings = new String[32];
            dayStrings[0] = "-";
            for (int i = 1; i < 32; i++) {
                if (i <10) {
                    dayStrings[i] = "0"+Integer.toString(i);
                } else {
                    dayStrings[i] = Integer.toString(i);
                }
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
            //String creation for JComboBox complete

            dayOptions = new JComboBox(dayStrings);
            monthOptions = new JComboBox(monthStrings);
            yearOptions = new JComboBox(yearStrings);
            hourOptions = new JComboBox(hourStrings);
            minuteOptions = new JComboBox(minuteStrings);
            direction  = new JComboBox(directionStrings);
            status = new JComboBox(statusStrings);
            JButton addFlight = new JButton("Add Flight");//this button adds flights
            JButton clear = new JButton("Clear");//this button clears GUI
            JLabel existing = new JLabel("Flight name already taken!");
            existing.setFont(new Font("Arial", Font.PLAIN, 14));
            existing.setForeground(Color.RED);

            monthOptions.addActionListener(this);
            yearOptions.addActionListener(this);
            addFlight.addActionListener(new ActionListener() {
                @Override
                /**
                 * actionPerformed method overridden and checks that all fields and boxes have been selected before a flight can be added
                 * Also makes boxes highlight red if incomplete
                 * @Author Garvin Hui
                 */
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
                        //make sure that flight name is formatted properly or don't add flight
                        boolean formatted = true;
                        String tempName = flightName.getText();
                        tempName = tempName.toUpperCase();
                        if (tempName.length() <= 6) {
                            for (int i = 0; i < tempName.length(); i++) {
                                if (i < 2) {
                                    if ((tempName.charAt(i) < 'A') || (tempName.charAt(i) > 'Z')) {
                                        formatted = false;
                                    }
                                } else {
                                    if ((tempName.charAt(i) < '0') || (tempName.charAt(i) > '9')) {
                                        formatted = false;
                                    }
                                }
                            }
                        } else {
                            formatted = false;
                        }
                        if (formatted == false) {
                            add = false;
                            flightName.setBorder(BorderFactory.createLineBorder(Color.RED));
                        } else {
                            //make sure that the flight name isn't already used before adding
                            boolean exists = false;
                            PriorityQueue<Flight> readIn = savePQueue(arrivals);
                            Flight tempFlight = readIn.dequeue();
                            while((tempFlight != null) && (exists == false)) {
                                if (tempFlight.getName().toUpperCase().equals(flightName.getText().toUpperCase())) {
                                    exists = true;
                                } else {
                                    tempFlight = readIn.dequeue();
                                }
                            }
                            readIn = savePQueue(departures);
                            tempFlight = readIn.dequeue();
                            while ((tempFlight != null) && ( exists == false)) {
                                if (tempFlight.getName().toUpperCase().equals(flightName.getText().toUpperCase())) {
                                    exists = true;
                                } else {
                                    tempFlight = readIn.dequeue();
                                }
                            }
                            if (!exists) {
                                flightName.setBorder(new JTextField().getBorder());
                                existing.setVisible(false);//hide popup explaining why flight won't add
                            } else {
                                add = false;
                                flightName.setBorder(BorderFactory.createLineBorder(Color.RED));
                                existing.setVisible(true);//show popup to explain why flight won't add
                            }
                        }
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

                    if (add) {//adding flights here
                        if (direction.getSelectedItem().equals("Arriving")) {
                            arrivals.add(new Flight(flightName.getText().toUpperCase(), flightCompany.getText(), setLocation.getText(), yearOptions.getSelectedItem()+"/"+monthOptions.getSelectedItem()+"/"+dayOptions.getSelectedItem(), (String)hourOptions.getSelectedItem()+minuteOptions.getSelectedItem(), (String)status.getSelectedItem()));
                            clearInputs();//resetting inputs after flight is added
                        } else {
                            departures.add(new Flight(flightName.getText().toUpperCase(), flightCompany.getText(), setLocation.getText(), yearOptions.getSelectedItem()+"/"+monthOptions.getSelectedItem()+"/"+dayOptions.getSelectedItem(), (String)hourOptions.getSelectedItem()+minuteOptions.getSelectedItem(), (String)status.getSelectedItem()));
                            clearInputs();
                        }
                    }
                }
            });
            clear.addActionListener(new ActionListener() {
                @Override
                /**
                 * Method overriding makes button clear GUI
                 * @Author Garvin Hui
                 */
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

            existing.setBounds(400, 40, 200, 25);
            existing.setVisible(false);//becomes visible later if user tries to add flight that already exists

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
            add(existing);
        }

        @Override
        /**
         * Overriding JComboBoxes checks for changes in year, month or date
         * This allows for dynamic calendars (leap years, and day lengths matching each month)
         * @Author Garvin Hui
         */
        public void actionPerformed(ActionEvent e) {
            //These strings are used to for JComboBox models to be used by JComboBoxes
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
                    if (i < 10) {
                        febDates[i] = "0"+Integer.toString(i);
                    } else {
                        febDates[i] = Integer.toString(i);
                    }
                }
                if (i < leapDates.length) {
                    if (i < 10) {
                        leapDates[i] = "0"+Integer.toString(i);
                    } else {
                        leapDates[i] = Integer.toString(i);
                    }
                }
                if (i < shortDates.length) {
                    if (i < 10) {
                        shortDates[i] = "0"+Integer.toString(i);
                    } else {
                        shortDates[i] = Integer.toString(i);
                    }
                }
                if (i < 10) {
                    longDates[i] = "0"+Integer.toString(i);
                } else {
                    longDates[i] = Integer.toString(i);
                }
            }
            DefaultComboBoxModel monthLength;
            //end of string creation for use by combo box model

            JComboBox source = (JComboBox)e.getSource();

            //dynamically changes day lengths to match each month
            if (source.equals(yearOptions)) {//if changes in year is made, reset month and day
                monthOptions.setSelectedItem("-");
                dayOptions.setSelectedItem("-");
            } else if (source.equals(monthOptions)) {//checking if month belongs to month with 31 days
                if (!monthOptions.getSelectedItem().equals("-")) {
                    int monthNumber = Integer.parseInt((String) monthOptions.getSelectedItem());
                    dayOptions.setSelectedItem("-");
                    if (Arrays.asList(longMonths).contains(monthNumber)) {
                        monthLength = new DefaultComboBoxModel(longDates);
                        dayOptions.setModel(monthLength);
                    } else {
                        dayOptions.setSelectedItem("-");
                        if (monthNumber == 2) {//if february
                            if (!yearOptions.getSelectedItem().equals("-")) {
                                if (Integer.parseInt((String) yearOptions.getSelectedItem()) % 4 == 0) {//if leap year
                                    monthLength = new DefaultComboBoxModel(leapDates);
                                    dayOptions.setModel(monthLength);
                                } else {//if not leapyear
                                    monthLength = new DefaultComboBoxModel(febDates);
                                    dayOptions.setModel(monthLength);
                                }
                            }
                        } else {//month with 30 days
                            monthLength = new DefaultComboBoxModel(shortDates);
                            dayOptions.setModel(monthLength);
                        }
                    }
                }
            }
        }

        /**
         * Displays GUI for addPanel for adding flights
         * @param g
         * @Author Garvin Hui
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
            g.drawString("NOTE: Flight names but be for format", 300, 250);
            g.drawString("LLN - LLNNNN where", 300, 270);
            g.drawString("L is a letter and N is digit", 300, 290);
        }

        /**
         * This method resets all text fields and JComboBoxes
         * Method also restores all highlights of boxes to normal
         * @Author Garvin Hui
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
            status.setSelectedIndex(0);
            yearOptions.setBorder(BorderFactory.createEmptyBorder());
            monthOptions.setBorder(BorderFactory.createEmptyBorder());
            dayOptions.setBorder(BorderFactory.createEmptyBorder());
            hourOptions.setBorder(BorderFactory.createEmptyBorder());
            minuteOptions.setBorder(BorderFactory.createEmptyBorder());
            setLocation.setBorder(new JTextField().getBorder());
            direction.setBorder(BorderFactory.createEmptyBorder());
            flightName.setBorder(new JTextField().getBorder());
            flightCompany.setBorder(new JTextField().getBorder());
            status.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    /**
     * Takes in arrival or departure to convert into stack then converts into priority queue ordering flights by date
     * @param tree takes in either arrival or departure tree
     * @return priority queue of flights in order by date and time
     */
    public PriorityQueue savePQueue(SortBTree tree) {
        //create priority queue by traversing through stack
        PriorityQueue<Flight> newQueue = new PriorityQueue<Flight>();
        Stack<Flight> readIn = tree.saveTreeStack();
        Flight tempFlight = readIn.pop();
        String priorityString;
        long priority;
        while (tempFlight != null) {
            //priority is a long value according to date of flight: YYYYMMDDHHMM
            priorityString = tempFlight.getDate().replace("/","");
            priorityString += tempFlight.getTime();
            priority = Long.parseLong(priorityString);
            newQueue.enqueue(tempFlight, priority);
            tempFlight = readIn.pop();
        }
        return newQueue;
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
}