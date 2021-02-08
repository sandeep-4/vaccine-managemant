package io.com.vaccine.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.com.vaccine.expception.UsernameAlreadyExistException;
import io.com.vaccine.model.Role;
import io.com.vaccine.model.User;
import io.com.vaccine.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Autowired
	JwtTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String token=getJwtfromRequest(request);
			if(token==null) {
				throw new UsernameAlreadyExistException("Token not here, Please Login " +token);
			}
			
			
//			System.out.println(token);
			Claims claims=Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		
			String username=claims.getSubject();
			final List<GrantedAuthority> authorities=Arrays.stream(claims.get("role").toString()
					.split(",")).map(role->role.startsWith("ROLE_")?role:"ROLE_"+role)
					.map(SimpleGrantedAuthority ::new)
					.collect(Collectors.toList());
			if(username!=null) {
				UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(username,null,authorities);
				System.out.println(authentication.isAuthenticated());	
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}
			} catch (Exception e) {
			logger.error("could not set authentication in security context  :  " +e);
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private String getJwtfromRequest(HttpServletRequest request) {
		String bearerToken=request.getHeader("Authorization");
		if(bearerToken!=null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7,bearerToken.length());
		}
		return null;
	}

}
