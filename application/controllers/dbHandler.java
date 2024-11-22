package application.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbHandler {
	// private final static String url =
	// "jdbc:mysql://b9vpudlz4yz3pd30claw-mysql.services.clever-cloud.com:3306/b9vpudlz4yz3pd30claw?useSSL=false&serverTimezone=UTC";;
	// private final static String username = "uhrz0o1aoalvkhor";
	// private final static String pass = "v2PhvwmtuGEqTtMXhfFx";

	private final static String url = "jdbc:mariadb://127.0.0.1:3310/b9vpudlz4yz3pd30claw?useSSL=false&serverTimezone=UTC";
	private final static String username = "root";
	private final static String pass = "";

	// private final static String url =
	// "jdbc:sqlserver://MNK;Database=b9vpudlz4yz3pd30claw;IntegratedSecurity=true";
	// private final static String username = "root";
	// private final static String pass = "";

	// method predefined for connecting to database to avoid redundancy
	// adding static to make it available without creating an object
	public static Connection connect() throws ClassNotFoundException, SQLException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("MySQL JDBC Driver not found.");
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, pass);
			// connection = DriverManager.getConnection(url);
			System.out.println("success");
		} catch (SQLException e) {
			System.out.println("Error while connecting to the database: " + e.getMessage());
		}
		return connection;
	}
}
