package org.elis.homeservices.utility;

import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceConfig {
	private static DataSource dataSource;
	
	public static DataSource getDataSource() {
		return dataSource;
	}

	static {
		String password = System.getenv("DB_PASS");
		Properties properties = new Properties();
		//connessione al db
		properties.setProperty("dataSourceClassName", "com.mysql.cj.jdbc.MysqlDataSource");
		properties.setProperty("dataSource.user", "root");
		if(password!=null) {
			properties.setProperty("dataSource.password", password);
		}
		else {
			properties.setProperty("dataSource.password", "root");
		}
		properties.setProperty("dataSource.databaseName", "home_services_db");
		properties.setProperty("dataSource.serverName", "localhost");
		//pool di connection
		properties.setProperty("maximumPoolSize", "10");
		properties.setProperty("minimumIdle", "5");
		properties.setProperty("idleTimeout", "300000");
		properties.setProperty("connectionTimeout", "20000");

		HikariConfig hikariConfig = new HikariConfig(properties);
		dataSource = new HikariDataSource(hikariConfig);
	}
}
