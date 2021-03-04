package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * this class is used to make unitary tests of the app service
 */
@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {



    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static FareCalculatorService fareCalculatorService;

    @BeforeEach
    void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    void processIncomingVehicleTest() {
        //Arrange
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
        //Act
        parkingService.processIncomingVehicle();
        //Assert
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    void processExitingVehicleTest() throws Exception {
        //Arrange
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(LocalDateTime.now().minusMinutes(60));
        ticket.setOutTime(LocalDateTime.now());
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        //Act
        parkingService.processExitingVehicle();
        //Assert
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    void ProcessExitingWith5PerCentOff() throws Exception {
        //Arrange
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(LocalDateTime.now().minusMinutes(90));
        ticket.setOutTime(LocalDateTime.now());
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

        fareCalculatorService.oldTicket = 3;
        //Act
        parkingService.processExitingVehicle();
        //Assert
        assertEquals(0.95, ticket.getPrice());
    }
}


