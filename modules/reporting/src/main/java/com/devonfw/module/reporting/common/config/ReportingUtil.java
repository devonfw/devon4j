package com.devonfw.module.reporting.common.config;

import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.devonfw.module.reporting.common.api.exception.ReportingException;

/**
 * This is the implementation of several basic functionalities associated to Jasper Reports Library.
 */
@Named
public class ReportingUtil {

  private static final Log log = LogFactory.getLog(ReportingUtil.class);

  @Inject
  private ReportingConfigProperties reportingConfigProperties;

  /**
   * Returns the data provided as JRDataSource type in order to fill the report.
   *
   * @param data the data to be included in the report.
   * @return {@link JRDataSource}
   */
  public JRDataSource getDataSource(Collection<? extends Object> data) {

    if (data.size() == 0) {
      return new JREmptyDataSource();
    } else {
      return new JRMapCollectionDataSource((Collection<Map<String, ?>>) data);
    }
  }

  /**
   * Configures a Jasper Reports Exporter setting its input (a JasperPrint object) and output (a FileOutputStream
   * object)
   *
   * @param exporter the {@linkplain JRAbstractExporter} to configure
   * @param print the {@link JasperPrint} object to configure as the exporter input it can be a list of JasperPrint
   *        objects.
   * @param stream the {@link OutputStream} to configure as the exporter output.
   * @param format the {@link ReportingConstants} according to which the exporter will be configured.
   * @throws ReportingException if the configuration process of the exporter fails.
   */
  public void configureExporter(JRAbstractExporter exporter, Object print, OutputStream stream, String format)
      throws ReportingException {

    ExporterInput exporterInput = print instanceof List ? SimpleExporterInput.getInstance((List<JasperPrint>) print)
        : new SimpleExporterInput((JasperPrint) print);
    ExporterOutput exporterOutput = null;
    if (print instanceof List || print instanceof JasperPrint) {

      switch (format) {
        case ReportingConstants.XLS:
          exporterOutput = new SimpleOutputStreamExporterOutput(stream);
          exporter.setConfiguration(getXlsConfiguration());
          break;
        case ReportingConstants.CSV:
        case ReportingConstants.WORD:
        case ReportingConstants.RTF:
          exporterOutput = new SimpleWriterExporterOutput(stream);
          break;
        case ReportingConstants.TEXT:
          exporterOutput = new SimpleWriterExporterOutput(stream);
          exporter.setConfiguration(getTxtConfiguration());
          break;
        case ReportingConstants.XLSX:
          exporterOutput = new SimpleOutputStreamExporterOutput(stream);
          exporter.setConfiguration(getXlsxConfiguration());
          break;
        case ReportingConstants.HTML:
          exporterOutput = new SimpleHtmlExporterOutput(stream);
          break;
        default:
          exporterOutput = new SimpleOutputStreamExporterOutput(stream);
      }

      exporter.setExporterInput(exporterInput);
      exporter.setExporterOutput(exporterOutput);
    } else {
      throw new InvalidParameterException(
          "In order to configure the JRAbstractExporter the object supplied must be of type JasperPrint or a List of JasperPrint objects");
    }
  }

  private SimpleTextReportConfiguration getTxtConfiguration() throws ReportingException {

    SimpleTextReportConfiguration txtConfiguration = new SimpleTextReportConfiguration();
    try {
      txtConfiguration.setCharWidth(Float.parseFloat(this.reportingConfigProperties.getTxtConfig().get("charwidth")));
      txtConfiguration.setCharHeight(Float.parseFloat(this.reportingConfigProperties.getTxtConfig().get("charheight")));
      txtConfiguration
          .setPageWidthInChars(Integer.parseInt(this.reportingConfigProperties.getTxtConfig().get("pagewidthinchars")));
      txtConfiguration.setPageHeightInChars(
          Integer.parseInt(this.reportingConfigProperties.getTxtConfig().get("pageheightinchars")));
      return txtConfiguration;
    } catch (NumberFormatException e) {
      log.error(e.getMessage(), e);
      throw new ReportingException(e, "Some txtConfig parameter in application.properties may have an invalid value.");
    }
  }

  private SimpleXlsReportConfiguration getXlsConfiguration() {

    SimpleXlsReportConfiguration xlsConfiguration = new SimpleXlsReportConfiguration();
    xlsConfiguration.setOnePagePerSheet(false);
    return xlsConfiguration;
  }

  private SimpleXlsxReportConfiguration getXlsxConfiguration() {

    SimpleXlsxReportConfiguration xlsxConfiguration = new SimpleXlsxReportConfiguration();
    xlsxConfiguration.setOnePagePerSheet(false);
    return xlsxConfiguration;
  }
}
