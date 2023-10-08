package edu.ntnu.stud;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class TrainDispatchApp {
    public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      Map<String, Runnable> options = new HashMap<>();
      ArrayList<TrainDeparture> TrainDepartureList = new ArrayList<>();
      TrainFactory trainFactory = new TrainFactory();
      options.put("2", () -> {TrainDepartureList.add(trainFactory.addDeparture());});
        System.out.println("1. Overview of all departures");
        System.out.println("2. Add a Departure");
        System.out.println("3. Assign Track to Departure");
        System.out.println("4. Add delay to departure");
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
