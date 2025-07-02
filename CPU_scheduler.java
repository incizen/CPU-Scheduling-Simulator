//The brain class of the project
import java.util.*;

public class CPU_scheduler {
    private Scanner scanner = new Scanner(System.in); // For reading the user input from the keyboard
    private List<Processes> processes = new ArrayList<>(); // For the storage of the inputs

    public void setProcesses(List<Processes> newList) {
        this.processes = newList;
    }

    public void run() {
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println("Enter arrival and burst time " + i + ":");  //For the process
            int arrival = scanner.nextInt();
            int burst = scanner.nextInt(); // The cpu need time 
            int priority = scanner.nextInt();
            processes.add(new Processes(i, arrival, burst, priority));
        }

        System.out.println("Choose a Scheduling Algorithm:");
        System.out.println("1. First-Come-First-Serve (FCFS)");
        System.out.println("2. Shortest Job First (SJF)");
        System.out.println("3. Round Robin");
        System.out.println("4. Priority Scheduling");


int choice = scanner.nextInt();


// For the selected method 
        switch (choice) {
            case 1:
                fcfs();
                break;
            case 2:
                sjf();
                break;
            case 3:
                System.out.print("Enter the time quantum: ");
                int quantum = scanner.nextInt();
                roundRobin(quantum);
                break;
            default:
                System.out.println("Invalid pick.");
                case 4:
                   priorityScheduling();
                   break;

        }
    }

public void fcfs() {  // First come first serve 
    processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
    int currentTime = 0;

    for (Processes p : processes) {
        if (currentTime < p.arrivalTime) {
            currentTime = p.arrivalTime;
        }
        p.waitingTime = currentTime - p.arrivalTime;
        currentTime += p.burstTime;
        p.turnaroundTime = p.waitingTime + p.burstTime; 
    }

    printStats("FCFS");
}


    public void sjf() { //Shortest job first 
        List<Processes> completed = new ArrayList<>();
        int time = 0;
        while (completed.size() < processes.size()) {
            List<Processes> available = new ArrayList<>();
            for (Processes p : processes) {
                if (!completed.contains(p) && p.arrivalTime <= time) {
                    available.add(p);
                }
            }
            if (available.isEmpty()) {     // For collecting all processes not yet completed and choosing the one with the shortest burst time
                time++;
                continue;
            }
            available.sort(Comparator.comparingInt(p -> p.burstTime));
            Processes p = available.get(0);
            p.waitingTime = time - p.arrivalTime;
            time += p.burstTime;
            p.turnaroundTime = p.waitingTime + p.burstTime;
            completed.add(p);
        }
        processes = completed;
        printStats("SJF");
    }

public void roundRobin(int quantum) {  // Round robin
    Queue<Processes> queue = new LinkedList<>();
    List<Processes> readyList = new ArrayList<>(processes);
    readyList.sort(Comparator.comparingInt(p -> p.arrivalTime));

    int time = 0, index = 0;
    queue.add(readyList.get(index++));

    while (!queue.isEmpty()) {
        Processes current = queue.poll();   

        if (current.remainingTime > quantum) {
            current.remainingTime -= quantum;
            time += quantum;
        } else {
            time += current.remainingTime;
            current.remainingTime = 0;
            current.turnaroundTime = time - current.arrivalTime;
            current.waitingTime = current.turnaroundTime - current.burstTime;
        }

        while (index < readyList.size() && readyList.get(index).arrivalTime <= time) {
            queue.add(readyList.get(index++));
        }                                                   

        if (current.remainingTime > 0) {   // If its not finished, goes back in line 
            queue.add(current);
        }
    }

    printStats("Round Robin");
}

public void priorityScheduling() {  //Priority pics the one with the highest priority
    int currentTime = 0;
    List<Processes> completed = new ArrayList<>();

    while (completed.size() < processes.size()) {
        List<Processes> available = new ArrayList<>();
        for (Processes p : processes) {
            if (!completed.contains(p) && p.arrivalTime <= currentTime) {
                available.add(p);
            }
        }

        if (available.isEmpty()) {
            currentTime++;
            continue;
        }

        available.sort(Comparator.comparingInt(p -> p.priority));
        Processes current = available.get(0);

        current.waitingTime = currentTime - current.arrivalTime;
        currentTime += current.burstTime;
        current.turnaroundTime = current.waitingTime + current.burstTime;
        completed.add(current);
    }

    processes = completed;
    printStats("Priority Scheduling");
}

public String getStats() {
    StringBuilder sb = new StringBuilder();
    double totalWait = 0, totalTurnaround = 0;
    for (Processes p : processes) {
        sb.append("Process ").append(p.pid)
          .append(": Waiting Time = ").append(p.waitingTime)
          .append(", Turnaround Time = ").append(p.turnaroundTime).append("\n");
        totalWait += p.waitingTime;
        totalTurnaround += p.turnaroundTime;
    }
    sb.append(String.format("Average Waiting Time: %.2f\n", totalWait / processes.size()));
    sb.append(String.format("Average Turnaround Time: %.2f\n", totalTurnaround / processes.size()));
    return sb.toString();
}


    private void printStats(String algorithm) { // For printing results 
        System.out.println("\n" + algorithm + " Scheduling Results:");
        double totalWait = 0, totalTurnaround = 0;
        for (Processes p : processes) {
            System.out.println("Process " + p.pid + ": Waiting Time = " + p.waitingTime + ", Turnaround Time = " + p.turnaroundTime);
            totalWait += p.waitingTime;
            totalTurnaround += p.turnaroundTime;
        }
        System.out.printf("Average Waiting Time: %.2f\n", totalWait / processes.size());
        System.out.printf("Average Turnaround Time: %.2f\n", totalTurnaround / processes.size());
    }
}
