package edu.ntnu.stud;

import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class TrainRegistryTest {
    private TrainRegistry trainRegistry;

    ArrayList<TrainDeparture> trainDepartureList;
    HashMap<Integer, TrainDeparture> numberToDepartureMap;

    @BeforeEach
    void setUp() {
        trainRegistry = new TrainRegistry();
        trainDepartureList = new ArrayList<>();
        numberToDepartureMap = new HashMap<>();

        trainRegistry.addDeparture(LocalTime.of(12,0), "L1", "Trondheim", 1, 1, LocalTime.of(0, 0));
        trainRegistry.addDeparture(LocalTime.of(10,0), "L2", "Stavanger", 2, 2, LocalTime.of(0, 0));
    }

    @Test
    void testAddDeparture() {
        assertEquals(2, trainRegistry.getTrainDepartures().size());
    }

    @Test
    void testAssignTrack() {
        trainRegistry.assignTrack(1, 2);
        assertEquals(2, trainRegistry.departureFromNumber(1).getTrack());
    }

    @Test
    void testAddDelay() {
        trainRegistry.addDelay(1, LocalTime.of(0, 10));
        assertEquals(LocalTime.of(0, 10), trainRegistry.departureFromNumber(1).getDelay());
    }

    //TODO: FIX THIS TEST
    @Test
    void testDepartureFromNumber() {
        System.out.println(numberToDepartureMap);
        assertEquals(numberToDepartureMap.get(1), trainRegistry.departureFromNumber(1));
    }

    @Test
    void testDepartureFromDestination() {
        List<TrainDeparture> foundDepartures = trainRegistry.departureFromDestination("Trondheim");
        assertNotNull(foundDepartures);
        assertEquals(1, foundDepartures.size());
        assertEquals("Trondheim", foundDepartures.get(0).getDestination());
    }

    @Test
    void testSetCurrentTime() {
        trainRegistry.setCurrentTime(LocalTime.of(12, 0));
        assertEquals(trainRegistry.getCurrentTime(), LocalTime.of(12, 0));
    }

    @Test
    void testRemoveDeparted(){
        trainRegistry.setCurrentTime(LocalTime.of(12, 30));
        trainRegistry.removeDeparted();
        assertEquals(0, trainDepartureList.size());
        assertEquals(0, numberToDepartureMap.size());
    }

    @Test
    void testSortByDepartureTime(){
        trainRegistry.sortByDepartureTime();
        assertEquals(LocalTime.of(10,0), trainRegistry.getTrainDepartures().get(0).getDepartureTime());
    }


    @Test
    void testCheckDepartureTimeExistsTrack(){
        assertTrue(trainRegistry.checkDepartureTimeExistsTrack(1, LocalTime.of(12, 0)));
        assertFalse(trainRegistry.checkDepartureTimeExistsTrack(1, LocalTime.of(10, 0)));
    }

    @Test
    void testCheckDepartureTimeExistsLine(){
        assertTrue(trainRegistry.checkDepartureTimeExistsLine("L1", LocalTime.of(12, 0)));
        assertFalse(trainRegistry.checkDepartureTimeExistsLine("L1", LocalTime.of(10, 0)));
    }

}
