/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
        window = new AirportManager();
        SortBTree<Flight> flights = new SortBTree<>();
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

        flightEditor = new AirportPanel();
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

    private class AirportTabs extends JTabbedPane {
        AirportTabs() {
            addTab("Add a Flight", flightEditor);
            setMnemonicAt(0, KeyEvent.VK_1);
            addTab("Remove a Flight", new JPanel());
            setMnemonicAt(1,KeyEvent.VK_2);
        }
    }

    private class AirportPanel extends JPanel implements ActionListener {
        JComboBox yearOptions;
        JComboBox monthOptions;
        JComboBox dayOptions;
        JComboBox hourOptions;
        JComboBox minuteOptions;
        JTextField setLocation;
        JComboBox direction;
        JTextField flightName;
        JTextField flightCompany;
        AirportPanel() {
            setLayout(null);
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();

            String[] yearStrings = {"-", Integer.toString(year), Integer.toString(year+1)};
            String[] monthStrings = new String[13];
            String[] directionStrings = {"-","Arriving", "Departing"};
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
            JButton addFlight = new JButton("Add Flight");
            JButton clear = new JButton("Clear");

            monthOptions.addActionListener(this);
            yearOptions.addActionListener(this);
            addFlight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            clear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    yearOptions.setSelectedIndex(0);
                    monthOptions.setSelectedIndex(0);
                    dayOptions.setSelectedIndex(0);
                    hourOptions.setSelectedIndex(0);
                    minuteOptions.setSelectedIndex(0);
                    setLocation.setText("");
                    direction.setSelectedIndex(0);
                    flightName.setText("");
                    flightCompany.setText("");
                }
            });

            setLocation = new JTextField();
            flightName = new JTextField();
            flightCompany = new JTextField();

            yearOptions.setBounds(10,60,55,25);
            monthOptions.setBounds(85, 60, 40, 25);
            dayOptions.setBounds(150,60,40,25);
            hourOptions.setBounds(10, 150, 40, 25);
            minuteOptions.setBounds(75, 150, 40, 25);
            setLocation.setBounds(10, 220, 80, 25);
            direction.setBounds(10, 285, 85, 25);
            flightName.setBounds(300, 40, 80, 25);
            flightCompany.setBounds(300, 115, 80, 25);
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
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Year",10,50);
            g.drawString("Month",85, 50);
            g.drawString("Day", 150, 50);
            g.drawString("Hour", 10, 140);
            g.drawString("Minute", 75, 140);
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