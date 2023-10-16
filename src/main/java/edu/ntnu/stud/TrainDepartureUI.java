package edu.ntnu.stud;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class TrainDepartureUI {
    Scanner input = new Scanner(System.in);
    Map<String, Runnable> options = new HashMap<>();
    public void init() {
        ArrayList<TrainDeparture> TrainDepartureList = new ArrayList<>();
        TrainFactory trainFactory = new TrainFactory();
        options.put("1", () -> {
            for(TrainDeparture trainDeparture : TrainDepartureList) {
                System.out.println(trainDeparture.toString());}
            });
        options.put("2", () -> {TrainDepartureList.add(trainFactory.addDeparture());});
        options.put("3", () -> {trainFactory.assignTrack();});
    }

    public void start() {
        while (true) {
            System.out.println("1. Overview of all departures");
            System.out.println("2. Add a Departure");
            System.out.println("3. Assign Track to Departure");
            System.out.println("4. Add Delay to departure");
            System.out.println("5. Search for departure with Train Number");
            System.out.println("6. Search for departure with Destination");
            System.out.println("7. Update System Time");
            System.out.println("8. Exit");
            String choice = input.nextLine();


            if (options.containsKey(choice)) {
                options.get(choice).run();
            } else {
                System.out.println("Invalid choice");
            }
        }
    }
}
