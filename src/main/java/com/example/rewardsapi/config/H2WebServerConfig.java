package com.example.rewardsapi.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Start H2's standalone web console (http://localhost:8082/) on application startup.
 * This avoids servlet API version / servlet registration problems.
 */
@Configuration
public class H2WebServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        // start H2 web console on port 8082
        return Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
    }
}