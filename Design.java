//For the looks
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Design extends JFrame {
    private JTextArea outputArea;
    private JTextField arrivalField, burstField, priorityField;
    private JButton addButton, runButton;
    private JComboBox<String> algorithmBox;
    private DefaultListModel<String> processListModel;

    private int processCounter = 0;  //For keeping track of processes
    private ArrayList<Processes> processList = new ArrayList<>();

    //The Constructor
    public Design() {  
        setTitle("CPU Scheduler Simulator");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));  //The input panel
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        inputPanel.add(arrivalField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        inputPanel.add(burstField);

        inputPanel.add(new JLabel("Priority:"));
        priorityField = new JTextField();
        inputPanel.add(priorityField);

        inputPanel.add(new JLabel("Algorithm:"));
        algorithmBox = new JComboBox<>(new String[]{"FCFS", "SJF", "Round Robin", "Priority Scheduling"});
        inputPanel.add(algorithmBox);

        addButton = new JButton("Add Process");
        runButton = new JButton("Run Simulation");
        inputPanel.add(addButton);
        inputPanel.add(runButton);

        add(inputPanel, BorderLayout.NORTH);

        processListModel = new DefaultListModel<>();
        JList<String> processListView = new JList<>(processListModel);  //For displaying the list of prosesses 
        JScrollPane scrollPane = new JScrollPane(processListView);
        add(scrollPane, BorderLayout.CENTER);

        outputArea = new JTextArea();  //For displaying the results
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {  //To read the user input 
            public void actionPerformed(ActionEvent e) {
                try {
                    int arrival = Integer.parseInt(arrivalField.getText());
                    int burst = Integer.parseInt(burstField.getText());
                    int priority = Integer.parseInt(priorityField.getText());
                    Processes p = new Processes(processCounter++, arrival, burst, priority);
                    processList.add(p);
                    processListModel.addElement("P" + p.pid + " (Arrival: " + arrival + ", Burst: " + burst + ", Priority: " + priority + ")");
                    arrivalField.setText("");
                    burstField.setText("");
                    priorityField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid integers.");
                }
            }
        });

        runButton.addActionListener(new ActionListener() {   
            public void actionPerformed(ActionEvent e) {
                CPU_scheduler scheduler = new CPU_scheduler();
                scheduler.setProcesses(new ArrayList<Processes>(processList));  //Makes a list of the processes added 
                String selected = (String) algorithmBox.getSelectedItem();  //Runs the selected algorithm
                if ("FCFS".equals(selected)) scheduler.fcfs();
                else if ("SJF".equals(selected)) scheduler.sjf();
                else if ("Priority Scheduling".equals(selected)) scheduler.priorityScheduling();
                else {
                    String q = JOptionPane.showInputDialog("Enter time quantum:");
                    try {
                        scheduler.roundRobin(Integer.parseInt(q));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid quantum.");
                        return;
                    }
                }
                outputArea.setText(scheduler.getStats());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Design(); //The entry point 
    }
}

