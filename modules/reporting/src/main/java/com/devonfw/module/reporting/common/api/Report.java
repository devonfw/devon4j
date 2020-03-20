package com.devonfw.module.reporting.common.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to encapsulate a report
 *
 * @param <T> type of the data that is provided to be included in the report
 */
public class Report<T> {
  private String name;

  private String dataSourceName;

  private List<T> data;

  private String templatePath;

  private HashMap<String, Object> params;

  /**
   * @return name
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getName}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return dataSourceName
   */
  public String getDataSourceName() {

    return this.dataSourceName;
  }

  /**
   * @param dataSourceName new value of {@link #getDataSourceName}.
   */
  public void setDataSourceName(String dataSourceName) {

    this.dataSourceName = dataSourceName;
  }

  /**
   * @return data
   */
  public List<T> getData() {

    return this.data;
  }

  /**
   * @param data new value of {@link #getData}.
   */
  public void setData(List<T> data) {

    this.data = data;
  }

  /**
   * @return templatePath
   */
  public String getTemplatePath() {

    return this.templatePath;
  }

  /**
   * @param templatePath new value of {@link #getTemplatePath}.
   */
  public void setTemplatePath(String templatePath) {

    this.templatePath = templatePath;
  }

  /**
   * @return params
   */
  public Map<String, Object> getParams() {

    return this.params;
  }

  /**
   * @param params new value of {@link #getParams}.
   */
  public void setParams(HashMap<String, Object> params) {

    this.params = params;
  }
}
