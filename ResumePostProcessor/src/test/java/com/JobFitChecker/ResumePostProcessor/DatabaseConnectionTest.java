package com.JobFitChecker.ResumePostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDataSourceIsNotNull() {
        assertThat(dataSource).isNotNull();
        System.out.println("DataSource is not null, database connection initialized.");
    }

    @Test
    public void testDatabaseConnection() throws Exception {
        // This runs a simple query to test the connection
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertThat(result).isEqualTo(1);
        System.out.println("Database connection successful! Query returned: " + result);
    }
}
