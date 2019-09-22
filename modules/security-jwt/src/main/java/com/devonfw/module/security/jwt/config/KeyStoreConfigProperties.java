package com.devonfw.module.security.jwt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains properties related with keystore.
 *
 */

@ConfigurationProperties(prefix="server.ssl")
public class KeyStoreConfigProperties {


	private String keyStoreLocation;

	private String keystoreType;

	private String password;

	private String keyAlias;

	private String keyPassword;

	Map<String,String> customPropMap=new HashMap<>();

	public String getKeyStoreLocation() {
		return keyStoreLocation;
	}

	public void setKeyStoreLocation(String keyStoreLocation) {
		this.keyStoreLocation = keyStoreLocation;
	}

	public String getKeystoreType() {
		return keystoreType;
	}

	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, String> getCustomPropMap() {
		return customPropMap;
	}

	public void setCustomPropMap(Map<String, String> customPropMap) {
		this.customPropMap = customPropMap;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public String getKeyPassword() {
		return keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}



}
