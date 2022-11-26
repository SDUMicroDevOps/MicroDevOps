package com.oops.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;

import javax.sql.DataSource;

public class SQLCloudController {

    private static final String DB_USER = "root";
    private static final String DB_PASS = "smom19[d#+j<mk+O";
    private static final String DB_NAME = "mysql";

    private static final String CONNECTION_NAME = "optical-empire-364322:europe-west1:oops";
    private static final String INSTANCE_HOST = "34.78.220.25";
    private static final String DB_PORT = "3306";

    //Simple test file
    public static DataSource createConnectionPool() {
        HikariConfig config = new HikariConfig();

        Properties connProps = new Properties();
        connProps.setProperty("user", DB_USER);
        connProps.setProperty("password", DB_PASS);
        connProps.setProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
        connProps.setProperty("cloudSqlInstance", CONNECTION_NAME);

        config.setJdbcUrl(String.format("jdbc:mysql:///%s", DB_NAME));
        config.setDataSourceProperties(connProps);

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(5);

        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
    
}