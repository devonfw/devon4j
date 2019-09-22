package com.devonfw.module.security.jwt.config;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import com.devonfw.module.security.jwt.util.SignatureAlgorithm;

/**
 * Interface which provides access to Keystore functionality. Check {@link KeyStoreAccessImpl} for implementation.
 *
 */
public interface KeyStoreAccess {

	 KeyStore getKeyStore();

	 PublicKey getPublicKey();

	 List<SignatureAlgorithm> loadAllAlgorithmList();

	 PrivateKey getPrivateKey(String alias, String password);

}
