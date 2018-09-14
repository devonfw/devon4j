package com.devonfw.module.jpa.dataaccess.api;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Workaround if forced to allow more IN-clause values than supported by your database if really required. Please use
 * this feature with extreme caution. Databases do have their reasons for such limitations. Using to many values might
 * also kill your statement cache in the database.
 *
 * @since 3.0.0
 */
class DatabaseConfigProperties {

  private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfigProperties.class);

  private static DatabaseConfigProperties instance;

  @Value("${database.query.in-clause.max-values:2147483647}")
  private int maxSizeOfInClause;

  /** Die maximale Anzahl von Werten, welche Oracle in einer IN-Klausel verarbeiten kann. */
  public static final int MAX_SIZE_OF_IN_CLAUSE_IN_ORACLE = 1000;

  /**
   * The constructor.
   */
  public DatabaseConfigProperties() {

    super();
    this.maxSizeOfInClause = Integer.MAX_VALUE;
  }

  @PostConstruct
  protected void initialize() {

    if (instance == null) {
      instance = this;
      LOG.debug("Registering DB configuration instance {}.", this);
    } else {
      LOG.warn("Instance {} has already been registered and can not be replaced by {}", instance, this);
    }
  }

  public int getMaxSizeOfInClause() {

    return this.maxSizeOfInClause;
  }

  /**
   * @return instance
   */
  public static DatabaseConfigProperties getInstance() {

    if (instance == null) {
      new DatabaseConfigProperties();
    }
    return instance;
  }

  @Override
  public String toString() {

    return getClass().getSimpleName() + "[maxSizeOfInClause=" + this.maxSizeOfInClause + "]";
  }

}
