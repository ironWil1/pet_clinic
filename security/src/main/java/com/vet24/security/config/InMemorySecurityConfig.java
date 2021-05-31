package com.vet24.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class InMemorySecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .csrf().disable();
    }

    @Bean(name="myUserDetailsService")
    protected UserDetailsService users () {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("admin")
                .build();
        UserDetails doctor = User.builder()
                .username("doctor")
                .password("{noop}doctor")
                .roles("DOCTOR")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
