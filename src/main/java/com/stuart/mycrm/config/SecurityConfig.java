package com.stuart.mycrm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;
import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String MANAGER = "MANAGER";
    public static final String EMPLOYEE = "EMPLOYEE";

    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Autowired, security data source.
     */
    private final DataSource securityDataSource;

    @Autowired
    public SecurityConfig(DataSource securityDataSource) {
        this.securityDataSource = securityDataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(securityDataSource).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // GET
                .antMatchers(HttpMethod.GET, "/api/customers").hasRole(EMPLOYEE)
                .antMatchers(HttpMethod.GET, "/api/customers/**").hasRole(EMPLOYEE)
                // POST
                .antMatchers(HttpMethod.POST, "/api/customers").hasAnyRole(MANAGER, ADMIN)
                .antMatchers(HttpMethod.POST, "/api/customers/**").hasAnyRole(MANAGER, ADMIN)
                // PUT
                .antMatchers(HttpMethod.PUT, "/api/customers").hasAnyRole(MANAGER, ADMIN)
                .antMatchers(HttpMethod.PUT, "/api/customers/**").hasAnyRole(MANAGER, ADMIN)
                // DELETE
                .antMatchers(HttpMethod.DELETE, "/api/customers/**").hasRole(ADMIN)
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        var jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(securityDataSource);
        return jdbcUserDetailsManager;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsManager());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info(() -> "***** Setting up password encoder bean *****");
        return new BCryptPasswordEncoder();
    }
}
