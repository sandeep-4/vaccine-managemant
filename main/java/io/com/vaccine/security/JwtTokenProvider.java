package io.com.vaccine.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.com.vaccine.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	
	public String generateToken(Authentication authentication) {
		
		String authority=authentication.getAuthorities().stream()
				.map(GrantedAuthority :: getAuthority)
				.collect(Collectors.joining());
		
		User user=(User) authentication.getPrincipal();
		Date now=new Date(System.currentTimeMillis());
		
		Date expirary=new Date(now.getTime()+30000000);
		
		String userId=user.getId().toString();
		Map<String, Object> claims=new HashMap<>();
		claims.put("id", userId);
		claims.put("username", user.getUsername());
		claims.put("fullname", user.getFullname());
		claims.put("role", authority);
		

		return Jwts.builder().setSubject(authentication.getName()).claim("role", authority)
				.setIssuedAt(now)
				.setExpiration(expirary)
				.signWith(SignatureAlgorithm.HS512, "secretkey").compact();
		
		
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token);
			return true;
		}  catch (SignatureException e) {

			System.out.println("JWT signature errors");

		} catch (MalformedJwtException e) {

			System.out.println("Jwt token exception" + e);
		} catch (ExpiredJwtException e) {
			System.out.println("expired token");
		} catch (UnsupportedJwtException e) {
			System.out.println("token not supported");
		} catch (IllegalArgumentException ex) {
			System.out.println("illegal args");
		}
		return false;
	}
	
	public String getUsernameFromJWT(String token) {
		Claims claims=Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		String username=(String) claims.get("username");
		return username;
		
	}
	
	public Long getUserIdFromJWT(String token) {
		Claims claims=Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
		String id=(String) claims.get("id");
		return Long.parseLong(id);
		
	}
	
//	
//	public Authentication getAuthentication(HttpServletRequest request) {
//		String token=resolveToken(request);
//		if(token==null) {
//			return null;
//		}
//		Claims claims=Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
//		String username=claims.getSubject();
//		final List<GrantedAuthority> authorities=Arrays.stream(claims.get("role").toString()
//				.split(",")).map(role->role.startsWith("ROLE_")?role:"ROLE_"+role)
//				.map(SimpleGrantedAuthority ::new)
//				.collect(Collectors.toList());
//		if(username!=null) {
//			new UsernamePasswordAuthenticationToken(username,null,authorities);
//		}
//		return null;
//	}
//	
//	public String resolveToken(HttpServletRequest request) {
//		String bearerToken=request.getHeader("Authorization");
//		if(bearerToken!=null && bearerToken.startsWith("Bearer ")) {
//			return bearerToken.substring(7,bearerToken.length());
//		}
//		return null;
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
