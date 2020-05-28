package com.devonfw.module.security.jwt.common.api;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.devonfw.module.security.jwt.common.impl.JwtManagerImpl;
import com.devonfw.module.test.common.base.ComponentTest;
import com.devonfw.test.app.TestApplication;

/**
 * Abstract test class for JWT {@link ComponentTest}s.
 */
@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.NONE)
public abstract class JwtComponentTest extends ComponentTest {

  /** Test login. */
  protected static final String TEST_LOGIN = "john.doe";

  /** Test issuer of JWT. */
  protected static final String TEST_ISSUER = "devon4j";

  /** A role for testing. */
  protected static final String TEST_ROLE_READ_MASTER_DATA = "test.ReadMasterData";

  /** A role for testing. */
  protected static final String TEST_ROLE_SAVE_USER = "test.SaveUser";

  /** Roles for testing. */
  protected static final String TEST_ROLES = TEST_ROLE_READ_MASTER_DATA + "," + TEST_ROLE_SAVE_USER;

  /** Expiration in millis as configured in application.properties */
  protected long EXPIRATION_MS = 4 * 60 * 60 * 1000;

  /** Not-before-delay in millis as configured in application.properties */
  protected long NOT_BEFORE_DELAY_MS = 1 * 60 * 1000;

  /** Test JSON Web Token. */
  protected static final String TEST_JWT = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsInJvbGVzIjoidGVzdC5SZWFkTWFzdGVyRGF0YSx0ZXN0LlNhdmVVc2VyIiwiaXNzIjoiZGV2b240aiIsIm5iZiI6MTU4NzcxMzA1NiwiZXhwIjoxNTg3NzQyMTU2fQ.Onx1p7ZrscBcGRcfVlRncVKLL-j1QMC_n9SgPLq1PjjEJMS6VRz52u0o2lI2LwdPCjve0ujdHfuYHje4jyGgFJwMbL45CpYijl9gFsHzu4WLvw8qH2sZgpe0LSl0ZqdpvpsGtBYqQeL0jdIkz0ppai9U5Ctyr6fxl5LCXeLrQ991dK3n9vJ6i5eEPVL_6TyK5nzTMuJfgxNNLxqxo_drKQGywitQfv0IPpwFdEe5537_buibF1QAOdDCCi-qN8hyZiut0wXmBu1k4yDgw3IwdkCBngi7KAGaHlSI0smRH-wNpsf0pTlYwk1U6AGCCyrxks0WMJG528rtA3HQL5lae6KtyCGW6xfHav9HvmAMU0y7TQlzqadVddxrxdGTP342Y2yjc0HKeKDLLi84Rzp8Z5AkJrG-f0Gcah_ExO9rU9jE3OUpSTmbZAuks56hLC6bik8cZHh9aJ7J-7CG7_5c224fRtlayp0GHPAIYSLZU1SIvN68mgGLpJKGQYNA7l3RFQbfe-h_nfBMAM0Izk7TiHyEM_E7rkh2aBxgkByXdMzm8GQmvDV_is7It2r1DIyHUydYkGAJuJB0sbuX8bkIR3t9ylW-MoGZw5KVUWkNNDlBEzw1bgX6j17JU8al6CWsau9LIt9tSLnrY7YDCpQG1lEXHk95OQCp83wminUnbU8";

  /** Test JSON Invalid Web Token. */
  protected static final String INVALID_TEST_JWT = "AeyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsInJvbGVzIjoidGVzdC5SZWFkTWFzdGVyRGF0YSx0ZXN0LlNhdmVVc2VyIiwiaXNzIjoiZGV2b240aiIsIm5iZiI6MTU4NzcxMzA1NiwiZXhwIjoxNTg3NzQyMTU2fQ.Onx1p7ZrscBcGRcfVlRncVKLL-j1QMC_n9SgPLq1PjjEJMS6VRz52u0o2lI2LwdPCjve0ujdHfuYHje4jyGgFJwMbL45CpYijl9gFsHzu4WLvw8qH2sZgpe0LSl0ZqdpvpsGtBYqQeL0jdIkz0ppai9U5Ctyr6fxl5LCXeLrQ991dK3n9vJ6i5eEPVL_6TyK5nzTMuJfgxNNLxqxo_drKQGywitQfv0IPpwFdEe5537_buibF1QAOdDCCi-qN8hyZiut0wXmBu1k4yDgw3IwdkCBngi7KAGaHlSI0smRH-wNpsf0pTlYwk1U6AGCCyrxks0WMJG528rtA3HQL5lae6KtyCGW6xfHav9HvmAMU0y7TQlzqadVddxrxdGTP342Y2yjc0HKeKDLLi84Rzp8Z5AkJrG-f0Gcah_ExO9rU9jE3OUpSTmbZAuks56hLC6bik8cZHh9aJ7J-7CG7_5c224fRtlayp0GHPAIYSLZU1SIvN68mgGLpJKGQYNA7l3RFQbfe-h_nfBMAM0Izk7TiHyEM_E7rkh2aBxgkByXdMzm8GQmvDV_is7It2r1DIyHUydYkGAJuJB0sbuX8bkIR3t9ylW-MoGZw5KVUWkNNDlBEzw1bgX6j17JU8al6CWsau9LIt9tSLnrY7YDCpQG1lEXHk95OQCp83wminUnbU8";

  @Inject
  private JwtManagerImpl jwtManager;

  private Clock clock;

  /**
   * @return the {@link JwtManager}.
   */
  public JwtManager getJwtManager() {

    return this.jwtManager;
  }

  /**
   * Adjustes the {@link Clock} of {@link JwtManager} to a fixed date in the past shortly after the {@link #TEST_JWT}
   * was created (time-travel).
   */
  protected void adjustClock() {

    this.clock = this.jwtManager.getClock();
    this.jwtManager.setClock(Clock.fixed(Instant.ofEpochSecond(1587716116L), ZoneOffset.UTC));
  }

  /**
   * Resets the {@link Clock} of {@link JwtManager} to undo the changes of {@link #adjustClock()}.
   */
  protected void resetClock() {

    assertThat(this.clock).as("Original clock stored on adjustClock()").isNotNull();
    this.jwtManager.setClock(this.clock);
    this.clock = null;
  }

  /**
   * @param date the {@link Date}. May be {@code null}.
   * @param millis the milliseconds to add.
   * @return the sum. May be {@code null}.
   */
  protected Date addToDate(Date date, long millis) {

    return new Date(date.getTime() + millis);
  }

}
