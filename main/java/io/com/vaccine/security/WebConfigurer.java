package io.com.vaccine.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.com.vaccine.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true
		)
public class WebConfigurer extends WebSecurityConfigurerAdapter{

	@Autowired
	JwtAuthenticateEntryPoint jwtEntryPoint;
	
	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Autowired
	BCryptPasswordEncoder bcrypt;
	
	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bcrypt);
	}

	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(jwtEntryPoint)
		.and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.headers().frameOptions().sameOrigin()
		.and()
		.authorizeRequests()
		.antMatchers("/index",
				"/api/user/register",
				"/api/user/login",
				"/api/vaccine/all",
				"/api/vaccine/search/{filter}",
				"/favicon.ico",
				"/**/*.png",
				"/**/*.jpeg",
				"/**/*.jpg",
				"/**/*.gif",
				"/**/*.html",
				"/**/*.css",
				"/**/*.js"
				)
		.permitAll()
		.antMatchers("/api/patient/*/**",
				"/api/appointment/*/**").hasRole("PATIENT")
		.antMatchers("/api/doctor/*/**"
				,"/api/vaccine/*/**",
				"/api/appointment/*/**").hasRole("DOCTOR")
		.anyRequest().fullyAuthenticated()
		.and().logout().permitAll()
//		.logoutRequestMatcher(new AntPathMatcher("/api/user/logout"))
		.and()
		.httpBasic();
		
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public WebMvcConfigurer crosConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedMethods("*");
//				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
			}
			};
	}
	
	
	
}
