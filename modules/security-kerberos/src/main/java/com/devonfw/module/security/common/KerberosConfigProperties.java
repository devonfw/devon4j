package com.devonfw.module.security.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 *
 *
 */
@Configuration
public class KerberosConfigProperties {
	private boolean debug = true;

	@Value("${kerberos.keytab-location}")
	private String keytabLocation;

	@Value("${kerberos.service-principal}")
	private String servicePrincipalName;

	public boolean isDebug() {

		return this.debug;
	}

	public void setDebug(boolean debug) {

		this.debug = debug;
	}

	public String getServicePrincipalName() {

		return this.servicePrincipalName;
	}

	public void setServicePrincipalName(String servicePrincipalName) {

		this.servicePrincipalName = servicePrincipalName;
	}

	public String getKeytabLocation() {
		return keytabLocation;
	}

	public void setKeytabLocation(String keytabLocation) {
		this.keytabLocation = keytabLocation;
	}
}
