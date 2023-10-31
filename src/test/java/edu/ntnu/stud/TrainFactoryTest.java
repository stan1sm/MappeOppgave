package edu.ntnu.stud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;


import java.io.ByteArrayInputStream;

public class TrainFactoryTest {

    private TrainFactory trainFactory;
    private TrainDeparture trainDeparture;

    @BeforeEach
    void setUp() {
        trainFactory = new TrainFactory();
        trainDeparture = new TrainDeparture(LocalTime.of(12,0), "L1", "Trondheim", 1,  LocalTime.of(0, 0));
        trainFactory.addDeparture(LocalTime.of(12,0), "L1", "Trondheim", 1, 1, LocalTime.of(0, 0));

    }

    @Test
    void addDeparture() {
        assertEquals(1, trainFactory.getTrainDepartureList().size());
    }

    @Test
    void assignTrack() {
        trainFactory.assignTrack(1, 2);
        assertEquals(2, trainFactory.departureFromNumber(1).getTrack());
    }

    @Test
    void addDelay() {
        trainFactory.addDelay(1, LocalTime.of(0, 10));
        assertEquals(LocalTime.of(0, 10), trainFactory.departureFromNumber(1).getDelay());
    }

    @Test
    void departureFromNumber() {
        assertEquals(trainDeparture.getTrainNumber(), trainFactory.departureFromNumber(1).getTrainNumber());
    }

    @Test
    void departureFromDestination() {
        assertEquals(trainDeparture, trainFactory.departureFromDestination("Trondheim"));
    }

    @Test
    void setCurrentTime() {
        trainFactory.setCurrentTime(LocalTime.of(12, 0));
        assertEquals(trainFactory.getCurrentTime(), LocalTime.of(12, 0));
    }

    @Test
    void fillTrainDepartureList() {
        trainFactory.fillTrainDepartureList();
        assertEquals(5, trainFactory.getTrainDepartureList().size());
    }
}
