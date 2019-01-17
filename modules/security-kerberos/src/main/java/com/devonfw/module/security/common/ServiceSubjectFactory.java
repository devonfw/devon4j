package com.devonfw.module.security.common;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.kerberos.authentication.sun.GlobalSunJaasKerberosConfig;
import org.springframework.stereotype.Component;


@Component
public class ServiceSubjectFactory {

  private final KerberosConfigProperties kerberosProperties;


  private Subject subject;

  /**
   * @return subject
   */
  public Subject getSubject() {

    return this.subject;
  }

  /**
   * @param subject new value of {@link #getsubject}.
   */
  public void setSubject(Subject subject) {

    this.subject = subject;
  }


  public ServiceSubjectFactory(KerberosConfigProperties kerberosProperties) throws Exception {
    this.kerberosProperties = kerberosProperties;
    GlobalSunJaasKerberosConfig config = new GlobalSunJaasKerberosConfig();
    config.setDebug(kerberosProperties.isDebug());
    config.afterPropertiesSet();
    System.setProperty("sun.security.krb5.rcache", "none");
  }

  // @Scheduled(fixedRateString = "${kerberos.ticketRefreshSeconds}000")
  // public void refreshServiceSubject() throws LoginException, IOException {
  //
  // if (!this.developMode)
  // this.subject = login();
  // }


  public Subject login() throws LoginException, IOException {

    LoginConfig loginConfig = new LoginConfig(this.kerberosProperties.getKeytabLocation(),
        this.kerberosProperties.getServicePrincipalName(), this.kerberosProperties.isDebug());
    Set<Principal> princ = new HashSet<>(1);
    princ.add(new KerberosPrincipal(this.kerberosProperties.getServicePrincipalName()));
    Subject sub = new Subject(false, princ, new HashSet<>(), new HashSet<>());
    LoginContext lc = new LoginContext("", sub, null, loginConfig);
    lc.login();
    return lc.getSubject();
  }

  /**
   * Normally you need a JAAS config file in order to use the JAAS Kerberos Login Module, with this class it is not
   * needed and you can have different configurations in one JVM.
   */
  private static class LoginConfig extends Configuration {
    private String keyTabLocation;

    private String servicePrincipalName;

    private boolean debug;

    public LoginConfig(String keyTabLocation, String servicePrincipalName, boolean debug) {
      this.keyTabLocation = keyTabLocation;
      this.servicePrincipalName = servicePrincipalName;
      this.debug = debug;
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {

      HashMap<String, String> options = new HashMap<>();
      options.put("useKeyTab", "true");
      options.put("keyTab", this.keyTabLocation);
      options.put("principal", this.servicePrincipalName);
      options.put("storeKey", "true");
      options.put("renewTGT", "true");
      options.put("useTicketCache", "true");
      options.put("doNotPrompt", "true");
      if (this.debug) {
        options.put("debug", "true");
      }
      options.put("isInitiator", "true");

      return new AppConfigurationEntry[] { new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
          AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options), };
    }
  }
}
