package com.buildsessions.stockmarketanalyzer.config;

import com.buildsessions.stockmarketanalyzer.service.AuthenticationSuccessHandler;
import com.buildsessions.stockmarketanalyzer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        {
                            authorizationManagerRequestMatcherRegistry.requestMatchers("/home").permitAll();
                            authorizationManagerRequestMatcherRegistry.requestMatchers("/user/**").permitAll();
                            authorizationManagerRequestMatcherRegistry.requestMatchers("/admin/**").hasRole("ADMIN");
                            authorizationManagerRequestMatcherRegistry.anyRequest().permitAll();
                        }
                )
                .formLogin(
                        httpSecurityFormLoginConfigurer -> {
                            httpSecurityFormLoginConfigurer.successHandler(new AuthenticationSuccessHandler());
                            httpSecurityFormLoginConfigurer.permitAll();
                        }
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


}
