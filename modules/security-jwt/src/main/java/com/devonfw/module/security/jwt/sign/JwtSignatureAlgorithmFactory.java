package com.devonfw.module.security.jwt.sign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Factory class to load algorithms
 *
 * @since 3.3.0
 */
@Named
public class JwtSignatureAlgorithmFactory {

  private final Map<String, JwtSignatureAlgorithm> algorithmMap;

  /**
   * The constructor.
   */
  public JwtSignatureAlgorithmFactory() {

    super();
    this.algorithmMap = new HashMap<>();
  }

  /**
   * Loads all the algorithms with {@link JwtSignatureAlgorithm}}
   *
   * @param algorithms the {@link List} of {@link JwtSignatureAlgorithm}s to register the algorithm.
   */
  @Inject
  public void setAlgorithms(List<JwtSignatureAlgorithm> algorithms) {

    for (JwtSignatureAlgorithm algorithm : algorithms) {
      this.algorithmMap.put(algorithm.getName(), algorithm);
    }

  }

  /**
   * Gets the instance of {@link JwtSignatureAlgorithm} algorithm by algorithm name
   *
   * @param name
   * @return {@link JwtSignatureAlgorithm}
   */
  public JwtSignatureAlgorithm getAlgorithms(String name) {

    return this.algorithmMap.get(name);
  }
}
