package com.devonfw.module.security.jwt.util;

import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import com.devonfw.module.security.jwt.config.KeyStoreAccess;
import com.devonfw.module.security.jwt.config.KeyStoreAccessImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to take request object and extract token and validate it.
 *
 */
@Configuration
public class TokenExtractor {

	private static final Logger LOG = LoggerFactory.getLogger(TokenExtractor.class);

	private ObjectMapper objectMapper;

	private KeyStoreAccess keyStoreAccess;

	private Map<String, Object> tokenClaims;

	public TokenExtractor(KeyStoreAccess keyStoreAccess,ObjectMapper objectMapper) {
		this.keyStoreAccess = keyStoreAccess;
		this.objectMapper=objectMapper;
	}

	public Authentication validateTokenAndSignature(String token) {

		Authentication authentication = null;
		// get algorithm from header
		// create signatureverifier of particular type
		// extract token
		// deserialize it
		// verify signature
		// TODO:validate timestamp
		// TODO: take data from token and create authentication

		String algorithms = JwtHelper.headers(token).get("alg");

		String algorithmFamilyType = getAlgorithmFamilyType(algorithms);
		if (algorithmFamilyType == null) {
			// throw exception
		}

		SignatureVerifier verifier = signatureVerifierFactory(((RSAPublicKey)this.keyStoreAccess.getPublicKey()),
				algorithmFamilyType);

		Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);

		String claims = jwt.getClaims();
		try {
			tokenClaims = this.objectMapper.readValue(claims, Map.class);

			LOG.info("Token Claims " + tokenClaims.toString());

			Instant now = Instant.now();

			if(tokenClaims.get("nbf")!=null) {
				Instant nbf = Instant.ofEpochSecond(((Number) tokenClaims.get("nbf")).longValue());
				if (now.isBefore(nbf)) {
					// Throw exception
					LOG.info("Token is not valid before " + nbf);
				}

			}

			if(tokenClaims.get("exp") !=null) {
				Instant expiration_time = Instant.ofEpochSecond(((Number) tokenClaims.get("exp")).longValue());

				if (now.isAfter(expiration_time)) {
					// Throw exception
					LOG.info("Token is expired");
				}
			}


			String user = tokenClaims.get("sub").toString();
			// If token do not throw any exception above it is valid and we need to create
			// authentication object from it

			authentication = new UsernamePasswordAuthenticationToken(user,
					tokenClaims.get("roles").toString());
		} catch (IOException e) {

			LOG.error(e.getMessage());
		}
		return authentication;
	}

	private String getAlgorithmFamilyType(String algorithms) {
		StringBuilder familyAlgorithm = null;
		List<SignatureAlgorithm> algos = this.keyStoreAccess.loadAllAlgorithmList();
		for (SignatureAlgorithm algorithm : algos) {
			if (algorithm.getValue().equalsIgnoreCase(algorithms)) {
				LOG.info("Algorithm exists--");
				familyAlgorithm = new StringBuilder(algorithm.getFamilyName());
				LOG.info("Parent algo is " + familyAlgorithm.toString());
				return familyAlgorithm.toString();
			}
		}
		return null;
	}

	public SignatureVerifier signatureVerifierFactory(PublicKey publicKey, String algorithm) {
		switch (algorithm) {
		case "Elliptic Curve":
			return null;

		case "HMAC":
			return null;

		case "RSA":
			return new RsaVerifier((RSAPublicKey) publicKey);

		default:
			return new RsaVerifier((RSAPublicKey) publicKey);
		}
	}

}
