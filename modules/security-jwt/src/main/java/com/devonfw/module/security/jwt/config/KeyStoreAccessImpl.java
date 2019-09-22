package com.devonfw.module.security.jwt.config;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devonfw.module.security.jwt.util.SignatureAlgorithm;

/**
 * Implementation of KeystorAccess functionalities
 *
 */

public class KeyStoreAccessImpl implements KeyStoreAccess {

	private static final Logger LOG = LoggerFactory.getLogger(KeyStoreAccessImpl.class);

	@Inject
	KeyStoreConfigProperties keyStoreProps;

	@Inject
	JwtTokenConfigProperties tokenProps;

	private List<SignatureAlgorithm> algorithmList = loadAllAlgorithmList();;

	public List<SignatureAlgorithm> getAlgorithmList() {
		return algorithmList;
	}

	@Inject
	private KeyStore keyStore;

	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}

	@Override
	public KeyStore getKeyStore() {
		Objects.requireNonNull(this.keyStore, "KeyStore");
		return this.keyStore;
	}

	public List<SignatureAlgorithm> loadAllAlgorithmList() {
		List<SignatureAlgorithm> algoLists = Arrays.asList(SignatureAlgorithm.values());
		LOG.info("AlgoLists-- " + algoLists);
		return algoLists;

	}

	@Override
	public PublicKey getPublicKey() {
		PublicKey publicKey = null;
		try {
			Certificate certificate = keyStore.getCertificate(keyStoreProps.getKeyAlias());
			publicKey = certificate.getPublicKey();
		} catch (KeyStoreException e) {

			LOG.error(e.getMessage());

		}
		return publicKey;
	}

	@Override
	public PrivateKey getPrivateKey(String alias, String password) {
		try {
			Key key = keyStore.getKey(alias, "changeit".toCharArray());
			 if (key instanceof PrivateKey) {
				 return (PrivateKey) key;
			    }
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
