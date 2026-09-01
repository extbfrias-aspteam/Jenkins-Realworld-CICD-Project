package functions.cloudsql;


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
	public static DataSource createConnectionPool(String dbUser, String dbPass, String dbName,
			String instanceConnectionName) throws GeneralSecurityException {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(String.format("jdbc:postgresql:///%s", dbName));
		config.setUsername(dbUser); // e.g. "root", "postgres"
		config.setPassword(dbPass); // e.g. "my-password"
		config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
		config.addDataSourceProperty("cloudSqlInstance", instanceConnectionName);
		config.addDataSourceProperty("useSSL", false);
		config.addDataSourceProperty("requireSSL", false);
		config.addDataSourceProperty("ssl", "disable");
	    config.addDataSourceProperty("ipTypes", "PRIVATE");
		
		DataSource pool = new HikariDataSource(config);
		return pool;
	}
}
