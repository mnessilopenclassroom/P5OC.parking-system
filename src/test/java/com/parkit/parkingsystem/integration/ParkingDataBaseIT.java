package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * this class is used to make integration tests of the app service
 */
@ExtendWith(MockitoExtension.class)
class ParkingDataBaseIT {



    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static ParkingSpot parkingSpot;
    private static ParkingService parkingService;
    private static DataBasePrepareService dataBasePrepareService;
    private static final String vehicleRegNumber = "ABCDEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        dataBasePrepareService.clearDataBaseEntries();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
    }

    @Test
    void testParkingACar(){
        //Arrange
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(LocalDateTime.now());
        //Act
        parkingService.processIncomingVehicle();
        //Assert
        assertNotNull(ticketDAO.getTicket("ABCDEF"));//make sure that a ticket is saved
        assertFalse(parkingSpot.isAvailable());//availabbility updated
    }

    @Test
    void testParkingLotExit() throws Exception{
        //Arrange
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //TODO: check that the fare generated and out time are populated correctly in the database
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setInTime(LocalDateTime.now());
        ticket.setOutTime(LocalDateTime.now().plusMinutes(29));
        //Act
        parkingService.processIncomingVehicle();

        Thread.sleep(2000);
        parkingService.processExitingVehicle();
        //Arrange
        assertEquals(0,ticket.getPrice());//fare generated ok
        assertNotNull(ticket.getOutTime());//outtime generated ok
    }

}

