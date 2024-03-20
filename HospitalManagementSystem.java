package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystaem {
	private static final String url = "jdbc:mysql://127.0.0.1:3306/hospital";
	
	private static final String username  = "root";
	
	private static final String password = "";
	
	public static void main (String args []) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner scanner =  new Scanner(System.in);
		
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Patients patient = new Patients(connection, scanner);
			Doctor doctor = new Doctor(connection);
			
			while(true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patients");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				
				System.out.println("Enter Your choice: ");
				
				int choice = scanner.nextInt();

				switch(choice) {
				case 1:
					//Add Patient
					patient.addPatient();
					System.out.println();
				case 2 :
					//View Patients
					patient.viewPatients();
					System.out.println();
				case 3 :
					//view Doctors
					doctor.viewDoctor();
					System.out.println();
				case 4  :
					//Add Appointment
					bookAppointment(patient, doctor, connection, scanner);
				case 5 :
					return;
				
				default :
					System.out.println("Enter correct choice");

				}
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void bookAppointment(Patients patient, Doctor doctor, Connection connection, Scanner scanner) {
		System.out.print("Enter Patient id: ");
		int patientId  = scanner.nextInt();
		System.out.print("Enter Doctor Id: ");
		int doctorId = scanner.nextInt();
		System.out.print("Enter Appointment date (YYYY-MM-DD): ");
		String appointmentDate = scanner.next();
		if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
			if(checkDoctorAvilability(doctorId, appointmentDate, connection)) {
				String apppointmentQuery  = "INSERT INTO appointnments(patients_id, doctors_id, appointment_date) VALUES(?, ?, ?)";
				try {
					PreparedStatement preparedstatement = connection.prepareStatement(apppointmentQuery);
					preparedstatement.setInt(1, patientId);
					preparedstatement.setInt(2, doctorId);
					preparedstatement.setString(3, appointmentDate);
					
					int rowsAffected = preparedstatement.executeUpdate();
					
					if(rowsAffected>0) {
						System.out.println("Appointment Booked!!!");
					}else {
						System.out.println("Failed to Book Appointment");
					}
					
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Doctor not available on this date");

			}
		}else {
			System.out.println("Either doctor or patient doesn't exist!!!");

		}
	}
			
		public static  boolean checkDoctorAvilability(int doctorId, String appointmentDate, Connection connection) {
			String query = "SELECT COUNT(*) FROM appointnments WHERE doctors_id = ? AND appointment_date = ?";
			
			try {
				PreparedStatement preparedstatement = connection.prepareStatement(query);
				preparedstatement.setInt(1, doctorId);
				preparedstatement.setString(2, appointmentDate);
				
				ResultSet resultset = preparedstatement.executeQuery(); 
				
				if(resultset.next()) {
					int count = resultset.getInt(1);
					if(count==0) {
						return true;
					}else {
						return false;
					}
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			return false;
	
	}
}	
