package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public static DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public boolean saveTicket(Ticket ticket) {
        Connection con = null;
        try {

            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            Timestamp in = Timestamp.valueOf(ticket.getInTime());
            Timestamp out = Timestamp.valueOf(ticket.getOutTime());
            ps.setTimestamp(4, in);
            ps.setTimestamp(5, out);
            return ps.execute();
        }catch (Exception ex){
            logger.error("Error fetching next available slot(save ticket)",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return false;
        }
    }

    public Ticket getTicket(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket();
                Timestamp in = rs.getTimestamp(4);
                Timestamp out = rs.getTimestamp(5);
                LocalDateTime In = in.toLocalDateTime();
                LocalDateTime Out = out.toLocalDateTime();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(In);
                ticket.setOutTime(Out);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        }catch (Exception ex){
            logger.error("Error fetching next available slote(get ticket)",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
            return ticket;
        }
    }

    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        try {

            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            Timestamp out = Timestamp.valueOf(ticket.getOutTime());
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, out);
            ps.setInt(3,ticket.getId());
            ps.execute();
            return true;
        }catch (Exception ex){
            logger.error("Error updating ticket info",ex);
        }finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    public static int countByVehicleRegNumber(String vehicleRegNumber) throws Exception {
        int result;
        try (Connection con = dataBaseConfig.getConnection()) {
            final PreparedStatement ps = con.prepareStatement(DBConstants.GET_COUNT_PASSAGES);
            ps.setString(1, vehicleRegNumber);
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("countPassages");
            } else {
                throw new NoSuchElementException("Empty ResultSet");
            }
        }catch (Exception ex){
            logger.error("Error counting tickets by Vehicle Reg Number",ex);
            throw new Exception("Error counting tickets by Vehicle Reg Number", ex);
        }finally {
            dataBaseConfig.closeConnection(dataBaseConfig.getConnection());
        }
        return result;
    }
}
