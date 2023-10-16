package edu.ntnu.stud;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class TrainDispatchApp {
    public static void main(String[] args) {
      TrainDepartureUI UI = new TrainDepartureUI();
      UI.init();
      UI.start();
    }
}
