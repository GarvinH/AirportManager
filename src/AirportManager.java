/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


public class AirportManager extends JFrame {
    static JFrame window;
    JPanel flightEditor;

    public static void main(String[] args) {
        window = new AirportManager();
        SortBTree<Flight> flights = new SortBTree<>();
    }

    AirportManager() {
        super("Flight Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(640, 480);
        //getContentPane().setLayout(null);

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
        AirportPanel() {
            setLayout(null);
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();

            String[] yearStrings = {"-", Integer.toString(year), Integer.toString(year+1)};
            String[] monthStrings = new String[13];
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

            dayOptions = new JComboBox(dayStrings);
            monthOptions = new JComboBox(monthStrings);
            monthOptions.addActionListener(this);
            yearOptions = new JComboBox(yearStrings);
            yearOptions.addActionListener(this);

            JTextField setLocation = new JTextField();

            yearOptions.setBounds(10,60,55,25);
            monthOptions.setBounds(85, 60, 40, 25);
            dayOptions.setBounds(150,60,40,25);
            //setLocation.setBounds(10, 120, 80, 25);

            add(yearOptions);
            add(monthOptions);
            add(dayOptions);
            //add(setLocation);
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
            //g.drawString("Location", 10, 110);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Year",10,50);
            g.drawString("Month",85, 50);
            g.drawString("Day", 150, 50);
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