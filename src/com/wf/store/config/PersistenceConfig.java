package com.wf.store.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.wf.store"}, transactionManagerRef = "jpaTransactionManager")
@ComponentScan({"com.wf.store"})
public class PersistenceConfig {

    private static final String DOMAIN_PATH = "com.wf.store.domain";
    private static final String JDBC_DRIVER_CLASS_NAME = "jdbc.driverClassName";
    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USER = "jdbc.user";
    private static final String JDBC_PASS = "jdbc.pass";
    private static final String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";
    private static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    private static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    private static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
    private static final String HIBERNATE_C3P0_MIN_SIZE = "hibernate.c3p0.min_size";
    private static final String HIBERNATE_C3P0_MAX_SIZE = "hibernate.c3p0.max_size";
    private static final String HIBERNATE_C3P0_TIMEOUT = "hibernate.c3p0.timeout";
    private static final String HIBERNATE_C3P0_MAX_STATEMENTS = "hibernate.c3p0.max_statements";

    private static final String HIBERNATE_C3P0_IDLE_TEST_PERIOD = "hibernate.c3p0.idle_test_period";
    private static final String C3PO_MIN_SIZE = "c3p0.min_size";
    private static final String C3PO_MAX_SIZE = "c3p0.max_size";
    private static final String C3PO_TIMEOUT = "c3p0.timeout";
    private static final String C3PO_MAX_STATEMENTS = "c3p0.max_statements";
    private static final String C3PO_IDLE_TEST_PERIOD = "c3p0.idle_test_period";
    private final Environment env;

    @Autowired
    public PersistenceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPackagesToScan(DOMAIN_PATH);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaProperties(hibernateProperties());
        return emf;
    }

    @Bean
    public PlatformTransactionManager jpaTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(HIBERNATE_CONNECTION_DRIVER_CLASS, env.getProperty(JDBC_DRIVER_CLASS_NAME));
        hibernateProperties.setProperty(HIBERNATE_CONNECTION_URL, env.getProperty(JDBC_URL));
        hibernateProperties.setProperty(HIBERNATE_CONNECTION_USERNAME, env.getProperty(JDBC_USER));
        hibernateProperties.setProperty(HIBERNATE_CONNECTION_PASSWORD, env.getProperty(JDBC_PASS));

        hibernateProperties.setProperty(HIBERNATE_C3P0_MIN_SIZE, env.getProperty(C3PO_MIN_SIZE));
        hibernateProperties.setProperty(HIBERNATE_C3P0_MAX_SIZE, env.getProperty(C3PO_MAX_SIZE));
        hibernateProperties.setProperty(HIBERNATE_C3P0_TIMEOUT, env.getProperty(C3PO_TIMEOUT));
        hibernateProperties.setProperty(HIBERNATE_C3P0_MAX_STATEMENTS, env.getProperty(C3PO_MAX_STATEMENTS));
        hibernateProperties.setProperty(HIBERNATE_C3P0_IDLE_TEST_PERIOD, env.getProperty(C3PO_IDLE_TEST_PERIOD));

        return hibernateProperties;
    }

    @Bean
    public HttpTransport netHttpTransport() {
        return new NetHttpTransport();
    }

    @Bean
    public JsonFactory jacksonFactory() {
        return new JacksonFactory();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
