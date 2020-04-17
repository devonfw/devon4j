package com.devonfw.module.security.keystore.common.api;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.module.security.TestApplication;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * {@link ComponentTest} for {@link KeyStoreAccess}.
 */
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = WebEnvironment.NONE)
public class KeyStoreAccessTest extends ComponentTest {

  private static final String ALIAS = "devonfw";

  @Inject
  private KeyStoreAccess keyStoreAccess;

  /**
   * Test of {@link KeyStoreAccess#getPrivateKey(String)}.
   */
  @Test
  public void testGetPrivateKey() {

    PrivateKey privateKey = this.keyStoreAccess.getPrivateKey(ALIAS);
    assertThat(privateKey).isNotNull();
    assertThat(privateKey.getAlgorithm()).isEqualTo("RSA");
    assertThat(privateKey.getFormat()).isEqualTo("PKCS#8");
    assertThat(privateKey.getEncoded()).hasSize(2374);
  }

  /**
   * Test of {@link KeyStoreAccess#getCertificate(String)}.
   */
  @Test
  public void testGetCertificate() {

    Certificate certificate = this.keyStoreAccess.getCertificate(ALIAS);
    assertThat(certificate).isNotNull();
    assertThat(certificate.getType()).isEqualTo("X.509");
    assertThat(certificate).isInstanceOf(X509Certificate.class);
    X509Certificate x509 = (X509Certificate) certificate;
    assertThat(x509.getSigAlgName()).isEqualTo("SHA384withRSA");
    assertThat(x509.getSubjectDN().getName())
        .isEqualTo("CN=John Doe, OU=devon4j, O=devonfw, L=Tuscon, ST=Arizona, C=us");
    assertThat(certificate.getPublicKey()).isEqualTo(this.keyStoreAccess.getPublicKey(ALIAS));
  }

  /**
   * Test of {@link KeyStoreAccess#getPublicKey(String)}.
   */
  @Test
  public void testGetPublicKey() {

    PublicKey publicKey = this.keyStoreAccess.getPublicKey(ALIAS);
    assertThat(publicKey).isNotNull();
    assertThat(publicKey.getAlgorithm()).isEqualTo("RSA");
    assertThat(publicKey.getFormat()).isEqualTo("X.509");
    assertThat(publicKey.getEncoded()).hasSize(550);
  }

}
