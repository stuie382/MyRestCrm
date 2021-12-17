package com.stuart.mycrm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan("com.stuart.mycrm")
@PropertySource({"classpath:persistence-mysql.properties",
        "classpath:security-persistence-mysql.properties"})
public class AppConfig implements WebMvcConfigurer {

    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Autowired Spring environment.
     */
    private final Environment env;

    @Autowired
    public AppConfig(Environment env) {
        this.env = env;
    }

    /**
     * Set up the data source for the main application.
     *
     * @return A configured datasource for the main application
     */
    @Bean
    public DataSource appDataSource() {
        var appDataSource = new ComboPooledDataSource();
        // set the jdbc driver
        try {
            appDataSource.setDriverClass(env.getProperty("jdbc.driver"));
        } catch (PropertyVetoException exc) {
            logger.severe("Could not get JDBC driver from properties for data source.");
            throw new RuntimeException(exc);
        }

        logger.info("jdbc.url=" + env.getProperty("jdbc.url"));
        logger.info("jdbc.user=" + env.getProperty("jdbc.user"));

        // set database connection properties
        appDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        appDataSource.setUser(env.getProperty("jdbc.user"));
        appDataSource.setPassword(env.getProperty("jdbc.password"));

        // set connection pool properties
        appDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
        appDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
        appDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
        appDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

        return appDataSource;
    }

    /**
     * Set up the data source for the security application
     *
     * @return A configured datasource for the security application
     */
    @Bean
    public DataSource securityDataSource() {
        var securityDataSource = new ComboPooledDataSource();
        try {
            securityDataSource.setDriverClass(env.getProperty("security.jdbc.driver"));
        } catch (PropertyVetoException pve) {
            logger.severe("Could not get JDBC driver from properties for security data source");
            throw new RuntimeException(pve);
        }
        logger.info("security.jdbc.url=" + env.getProperty("security.jdbc.url"));
        logger.info("security.jdbc.user=" + env.getProperty("security.jdbc.user"));

        // set database connection properties
        securityDataSource.setJdbcUrl(env.getProperty("security.jdbc.url"));
        securityDataSource.setUser(env.getProperty("security.jdbc.user"));
        securityDataSource.setPassword(env.getProperty("security.jdbc.password"));

        // set connection pool properties
        securityDataSource.setInitialPoolSize(getIntProperty("security.connection.pool.initialPoolSize"));
        securityDataSource.setMinPoolSize(getIntProperty("security.connection.pool.minPoolSize"));
        securityDataSource.setMaxPoolSize(getIntProperty("security.connection.pool.maxPoolSize"));
        securityDataSource.setMaxIdleTime(getIntProperty("security.connection.pool.maxIdleTime"));

        return securityDataSource;
    }

    private Properties getHibernateProperties() {
        var props = new Properties();
        props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        return props;
    }

    private int getIntProperty(String propName) {
        String propVal = env.getProperty(propName);
        if (propVal != null) {
            return Integer.parseInt(propVal);
        }
        throw new IllegalArgumentException("Could not get value for property: " + propName);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        var sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(appDataSource());
        sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        var txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**")
//                .addResourceLocations("/resources/");
//    }
}
