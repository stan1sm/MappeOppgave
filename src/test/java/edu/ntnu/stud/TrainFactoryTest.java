package edu.ntnu.stud;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

public class TrainFactoryTest {
    void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testAddDeparture() {
        provideInput("12:00\n" +
                "Line\n" +
                "Destination\n" +
                "123\n" +
                "1\n" +
                "00:00\n");
        TrainFactory trainFactory = new TrainFactory();
        trainFactory.addDeparture();
        assert trainFactory.getTrainDepartureList().size() == 1;
        assert trainFactory.getTrainNumberMap().size() == 1;
        assert trainFactory.getTrainDestinationMap().size() == 1;
    }
}
