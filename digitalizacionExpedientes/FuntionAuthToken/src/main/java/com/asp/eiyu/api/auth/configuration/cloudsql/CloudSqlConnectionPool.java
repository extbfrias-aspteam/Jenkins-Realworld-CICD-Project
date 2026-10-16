package com.asp.eiyu.api.auth.configuration.cloudsql;


import java.security.GeneralSecurityException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Banco ASP Project: eiyu Class: CloudSqlConnectionPool.java
 *
 * Description:
 *
 * @author Herwin TR @company ICORPTTI @created Oct 6, 2023 @since JDK17
 *
 * @version Control de cambios: @version 1.0 Oct 6, 2023 Herwin: Creacion de la
 * clase
 *
 * @category
 *
 */
public class CloudSqlConnectionPool {
	private static final int MAX_POOL_SIZE = 2;
	private static final int MIN_IDLE = 0;
	private static final long CONNECTION_TIMEOUT_MS = 10000;
	private static final long IDLE_TIMEOUT_MS = 30000;
	private static final long MAX_LIFETIME_MS = 600000;
	private static volatile HikariDataSource dataSource;

	public static DataSource createConnectionPool(String dbUser, String dbPass, String dbName,
			String instanceConnectionName) throws GeneralSecurityException {
		HikariDataSource current = dataSource;
		if (current != null && !current.isClosed()) {
			return current;
		}
		synchronized (CloudSqlConnectionPool.class) {
			current = dataSource;
			if (current == null || current.isClosed()) {
				HikariConfig config = new HikariConfig();
				config.setJdbcUrl(String.format("jdbc:postgresql:///%s", dbName));
				config.setUsername(dbUser);
				config.setPassword(dbPass);
				config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
				config.addDataSourceProperty("cloudSqlInstance", instanceConnectionName);
				config.addDataSourceProperty("useSSL", false);
				config.addDataSourceProperty("requireSSL", false);
				config.addDataSourceProperty("ssl", "disable");
				config.addDataSourceProperty("ipTypes", "PRIVATE");
				config.setMaximumPoolSize(MAX_POOL_SIZE);
				config.setMinimumIdle(MIN_IDLE);
				config.setConnectionTimeout(CONNECTION_TIMEOUT_MS);
				config.setIdleTimeout(IDLE_TIMEOUT_MS);
				config.setMaxLifetime(MAX_LIFETIME_MS);
				config.setPoolName("auth-token-pool");
				current = new HikariDataSource(config);
				dataSource = current;
			}
			return current;
		}
	}
}
