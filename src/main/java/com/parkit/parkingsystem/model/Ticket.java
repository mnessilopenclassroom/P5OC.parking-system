package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * this class contains variables needed on a ticket
 * getters and setters to access this variables
 */
public class Ticket {


    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private LocalDateTime inTime;
    private LocalDateTime outTime;



    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public ParkingSpot getParkingSpot() {

        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {

        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {

        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {

        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {

        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    public LocalDateTime getInTime() {

        return inTime;
    }

    public void setInTime(LocalDateTime inTime) {

        this.inTime = inTime;
    }

    public LocalDateTime getOutTime() {

        return outTime;
    }

    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }


}
