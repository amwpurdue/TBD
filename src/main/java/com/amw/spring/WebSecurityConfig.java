package com.amw.spring;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
    {
        try
        {
            return http.authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/login").permitAll()
                            .requestMatchers("/**").hasRole("USER")
//                            .requestMatchers("/**").permitAll()
                    )
//                    .formLogin(form -> form.loginPage("/login")
//                            .loginProcessingUrl("/perform_login")
//                            .defaultSuccessUrl("/homepage.html", true)
//                            .failureUrl("/login.html?error=true")
//                            .failureHandler(this::authenticationFailureHandler))
                    .formLogin(withDefaults())
                    .build();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        // ...
    }

    private void authenticationFailureHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
    {

        System.out.println("******** authenticationFailureHandler ********");
        e.printStackTrace();
    }

    @Bean
    public DataSource getDataSource()
    {
        final DBConnection.DBInfo dbInfo = DBConnection.getDBInfo();

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dbInfo.url());
        dataSource.setUsername(dbInfo.user());
        dataSource.setPassword(dbInfo.password());


        return dataSource;
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsService(DataSource dataSource)
    {
        final JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        if(!userDetailsManager.userExists("admin"))
        {
            System.out.println("ADDING USER admin");
            final UserDetails user = User.builder()
                    .passwordEncoder(passwordEncoder()::encode)
                    .username("admin")
                    .password("admin123")
                    .authorities("ROLE_USER")
                    .build();
            userDetailsManager.createUser(user);
        }
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}