package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patients {
	private Connection connection;
	private Scanner scanner;
	
	public Patients(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void addPatient() {
		System.out.println("Enter the patient Name :");
		String name = scanner.next();
		System.out.println("Enter patients age");
		int age = scanner.nextInt();
		System.out.println("Enter patient gender");
		String gender = scanner.next();
				
		try {
			String query = "INSERT INTO patients(name, age, gender)  VALUE(?,?,?)";
			PreparedStatement praparedStatement = connection.prepareStatement(query);
			praparedStatement.setString(1, name);
			praparedStatement.setInt(2, age);
			praparedStatement.setString(3, gender);
			
			int effectedRows = praparedStatement.executeUpdate();
			
			if(effectedRows>0) {
				System.out.println("Patient Added Successfully");
			}else {
				System.out.println("Failed to add Patients");
			}
			
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	
		public void viewPatients() {
			String query = "select * from patients";
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery();
			
				System.out.println("Patients: ");
				System.out.println("+------------+--------------------+----------+----------+");
				System.out.println("| Patient id | Name               | Age      | Gender   |");
				System.out.println("+------------+--------------------+----------+----------+");
			
				while(resultSet.next()) {
					int id = resultSet.getInt("id");
					String name = resultSet.getString("name");
					int age  = resultSet.getInt("age");
					String gender = resultSet.getString("gender");
					System.out.printf("|%12s|%-20s|%-10s|%-10s|\n", id, name, age, gender);
				
					System.out.println("+------------+--------------------+----------+----------+");

				}	
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		
		public boolean getPatientById(int id) {
			String query = "SELECT * FROM patients WHERE id = ?";
			
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1,id);
				
				ResultSet resultset = preparedStatement.executeQuery();
				
				if(resultset.next()) {
					return true;
				}else{
					return false;
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
			
}
