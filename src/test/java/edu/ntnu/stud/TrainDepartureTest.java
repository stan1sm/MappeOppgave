package edu.ntnu.stud;

import java.time.DateTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class TrainDepartureTest {

    private TrainDeparture trainDeparture;

    @BeforeEach
    void setUp() {
        trainDeparture = new TrainDeparture(LocalTime.of(12,0), "L1", "Trondheim", 1,  LocalTime.of(0, 0));
    }

    private void createTrainWithNullDepartureTime() {
        LocalTime invalidDepartureTime = LocalTime.of(0, 0);
        new TrainDeparture(invalidDepartureTime, "L1", "Trondheim", 1, 0, LocalTime.of(0, 0));
    }

    private void createTrainWithNullLine() {
        new TrainDeparture(LocalTime.of(12, 0), null, "Trondheim", 1, 0, LocalTime.of(0, 0));
    }

    private void createTrainWithNullDestination() {
        new TrainDeparture(LocalTime.of(12, 0), "L1", null, 1, 0, LocalTime.of(0, 0));
    }

    @Test
    void getDepartureTime() {
        assertEquals(LocalTime.of(12, 0), trainDeparture.getDepartureTime());
    }

    @Test
    void getLine() {
        assertEquals("L1", trainDeparture.getLine());
    }

    @Test
    void getTrainNumber() {
        assertEquals(1, trainDeparture.getTrainNumber());
    }

    @Test
    void getDestination() {
        assertEquals("Trondheim", trainDeparture.getDestination());
    }

    @Test
    void getTrack() {
        assertEquals(-1, trainDeparture.getTrack());
    }

    @Test
    void getDelay() {
        assertEquals(LocalTime.of(0, 0), trainDeparture.getDelay());
    }

    @Test
    void setTrack() {
        trainDeparture.setTrack(1);
        assertEquals(1, trainDeparture.getTrack());
    }

    @Test
    void setTrackThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> trainDeparture.setTrack(-2));
    }

    @Test
    void setDelay() {
        trainDeparture.setDelay(LocalTime.of(0, 1));
        assertEquals(LocalTime.of(0, 1), trainDeparture.getDelay());
    }

    @Test
    void testSetDelayValidTime() {
        assertDoesNotThrow(() -> trainDeparture.setDelay(LocalTime.of(0, 1)));
        assertEquals(LocalTime.of(0, 1), trainDeparture.getDelay());
    }

    @Test
    void testSetDelayInvalidTime() {
        LocalTime invalidTime = LocalTime.of(0, 0);
        DateTimeException exception = assertThrows(DateTimeException.class, () -> trainDeparture.setDelay(invalidTime));
        assertEquals("Cannot update delay to 00:00", exception.getMessage());
    }

    @Test
    void getDepartureTimeWithDelay() {
        trainDeparture.setDelay(LocalTime.of(0, 1));
        assertEquals(LocalTime.of(12, 1), trainDeparture.getDepartureTimeWithDelay());
    }


    @Test
    void constructorThrowsExceptionForNullDepartureTime() {
        assertThrows(NullPointerException.class, this::createTrainWithNullDepartureTime);
    }


    @Test
    void constructorThrowsExceptionForNullLine() {
        assertThrows(NullPointerException.class, this::createTrainWithNullLine);
    }

    @Test
    void constructorThrowsExceptionForNullDestination() {
        assertThrows(NullPointerException.class, this::createTrainWithNullDestination);
    }


    @Test
    void testForLongDelays() {
        trainDeparture.setDelay(LocalTime.of(13, 0));
        assertEquals(LocalTime.of(1, 0), trainDeparture.getDepartureTimeWithDelay());
    }

}
