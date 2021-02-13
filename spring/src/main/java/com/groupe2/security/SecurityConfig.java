package com.groupe2.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//set spring security to use app_user table in datable as authentification source
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("select login as principal, password as credentials, active from app_user where login=?")
			.authoritiesByUsernameQuery("select login as principal, role as role from app_user where login=?")
			.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
    		.authorizeRequests()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/", "/sensors", "/addSensor", "/data", "/changeFrequency").hasAnyRole("ADMIN", "USER")
				.antMatchers("/admin").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
			.logout()
				.permitAll()
				.and()
			.exceptionHandling().accessDeniedPage("/403");
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() { 
		return new BCryptPasswordEncoder();
	}
}
