package edu.ntnu.stud;

import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class TrainFactoryTest {
    private TrainFactory trainFactory;

    ArrayList<TrainDeparture> trainDepartureList;
    HashMap<Integer, TrainDeparture> numberToDepartureMap;

    @BeforeEach
    void setUp() {
        trainFactory = new TrainFactory();
        trainDepartureList = new ArrayList<>();
        numberToDepartureMap = new HashMap<>();

        trainFactory.addDeparture(LocalTime.of(12,0), "L1", "Trondheim", 1, 1, LocalTime.of(0, 0));
        trainFactory.addDeparture(LocalTime.of(10,0), "L2", "Stavanger", 2, 2, LocalTime.of(0, 0));
    }

    @Test
    void testAddDeparture() {
        assertEquals(2, trainFactory.getTrainDepartures().size());
    }

    @Test
    void testAssignTrack() {
        trainFactory.assignTrack(1, 2);
        assertEquals(2, trainFactory.departureFromNumber(1).getTrack());
    }

    @Test
    void testAddDelay() {
        trainFactory.addDelay(1, LocalTime.of(0, 10));
        assertEquals(LocalTime.of(0, 10), trainFactory.departureFromNumber(1).getDelay());
    }

    //TODO: FIX THIS TEST
    @Test
    void testDepartureFromNumber() {
        System.out.println(numberToDepartureMap);
        assertEquals(numberToDepartureMap.get(1), trainFactory.departureFromNumber(1));
    }

    @Test
    void testDepartureFromDestination() {
        List<TrainDeparture> foundDepartures = trainFactory.departureFromDestination("Trondheim");
        assertNotNull(foundDepartures);
        assertEquals(1, foundDepartures.size());
        assertEquals("Trondheim", foundDepartures.get(0).getDestination());
    }

    @Test
    void testSetCurrentTime() {
        trainFactory.setCurrentTime(LocalTime.of(12, 0));
        assertEquals(trainFactory.getCurrentTime(), LocalTime.of(12, 0));
    }

    @Test
    void testRemoveDeparted(){
        trainFactory.setCurrentTime(LocalTime.of(12, 30));
        trainFactory.removeDeparted();
        assertEquals(0, trainDepartureList.size());
        assertEquals(0, numberToDepartureMap.size());
    }

    @Test
    void testSortByDepartureTime(){
        trainFactory.sortByDepartureTime();
        assertEquals(LocalTime.of(10,0), trainFactory.getTrainDepartures().get(0).getDepartureTime());
    }

    @Test
    void testCheckLineExists(){
        assertTrue(trainFactory.checkLineExists("L1"));
        assertTrue(trainFactory.checkLineExists("L2"));
        assertFalse(trainFactory.checkLineExists("L3"));
    }

    @Test
    void testCheckTrackExists(){
        assertTrue(trainFactory.checkTrackExists(1));
        assertTrue(trainFactory.checkTrackExists(2));
        assertFalse(trainFactory.checkTrackExists(3));
    }

    @Test
    void testDepartureTimesFromLine(){
        ArrayList<LocalTime> foundDepartures = trainFactory.departureTimesFromLine("L1");
        assertNotNull(foundDepartures);
        assertEquals(1, foundDepartures.size());
        assertEquals(LocalTime.of(12, 0), foundDepartures.get(0));
        ArrayList<LocalTime> foundDepartures1 = trainFactory.departureTimesFromLine("L2");
        assertNotNull(foundDepartures1);
        assertEquals(1, foundDepartures1.size());
        assertEquals(LocalTime.of(10, 0), foundDepartures1.get(0));
    }

    @Test
    void testDepartureTimesFromTrack(){
        ArrayList<LocalTime> foundDepartures = trainFactory.departureTimesFromTrack(1);
        assertNotNull(foundDepartures);
        assertEquals(1, foundDepartures.size());
        assertEquals(LocalTime.of(12, 0), foundDepartures.get(0));
        ArrayList<LocalTime> foundDepartures1 = trainFactory.departureTimesFromTrack(2);
        assertNotNull(foundDepartures1);
        assertEquals(1, foundDepartures1.size());
        assertEquals(LocalTime.of(10, 0), foundDepartures1.get(0));
    }

    @Test
    void testCheckDepartureTimeExistsTrack(){
        assertTrue(trainFactory.checkDepartureTimeExistsTrack(1, LocalTime.of(12, 0)));
        assertFalse(trainFactory.checkDepartureTimeExistsTrack(1, LocalTime.of(10, 0)));
    }

    @Test
    void testCheckDepartureTimeExistsLine(){
        assertTrue(trainFactory.checkDepartureTimeExistsLine("L1", LocalTime.of(12, 0)));
        assertFalse(trainFactory.checkDepartureTimeExistsLine("L1", LocalTime.of(10, 0)));
    }


}
