package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.parkit.parkingsystem.dao.TicketDAO.countByVehicleRegNumber;


public class FareCalculatorService {



    public void calculateFare(Ticket ticket) throws Exception {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        LocalDateTime inHour = ticket.getInTime();
        LocalDateTime outHour = ticket.getOutTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long d = ChronoUnit.MINUTES.between(inHour, outHour);
        double duration = (double) d;
        duration = duration / 60.00;


        String vehicleRegNumber = ticket.getVehicleRegNumber();

        int oldTicketsQuantity = countByVehicleRegNumber(vehicleRegNumber);


        if (oldTicketsQuantity > 1) {

            if (duration <= 0.5) {


                switch (ticket.getParkingSpot().getParkingType()) {
                    case CAR:
                    case BIKE: {
                        ticket.setPrice(0);
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Unkown Parking Type");
                }
            } else if (duration > 0.5) {

                switch (ticket.getParkingSpot().getParkingType()) {
                    case CAR: {
                        ticket.setPrice(((duration - 0.5) * Fare.CAR_RATE_PER_HOUR) * 0.95);
                        break;
                    }
                    case BIKE: {
                        ticket.setPrice(((duration - 0.5) * Fare.BIKE_RATE_PER_HOUR) * 0.95);
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Unkown Parking Type");
                }

            }
        }else {

                if (duration <= 0.5) {


                    switch (ticket.getParkingSpot().getParkingType()) {
                        case CAR:
                        case BIKE: {
                            ticket.setPrice(0);
                            break;
                        }
                        default:
                            throw new IllegalArgumentException("Unkown Parking Type");
                    }
                } else if (duration > 0.5) {

                    switch (ticket.getParkingSpot().getParkingType()) {
                        case CAR: {
                            ticket.setPrice((duration - 0.5) * Fare.CAR_RATE_PER_HOUR);
                            break;
                        }
                        case BIKE: {
                            ticket.setPrice((duration - 0.5) * Fare.BIKE_RATE_PER_HOUR);
                            break;
                        }
                        default:
                            throw new IllegalArgumentException("Unkown Parking Type");
                    }

                }

            }
        }
    }



