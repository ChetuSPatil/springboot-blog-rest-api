package com.javateche.blog.config;

import com.javateche.blog.security.JwtAuthenticationEntryPoint;
import com.javateche.blog.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter
    ){
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        //static method means only one object will be generated
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        //here we not need to provide UserDetailsService and PasswordEncoder to AuthtnicatoinManager, spring security automatically provides PassworEncoder and UserDetailsService
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //SecurityFilterChain is a interface, DefaultSecurityFilterChain is the implementation, http.build() returns it

        http.csrf((csrf) -> csrf.disable())
                //.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated()) // it allows any/all requests
                .authorizeHttpRequests(
                        (authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                ).exceptionHandling(exception -> exception .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                //.httpBasic(Customizer.withDefaults()); //it enables the http basic authentication

        //To execute the JwtAuthenticationFilter before Security filter chain3
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    /*
    //commented since we are using Database authentication
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails chetu = User.builder()
                .username("chetu")
                //.password("password")
                .password(passwordEncoder().encode("chetuPwd"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                //.password("password")
                .password(passwordEncoder().encode("adminPwd"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin, chetu);
    }
    */
}
