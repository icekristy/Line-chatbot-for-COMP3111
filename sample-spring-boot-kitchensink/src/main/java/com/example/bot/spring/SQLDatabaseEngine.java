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
		ResultSet result = null;
		Connection connection = null;
		PreparedStatement stmt = null;
		String response = null;
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(
					"SELECT response FROM test WHERE keyword = ?"
			);
			stmt.setString(1, text);
			result = stmt.executeQuery();
			if (result.next()){
				response = result.getString(1);
			}
		} catch (Exception e) {
			log.info("Exception while access database: {}", e.toString());
		} finally {
			try {
				result.close();
				stmt.close();
				connection.close();
			} catch (Exception ex) {
				log.info("Exception while closing database: {}", ex.toString());
			}
		}
		if (response != null)
			return response;
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
