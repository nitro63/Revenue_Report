package com.Enums;

import com.revenue_report.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public enum Rates {
    COMMISSION{
        @Override
        public String getId() throws SQLException {
            PreparedStatement statement = getCon().prepareStatement("SELECT * FROM `revenue_rates` WHERE " +
                    "`rate_status` IS TRUE");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                return rs.getString("id");
            }
            return null;
        }
//'"+ COMMISSION +"'
        @Override
        public double getActiveRate() throws SQLException {
            PreparedStatement statement = getCon().prepareStatement("SELECT * FROM `commission_rates` WHERE " +
                    "`rate_status` IS TRUE ");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                double test = rs.getDouble("rate_value");
                String test1 = rs.getString("id");
                test1.getBytes();
                return test;
            }
            return 0;
      }
    };
    private final Connection con;

    Rates() {
        this.con = DBConnection.getConn();
    }

    public Connection getCon(){
        return this.con;
    }
    public abstract String getId() throws SQLException;
    public abstract double getActiveRate() throws SQLException;

}
