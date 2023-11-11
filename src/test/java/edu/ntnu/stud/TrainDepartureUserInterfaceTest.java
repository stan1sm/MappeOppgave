package edu.ntnu.stud;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainDepartureUserInterfaceTest {

    private TrainDepartureUserInterface userInterface;
    private InputStream originalSystemIn;

    @BeforeEach
    void setUp() {
        userInterface = new TrainDepartureUserInterface();
        originalSystemIn = System.in;
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
    }

    @Test
    void addTrainDeparture_ValidInput_ShouldAddToTrainDepartureList() {
        String simulatedInput = "10:00\n00:15\nTestLine\nTestDestination\n123\n1\n";

        userInterface.addTrainDeparture();
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        assertEquals(1, userInterface.tableHeader().split("\n").length);
    }

    @Test
    void printDepartureOverview_ShouldPrintTableHeader() {
        userInterface.printDepartureOverview();

        assertEquals(1, userInterface.tableHeader().split("\n").length);
    }

    @Test
    void departureFromNumber_ValidTrainNumber_ShouldPrintDeparture() {
        String simulatedInput = "123\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        userInterface.departureFromNumber();

        assertEquals(1, userInterface.tableHeader().split("\n").length);
    }

    @Test
    void departureFromNumber_InvalidTrainNumber_ShouldPrintErrorMessage() {
        String simulatedInput = "invalid\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        userInterface.departureFromNumber();

        assertEquals(1, userInterface.tableHeader().split("\n").length);
    }
}
