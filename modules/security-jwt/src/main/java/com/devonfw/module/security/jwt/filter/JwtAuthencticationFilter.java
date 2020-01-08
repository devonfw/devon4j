package com.devonfw.module.security.jwt.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.devonfw.module.security.jwt.config.KeyStoreAccess;
import com.devonfw.module.security.jwt.util.TokenExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JwtAuthencticationFilter extends GenericFilterBean {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthencticationFilter.class);

	private static final String AUTHORIZATION = "Authorization";

	private KeyStoreAccess keyStoreAccess;

	private ObjectMapper objectMapper;

	public JwtAuthencticationFilter(KeyStoreAccess keyStoreAccess, ObjectMapper objectMapper) {
		this.keyStoreAccess = keyStoreAccess;
		this.objectMapper = objectMapper;
	}

	@SuppressWarnings("javadoc")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		Authentication authentication = getAuthentication((HttpServletRequest) request);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	private Authentication getAuthentication(HttpServletRequest request) {
		Authentication authentication;
		// Taking token from request
		String authToken = request.getHeader(AUTHORIZATION);

		if (authToken != null) {
			// deserialize token and validate
			TokenExtractor extractor = new TokenExtractor(keyStoreAccess, objectMapper);
			authentication = extractor.validateTokenAndSignature(authToken);
			return authentication;
		} else {
			LOG.info("Provide token in authorization header of request");
		}

		return null;
	}
}
