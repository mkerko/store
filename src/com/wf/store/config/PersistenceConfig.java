package com.wf.store.config;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.wf.store" }, transactionManagerRef = "jpaTransactionManager")
@EnableJpaAuditing
@ComponentScan({ "com.wf.store" })
public class PersistenceConfig {

    private final Environment env;

    @Autowired
    public PersistenceConfig(Environment env) {
        this.env = env;
    }

    /**
     * Factory method for the entityManager
     *
     * @return the entity manager
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPackagesToScan("com.wf.store.domain");

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaProperties(hibernateProperties());

        return emf;
    }

    /**
     * Factory method for the rest datasource
     *
     * @return rest-datasource
     */
    @Bean
    public DataSource restDataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));

        return dataSource;
    }

    /**
     * Factory method for the jpa transaction manager
     *
     * @return transaction manager
     */
    @Bean
    public PlatformTransactionManager jpaTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    /**
     * Factory method for the exception translation post processor (Whatever that is)
     *
     * @return exception translation post processor
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.connection.driver_class", env.getProperty("jdbc.driverClassName"));
        hibernateProperties.setProperty("hibernate.connection.url", env.getProperty("jdbc.url"));
        hibernateProperties.setProperty("hibernate.connection.username", env.getProperty("jdbc.user"));
        hibernateProperties.setProperty("hibernate.connection.password", env.getProperty("jdbc.pass"));

        return hibernateProperties;
    }

}
