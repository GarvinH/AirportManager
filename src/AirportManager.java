/* [AirportManager.java]
 * Flight management system for an airport
 * Albert Quon & Garvin Hui
 * 04/30/2019
 */

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;


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

    private class AirportPanel extends JPanel {
        JComboBox yearOptions;
        JComboBox monthOptions;
        JComboBox dayOptions;
        AirportPanel() {
            setLayout(null);
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();

            JTextArea dateTitle = new JTextArea("Date");
            dateTitle.setFont(new Font("Arial", Font.BOLD, 16));

            JTextArea dayTitle = new JTextArea("Day");
            JTextArea monthTitle = new JTextArea("Month");
            JTextArea yearTitle = new JTextArea("Year");

            String[] yearStrings = {"-", Integer.toString(year), Integer.toString(year+1)};
            String[] monthStrings = new String[13];
            monthStrings[0] = "-";
            for (int i = 1; i < 13; i++) {
                monthStrings[i] = Integer.toString(i);
            }
            ArrayList<String> dayStrings = new ArrayList<String>();
            dayStrings.add("-");
            for (int i = 1; i < 32; i++) {
                dayStrings.add(Integer.toString(i));
            }
            dayOptions = new JComboBox(dayStrings.toArray());
            monthOptions = new JComboBox(monthStrings);
            yearOptions = new JComboBox(yearStrings);

            dateTitle.setBounds(10,10,35,15);
            yearTitle.setBounds(10,35,30,15);
            yearOptions.setBounds(10,60,55,25);
            monthTitle.setBounds(85, 35, 35, 15);
            monthOptions.setBounds(85, 60, 40, 25);
            dayTitle.setBounds(150, 35, 25, 15);
            dayOptions.setBounds(150,60,40,25);
            //setSize(new Dimension(50,50));

            add(dateTitle);
            add(yearOptions);
            add(yearTitle);
            add(monthOptions);
            add(monthTitle);
            add(dayOptions);
            add(dayTitle);
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setDoubleBuffered(true);
            setLayout(null);

            if (!monthOptions.getSelectedItem().equals("-")) {
                int monthNumber = Integer.parseInt((String)monthOptions.getSelectedItem();
                if (monthNumber % 2 == 1) {
                    if (dayOptions.getItemCount() < 31) {
                        for (int i = 1; i < 32; i++) {
                            if (!dayOptions.getItemAt(i).equals(Integer.toString(i))) {
                                dayOptions.addItem(Integer.toString(i));
                            }
                        }
                    }
                } else {

                }
            }
            //System.out.println((String)yearOptions.getSelectedItem());
            //setLocation(50,50);
            //setBounds(50,50,50,50);
            /*g.setColor(Color.BLACK);
            g.fillRect(100,400, 200, 200);
            this.setPreferredSize(new Dimension(640,480));*/



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