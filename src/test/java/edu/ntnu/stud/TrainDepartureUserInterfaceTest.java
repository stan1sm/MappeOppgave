package edu.ntnu.stud;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainDepartureUserInterfaceTest {

    @BeforeEach
    void setUp() {

    }
    @Test
    void testTimeInputValidInput() {
        // Arrange
        String inputString = "12:30";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(inputStream);

        TrainDepartureUserInterface trainDepartureUI = new TrainDepartureUserInterface();

        // Act
        LocalTime result = trainDepartureUI.timeInput();

        // Assert
        assertEquals(LocalTime.of(12, 30), result);
    }

    @Test
    void testTimeInputInvalidThenValid() {
        // Arrange
        String inputString = "invalid\n12:30";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(inputStream);

        TrainDepartureUserInterface trainDepartureUI = new TrainDepartureUserInterface();

        // Act
        LocalTime result = trainDepartureUI.timeInput();

        // Assert
        assertEquals(LocalTime.of(12, 30), result);
    }

    @Test
    void testNumberInputValidInput(){
        int inputInt = 1;
        InputStream inputStream = new ByteArrayInputStream(String.valueOf(inputInt).getBytes());
        System.setIn(inputStream);

        TrainDepartureUserInterface trainDepartureUI = new TrainDepartureUserInterface();

        int result = trainDepartureUI.numberInput();

        assertEquals(inputInt, result);
    }

    @Test
    void testNumberInputInvalidThenValid(){
        String inputString = "invalid\n1";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(inputStream);

        TrainDepartureUserInterface trainDepartureUI = new TrainDepartureUserInterface();

        int result = trainDepartureUI.numberInput();

        assertEquals(1, result);
    }

    @Test
    void testTextInputValidInput(){
        String inputString = "valid";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(inputStream);

        TrainDepartureUserInterface trainDepartureUI = new TrainDepartureUserInterface();

        String result = trainDepartureUI.textInput();

        assertEquals(inputString, result);
    }

    @Test
    void testTextInputInvalidThenValid(){
        String inputString = "123\nvalid";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(inputStream);

        TrainDepartureUserInterface trainDepartureUI = new TrainDepartureUserInterface();

        String result = trainDepartureUI.textInput();

        assertEquals("valid", result);
    }

    @Test
    void testTrainNumberFromInput(){
        int inputInt = 1;
        InputStream inputStream = new ByteArrayInputStream(String.valueOf(inputInt).getBytes());
        System.setIn(inputStream);

        TrainDepartureUserInterface trainDepartureUI = new TrainDepartureUserInterface();

        int result = trainDepartureUI.trainNumberFromInput();

        assertEquals(inputInt, result);
    }
}
