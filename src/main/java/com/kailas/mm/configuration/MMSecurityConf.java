package com.kailas.mm.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MMSecurityConf {
    // This is for prmit all the requests
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable());

        return http.build();
    }

    // @Bean
    // SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws
    // Exception {
    //
    // http.authorizeHttpRequests((requests) -> requests
    // .requestMatchers("/member","/reservation").authenticated()
    // .requestMatchers("/items").permitAll())
    // .formLogin(Customizer.withDefaults())
    // .httpBasic(Customizer.withDefaults());
    // return http.build();
    //
    // }
    //

    // @Bean
    // PasswordEncoder passwordEncoder(){
    // return new BCryptPasswordEncoder();
    // }
    // @Bean
    // SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws
    // Exception {
    //
    // http.authorizeHttpRequests((requests) -> requests
    // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
    // // Permit access to static resources
    // .antMatchers("/myAccount","/myBalance","/myLoans","/myCards").authenticated())
    // // Require authentication for these URLs
    // .authorizeHttpRequests((requests) -> requests
    // .antMatchers("/items/**","/contact").permitAll()) // Permit access to /items/
    // and /contact without authentication
    // .formLogin(Customizer.withDefaults()) // Configure form login
    // .httpBasic(Customizer.withDefaults()); // Configure HTTP basic authentication
    //
    // return http.build();
}
