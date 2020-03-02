package com.devonfw.module.security.keystore.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Factory class to load KeyStore
 *
 * @since 3.3.0
 */
@Named
public class KeyStoreAccessFactory {

  private final Map<String, KeyStoreAccess> keyMap;

  /**
   * The constructor.
   */
  public KeyStoreAccessFactory() {

    super();
    this.keyMap = new HashMap<>();
  }

  /**
   * Loads all the KeyStore with {@link KeyStoreAccess}}
   *
   * @param keys the {@link List} of {@link KeyStoreAccess}s to register the keyStore.
   */
  @Inject
  public void setKeys(List<KeyStoreAccess> keys) {

    for (KeyStoreAccess key : keys) {
      this.keyMap.put(key.getAlias(), key);
    }
  }

  /**
   * Gets the instance of {@link KeyStoreAccess} KeyStore by alias
   *
   * @param alias
   * @return {@link KeyStoreAccess}
   */
  public KeyStoreAccess getKeys(String alias) {

    return this.keyMap.get(alias);
  }
}
