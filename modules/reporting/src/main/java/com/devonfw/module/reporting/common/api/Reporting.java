package com.devonfw.module.reporting.common.api;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * This is the interface for a simple facade to generate a report based on Jasper Reports library.
 *
 * @param <T> type of the data that is provided to be included in the report.
 */
public interface Reporting<T> {

  /**
   * Generates a report file with the given data, the template located in the templatePath and the type of file.
   * specified.
   *
   * @param report the {@link Report} that encapsulates the data to be included in the report, the location of the
   *        report template and the values of the parameters defined in the report template (in case template uses
   *        parameters).
   * @param file the file where the report must be created.
   * @param format - report format
   */
  public void generateReport(Report report, File file, String format);

  /**
   * Generates a report stream with the given data, the template located in the templatePath and the type of file.
   *
   * @param report the {@link Report} that encapsulates the data to be included in the report, the location of the
   *        report template and the values of the parameters defined in the report template (in case template uses
   *        parameters).
   * @param stream the stream where the report will be written.
   * @param format - report format
   */
  public void generateReport(Report report, OutputStream stream, String format);

  /**
   * Generates a file with a main report that stores other sub-reports.
   *
   * @param masterReport the main {@link Report}.
   * @param reports a list of {@link Report} reports to be included within the main report.
   * @param file the file where the report must be created.
   * @param format - report format
   */
  public void generateSubreport(Report masterReport, List<Report> reports, File file, String format);

  /**
   * Generates a stream with a main report that stores other sub-reports.
   *
   * @param masterReport the main {@link Report}.
   * @param reports a list of {@link Report} reports to be included within the main report.
   * @param stream the stream to manage the resultant report.
   * @param format - report format
   */
  public void generateSubreport(Report masterReport, List<Report> reports, OutputStream stream, String format);

  /**
   * Generates a report file that contains a concatenation of reports.
   *
   * @param reports the list of reports to be included in the report file.
   * @param file the file where the report must be created.
   * @param format - report format
   */
  public void concatenateReports(List<Report> reports, File file, String format);

  /**
   * Generates a stream that contains a concatenation of reports.
   *
   * @param reports the list of reports to be included in the report stream.
   * @param stream the stream to manage the resultant report.
   * @param format - report format
   */
  public void concatenateReports(List<Report> reports, OutputStream stream, String format);
}
