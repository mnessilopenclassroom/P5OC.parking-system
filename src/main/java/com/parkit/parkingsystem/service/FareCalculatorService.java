package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.parkit.parkingsystem.dao.TicketDAO.countByVehicleRegNumber;


public class FareCalculatorService {



    public static int oldTicket;
    public double price;
    /**
     * this class is used to calculate the fare take a ticket as parameter to get infos t calculate
     *
     * @param ticket
     */
    public void calculateFare (Ticket ticket) throws Exception {
        //make sure that the parking time is ok
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime());
        }

        LocalDateTime inHour = ticket.getInTime();
        LocalDateTime outHour = ticket.getOutTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long d = ChronoUnit.MINUTES.between(inHour, outHour);
        double duration = (double) d;
        duration = duration / 60.00;


        if (oldTicket > 0) {

            oldTicket = oldTicket;

        }else {
            oldTicket = countByVehicleRegNumber(ticket.getVehicleRegNumber());
        }



        if (duration <= 0.5) {


            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR:
                case BIKE: {
                    price = 0;
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        } else if (duration > 0.5) {

            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    price = ((duration - 0.5) * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    price = ((duration - 0.5) * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }
        }
                // give 5% reduction to regular customers
            if (oldTicket > 0){
                price = price*0.95;
            }
            ticket.setPrice(price);



        }
    }



