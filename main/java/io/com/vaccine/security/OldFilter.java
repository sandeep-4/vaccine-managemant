package io.com.vaccine.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.com.vaccine.model.Role;
import io.com.vaccine.model.User;
import io.com.vaccine.service.CustomUserDetailsService;

public class OldFilter extends OncePerRequestFilter{
	
	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Autowired
	JwtTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String jwts=getJwtfromRequest(request);
			
			if(StringUtils.hasText(jwts) && tokenProvider.validateToken(jwts)) {
//				Long userId=tokenProvider.getUserIdFromJWT(jwts);
//				User userDetails=userDetailsService.loadByUserId(userId);
				
				String username=tokenProvider.getUsernameFromJWT(jwts);
				User userDetails=(User) userDetailsService.loadUserByUsername(username);
				
//				System.out.println(userDetails.getUsername());
//				System.out.println(userDetails.getFullname());
			
//				UsernamePasswordAuthenticationToken authentication=new 
//						UsernamePasswordAuthenticationToken(userDetails, null,["PATIENT","DOCTOR"]);
				
			
				UsernamePasswordAuthenticationToken authentication=new 
						UsernamePasswordAuthenticationToken(userDetails, null,Collections.emptyList());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}
		} catch (Exception e) {
			logger.error("could not set authentication in security context");
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private String getJwtfromRequest(HttpServletRequest request) {
		String bearerToken=request.getHeader("Authorization");
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7,bearerToken.length());
		}
		return null;
	}

}
