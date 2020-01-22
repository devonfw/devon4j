package com.devonfw.module.reporting.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.devonfw.module.reporting.common.ReportingModuleApp;
import com.devonfw.module.reporting.common.api.Report;
import com.devonfw.module.reporting.common.api.Reporting;
import com.devonfw.module.reporting.common.config.ReportingConstants;
import com.devonfw.module.test.common.base.ComponentTest;

import ch.qos.logback.classic.Level;

/**
 * Test class to test the concatenated reports functionality
 *
 */
@SpringBootTest(classes = ReportingModuleApp.class)
public class ConcatenatedReportingTest extends ComponentTest {
  @SuppressWarnings("rawtypes")
  @Inject
  Reporting<HashMap> reportManager;

  private Resource template = new ClassPathResource("ConcatenatedReportingTest/SimpleReportTest.jrxml");

  private static Random rnd = new Random();

  @SuppressWarnings("rawtypes")
  List<Report> reports = null;

  private OutputStream stream = null;

  ch.qos.logback.classic.Logger loggerJasper = (ch.qos.logback.classic.Logger) LoggerFactory
      .getLogger("net.sf.jasperreports");

  ch.qos.logback.classic.Logger loggerApacheCommons = (ch.qos.logback.classic.Logger) LoggerFactory
      .getLogger("org.apache.commons.digester.Digester");

  /**
   * @throws IOException if the template for the report can not be found.
   */
  @BeforeEach
  public void init() throws IOException {

    this.loggerJasper.setLevel(Level.ERROR);
    this.loggerApacheCommons.setLevel(Level.ERROR);

    this.reports = getReportsList();
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with pdf format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedPdfReport() throws IOException {

    File pdf = File.createTempFile("concReports_", ".pdf");
    this.reportManager.concatenateReports(this.reports, pdf, ReportingConstants.PDF);
    assertThat(pdf.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with xls format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedXlsReport() throws IOException {

    File excel = File.createTempFile("concReports_", ".xls");
    this.reportManager.concatenateReports(this.reports, excel, ReportingConstants.XLS);
    assertThat(excel.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with xlsx format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedXlsxReport() throws IOException {

    File excel_xlsx = File.createTempFile("concReports_", ".xlsx");
    this.reportManager.concatenateReports(this.reports, excel_xlsx, ReportingConstants.XLSX);
    assertThat(excel_xlsx.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with html format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedHtmlReport() throws IOException {

    File html = File.createTempFile("concReports_", ".html");
    this.reportManager.concatenateReports(this.reports, html, ReportingConstants.HTML);
    assertThat(html.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with ods format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedOdsReport() throws IOException {

    File ods = File.createTempFile("concReports_", ".ods");
    this.reportManager.concatenateReports(this.reports, ods, ReportingConstants.ODS);
    assertThat(ods.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with odt format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedOdtReport() throws IOException {

    File odt = File.createTempFile("concReports_", ".odt");
    this.reportManager.concatenateReports(this.reports, odt, ReportingConstants.ODT);
    assertThat(odt.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with doc format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedDocReport() throws IOException {

    File doc = File.createTempFile("concReports_", ".doc");
    this.reportManager.concatenateReports(this.reports, doc, ReportingConstants.WORD);
    assertThat(doc.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with docx format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedDocxReport() throws IOException {

    File docx = File.createTempFile("concReports_", ".docx");
    this.reportManager.concatenateReports(this.reports, docx, ReportingConstants.DOCX);
    assertThat(docx.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with pptx format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedPptxReport() throws IOException {

    File pptx = File.createTempFile("concReports_", ".pptx");
    this.reportManager.concatenateReports(this.reports, pptx, ReportingConstants.PPTX);
    assertThat(pptx.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with rtf format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedRtfReport() throws IOException {

    File rtf = File.createTempFile("concReports_", ".rtf");
    this.reportManager.concatenateReports(this.reports, rtf, ReportingConstants.RTF);
    assertThat(rtf.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with csv format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedCsvReport() throws IOException {

    File csv = File.createTempFile("concReports_", ".csv");
    this.reportManager.concatenateReports(this.reports, csv, ReportingConstants.CSV);
    assertThat(csv.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with txt format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedTxtReport() throws IOException {

    File txt = File.createTempFile("concReports_", ".txt");
    this.reportManager.concatenateReports(this.reports, txt, ReportingConstants.TEXT);
    assertThat(txt.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a stream with pdf format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateConcatenatedStreamReport() throws IOException {

    this.stream = new ByteArrayOutputStream();
    this.reportManager.concatenateReports(this.reports, this.stream, ReportingConstants.PDF);
    assertThat(((ByteArrayOutputStream) this.stream).size()).isGreaterThan(0);
  }

  @SuppressWarnings("javadoc")
  @AfterEach
  public void end() throws IOException {

    if (this.stream != null)
      this.stream.close();
  }

  @SuppressWarnings({ "javadoc", "rawtypes", "unchecked" })
  public static List<HashMap> createList() {

    List lst = new ArrayList();
    lst.add(createItem("Tom Waits", 92));
    lst.add(createItem("Nick Cave", 97));
    lst.add(createItem("PJ Harvey", 95));

    return lst;
  }

  @SuppressWarnings({ "javadoc", "rawtypes", "unchecked" })
  public static Object createItem(String name, int rating) {

    Map map = new HashMap();
    map.put("ID", rnd.nextLong());
    map.put("Name", name);
    map.put("Rating", rating);
    return map;
  }

  @SuppressWarnings({ "rawtypes", "unchecked", "javadoc" })
  public List<Report> getReportsList() throws IOException {

    HashMap<String, Object> params = null;
    List<Report> reportsList = new ArrayList<>();

    Report report1 = new Report();
    params = new HashMap<>();
    params.put("ReportTitle", "Report 1");
    params.put("ReportDescription", "First concatenated report.");
    report1.setData(createList());
    report1.setTemplatePath(this.template.getURI().getPath());
    report1.setParams(params);

    Report report2 = new Report();
    params = new HashMap<>();
    params.put("ReportTitle", "Report 2");
    params.put("ReportDescription", "Second concatenated report.");
    report2.setData(createList());
    report2.setTemplatePath(this.template.getURI().getPath());
    report2.setParams(params);

    Report report3 = new Report();
    params = new HashMap<>();
    params.put("ReportTitle", "Report 3");
    params.put("ReportDescription", "Third concatenated report.");
    report3.setData(createList());
    report3.setTemplatePath(this.template.getURI().getPath());
    report3.setParams(params);

    reportsList.add(report1);
    reportsList.add(report2);
    reportsList.add(report3);

    return reportsList;
  }

}
