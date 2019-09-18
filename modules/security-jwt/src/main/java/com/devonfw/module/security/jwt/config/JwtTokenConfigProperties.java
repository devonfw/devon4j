package com.devonfw.module.security.jwt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class propeties realted with Jwt token such as Issuer, expiration hour etc.
 *
 */

@ConfigurationProperties(prefix="security.authentication.jwt")
public class JwtTokenConfigProperties {

	Map<String,String> propsAsMap=new HashMap<>();

	public Map<String, String> getPropsAsMap() {
		return propsAsMap;
	}

	public void setPropsAsMap(Map<String, String> propsAsMap) {
		this.propsAsMap = propsAsMap;
	}

}
