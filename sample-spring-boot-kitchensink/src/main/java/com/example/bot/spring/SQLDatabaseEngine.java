package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		ResultSet result = null;
		 Connection connection = null;
		 PreparedStatement stmt = null;
		 
		 try {
		 		connection = this.getConnection();
		 		stmt = connection.prepareStatement(
		 				"select reponse from test where keyword like concat('%', ?, '%')"
		 				);
		 		stmt.setString(1, text); 
		 		result = stmt.executeQuery();
		 		if (result.next())
		 		return result.getString(1);
		 		System.out.println("result: " + result.toString());
		 } catch (Exception e) {
		 	log.info("IOException while reading file: {}", e.toString());
		 } finally {
		 	try {
		 		result.close();stmt.close();connection.close();
		 	} catch (Exception ex) {
		 		log.info("IOException while closing file: {}", ex.toString());
		 	}
		 }
		 
		 if (result.next())
		 	return result.getString(1);
		 throw new Exception("NOT FOUND");

	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
