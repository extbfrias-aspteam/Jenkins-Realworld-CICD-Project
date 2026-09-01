package com.asp.eiyu.api.admdocument.configuration.cloudsql;

import java.security.GeneralSecurityException;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class CloudSqlConnectionPool {
    private static final int MAX_POOL_SIZE = 3;
    private static final int MIN_IDLE = 0;
    private static final long CONNECTION_TIMEOUT_MS = 10000;
    private static final long IDLE_TIMEOUT_MS = 30000;
    private static final long MAX_LIFETIME_MS = 600000;

    private static volatile HikariDataSource dataSource;

    public static DataSource getDataSource(String dbUser, String dbPass, String dbName,
                                           String instanceConnectionName) throws GeneralSecurityException {
        if (dataSource == null || dataSource.isClosed()) {
            synchronized (CloudSqlConnectionPool.class) {
                if (dataSource == null || dataSource.isClosed()) {
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
                    config.addDataSourceProperty("applicationName", "mi-api-documentos");
                    config.setPoolName("carga-documentos-pool");

                    dataSource = new HikariDataSource(config);
                }
            }
        }
        return dataSource;
    }
}
