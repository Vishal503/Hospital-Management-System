package HospitalManagementSystem;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String user = "root";
    private static final String password = "nanu";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try{
            Connection connection = DriverManager.getConnection(url,user,password);
            Patient patient = new Patient(connection,scanner);
            Doctors doctors = new Doctors(connection);
            while(true){
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch(choice){
                    case 1:
                        //Add Patient
                        patient.addPatient();
                        break;
                    case 2:
                        //View Patient
                        patient.viewPatient();
                        break;
                    case 3:
                        //View Doctors
                        doctors.viewPatient();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient,doctors,connection,scanner);
                        System.out.println();
                        break;

                    case 5:
                        System.out.println("THANK YOU! FOR VISITING");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient, Doctors doctors, Connection connection, Scanner scanner){
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor ID: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctors.getDoctorById(doctorId) ){
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id,doctor_id,appointment_data) VALUES(?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked Successfully");
                    }
                    else{
                        System.out.println("Appointment Booking Failed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Doctor is not available on this date!");
            }
        }
        else{
            System.out.println("Either Doctor or Patient does not exist");
        }

    }

    public static boolean checkDoctorAvailability(int doctorId, String appointment_data, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_data=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointment_data);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                if (count == 0){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
