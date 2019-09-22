package com.devonfw.module.security.jwt.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
public class JwtConfig {

	private static final Logger LOG = LoggerFactory.getLogger(JwtConfig.class);

	@Bean
	public JwtTokenConfigProperties jwtTokenConfigProperties() {
		return new JwtTokenConfigProperties();

	}

	@Bean
	public KeyStoreConfigProperties keyStoreConfigProperties() {
		return new KeyStoreConfigProperties();
	}

	@Bean
	@DependsOn("keyStoreConfigProperties")
	public KeyStore keyStore() {
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance(keyStoreConfigProperties().getKeystoreType());

			Resource keyStoreLocation = new FileSystemResource(
					new File(keyStoreConfigProperties().getKeyStoreLocation()));
			try (InputStream in = keyStoreLocation.getInputStream()) {

				keyStore.load(in, keyStoreConfigProperties().getPassword().toCharArray()); // "changeit".toCharArray()

				LOG.info("Keystore aliases " + keyStore.aliases().nextElement().toString());
			} catch (IOException | NoSuchAlgorithmException | CertificateException e) {

				e.printStackTrace();
			}
		} catch (KeyStoreException e) {

			e.printStackTrace();
		}
		return keyStore;

	}

	@Bean
	@Qualifier("keyStoreAccess")
	public KeyStoreAccessImpl keyStoreAccess() {
		return new KeyStoreAccessImpl();

	}

}
