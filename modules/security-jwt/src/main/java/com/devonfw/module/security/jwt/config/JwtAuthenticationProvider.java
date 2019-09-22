package com.devonfw.module.security.jwt.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * We can implement our authentication mechanism here.JwtAuthenticationProvider
 * implements AuthenticationProvider interface.In this class we will set
 * userdetailservice which need to be used by provider.
 *
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private UserDetailsService userDetailsService;

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(auth.getName());
		UsernamePasswordAuthenticationToken output = new UsernamePasswordAuthenticationToken(userDetails,
				auth.getCredentials(), userDetails.getAuthorities());
		output.setDetails(authentication.getDetails());
		return output;

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
