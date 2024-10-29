package com.nit.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nit.constant.AppConstants;
import com.nit.services.IUserAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.ParserBuilder;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

@Component
public class JwtValidator extends OncePerRequestFilter {

	@Autowired
	private IUserAuthService userAuthService;

	@Autowired
	private JwtUtil jwtUtil;

	private static final String SECRET_KEY = "qwwerttyuiioplkjhgfdsazxcvbnm jdljdyucxmkjhsdykj";

	private Logger logger = LoggerFactory.getLogger(JwtValidator.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		 String jwt = request.getHeader("Authorization");

//        String username = null;
//        String jwt = null;

		if (jwt != null && jwt.startsWith("Bearere")) {
			jwt = jwt.substring(7);
			try {
				SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

				Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				//
				String email = String.valueOf(claims.get("email"));
				String authorities = String.valueOf(claims.get("authorities"));

				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (Exception e) {
				// TODO: handle exception
				throw new BadCredentialsException("Invalid token...from jwt validator");
			}

		}
		chain.doFilter(request, response);
	}
}