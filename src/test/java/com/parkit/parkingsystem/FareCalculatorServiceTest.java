package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * this class is used to make unitary test to calculate fares in different scenarios
 */
public class FareCalculatorServiceTest {



    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar() throws Exception {
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(90);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike() throws Exception {
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(90);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(60);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Assert
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().plusMinutes(60);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() throws Exception {
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertEquals((0.25 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() throws Exception {
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertEquals( (0.25 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() throws Exception {
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusHours(24);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Arrange
        assertEquals( (23.5 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanHalfAnHourParkingTime() throws Exception {
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(28);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Arrange
        assertEquals( 0 , ticket.getPrice());
    }
    @Test
    public void calculateFareCarWith5minParkingTime() throws Exception {
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(5);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertEquals( (0) , ticket.getPrice());
    }
    @Test
    public void calculateFareBikeWith75MinParkingTime() throws Exception {
        //Arrange
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(75);
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //Act
        fareCalculatorService.calculateFare(ticket);
        //Assert
        assertEquals( (0.75 * Fare.BIKE_RATE_PER_HOUR) , ticket.getPrice());
    }


}
