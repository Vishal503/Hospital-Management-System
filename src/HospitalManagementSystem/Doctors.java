package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;

    //Constructor
    public Doctors(Connection connection) {
        this.connection = connection;

    }

    public void viewPatient(){
        String query = "SELECT * FROM doctors ";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("Doctors Details: ");
            System.out.println("+------------+------------------+--------+-----------+");
            System.out.println("| Doctor Id | Name              | Department    |");
            System.out.println("+------------+------------------+--------+-----------+");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                System.out.printf("| %-10s | %-17s | %-13s |\n",id,name,department);
                System.out.println("+------------+------------------+--------+-----------+");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
