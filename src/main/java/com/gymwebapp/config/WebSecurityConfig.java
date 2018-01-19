package com.gymwebapp.config;

import com.gymwebapp.domain.Administrator;
import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.Coach;
import com.gymwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;


import java.util.Arrays;

/**
 * @author foldv on 29.11.2017.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthSuccessHandler authSuccessHandler;
    @Autowired
    AuthFailureHandler authFailerHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/about.html").permitAll()
                    .antMatchers("/loginPage.html").not().authenticated()
                    .antMatchers("/about.html").permitAll()
                    .antMatchers("/main.html").permitAll()
                    .antMatchers("/contact.html").permitAll()
                    .antMatchers("/coaches.html").not().hasAnyRole("ADMIN")
                    .antMatchers("/courses.html").not().hasAnyRole("ADMIN")
                    .antMatchers("/index.html").permitAll()
                    .antMatchers("/manageCoaches.html").hasAnyRole("ADMIN")
                    .antMatchers("/manageCourses.html").hasAnyRole("ADMIN")
                    .antMatchers("/personalInfo.html").hasAnyRole("CLIENT")
                    .antMatchers("/register.html").not().hasAnyRole("CLIENT", "COACH", "ADMIN")
                    .antMatchers("/feedbacks.html").hasAnyRole("COACH")
                    .antMatchers("/schedule.html").permitAll()
                    .antMatchers("/", "/public/**", "/resources/**", "/resources/static/**")
                    .permitAll()
                    .and()
                .formLogin()
                    .loginPage("/loginPage.html").loginProcessingUrl("/login")
                    .usernameParameter("username").passwordParameter("password")
                    .failureHandler(authFailerHandler)
                    .successHandler(authSuccessHandler)
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/main.html")
                    .invalidateHttpSession(true)
                    .and()
                    .exceptionHandling().accessDeniedPage("/main.html");
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws  Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}