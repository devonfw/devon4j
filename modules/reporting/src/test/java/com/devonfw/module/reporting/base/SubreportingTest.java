package com.devonfw.module.reporting.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.devonfw.module.reporting.common.ReportingModuleApp;
import com.devonfw.module.reporting.common.api.Report;
import com.devonfw.module.reporting.common.api.Reporting;
import com.devonfw.module.reporting.common.config.ReportingConstants;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * Test class to test the subreports functionality
 *
 */
@SpringBootTest(classes = ReportingModuleApp.class)
public class SubreportingTest extends ComponentTest {

  @SuppressWarnings("rawtypes")
  @Inject
  Reporting<HashMap> reportManager;

  @SuppressWarnings("rawtypes")
  Report masterReport = null;

  @SuppressWarnings("rawtypes")
  List<Report> subreports = null;

  private final String CITY = "Valencia";

  private OutputStream stream = null;

  private Resource masterTemplate = new ClassPathResource("SubreportingTest/MasterReport.jrxml");

  private Resource productTemplate = new ClassPathResource("SubreportingTest/ProductReport.jrxml");

  private Resource addressTemplate = new ClassPathResource("SubreportingTest/AddressReport.jrxml");

  @SuppressWarnings({ "javadoc", "rawtypes", "unchecked" })
  @BeforeEach
  public void init() throws IOException {

    this.masterReport = new Report();
    this.subreports = new ArrayList<>();
    HashMap<String, Object> allParams = new HashMap<>();

    // subreport Products
    Report products = new Report();
    HashMap<String, Object> productsParams = new HashMap<>();
    productsParams.put("City", this.CITY);
    allParams.putAll(productsParams);
    products.setName("Products");
    products.setDataSourceName("ProductsDS");
    products.setData(getProductsAsMapList());
    products.setTemplatePath(this.productTemplate.getURI().getPath());
    this.subreports.add(products);

    // subreport Address
    Report address = new Report();
    address.setName("Address");
    address.setDataSourceName("AddressDS");
    address.setData(getAddressAsMapList());
    address.setTemplatePath(this.addressTemplate.getURI().getPath());
    this.subreports.add(address);

    // subreport Address2
    Report address2 = new Report();
    address2.setName("Address2");
    address2.setDataSourceName("Address2DS");
    address2.setData(getAddress2AsMapList());
    address2.setTemplatePath(this.addressTemplate.getURI().getPath());
    this.subreports.add(address2);

    // master report
    this.masterReport.setParams(allParams);
    this.masterReport.setName("MasterTest");
    this.masterReport.setData(getCitiesAsMapList(new String[] { this.CITY }));
    this.masterReport.setTemplatePath(this.masterTemplate.getURI().getPath());
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with pdf format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportPdfFile() throws IOException {

    File file = File.createTempFile("subreport_", ".pdf");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.PDF);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with xls format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportExcelFile() throws IOException {

    File file = File.createTempFile("subreport_", ".xls");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.XLS);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with xlsx format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportXlsxFile() throws IOException {

    File file = File.createTempFile("subreport_", ".xlsx");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.XLSX);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with html format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportHtmlFile() throws IOException {

    File file = File.createTempFile("subreport_", ".html");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.HTML);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with ods format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportOdsFile() throws IOException {

    File file = File.createTempFile("subreport_", ".ods");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.ODS);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with odt format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportOdtFile() throws IOException {

    File file = File.createTempFile("subreport_", ".odt");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.ODT);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with doc format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportDocFile() throws IOException {

    File file = File.createTempFile("subreport_", ".doc");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.WORD);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with docx format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportDocxFile() throws IOException {

    File file = File.createTempFile("subreport_", ".docx");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.DOCX);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with pptx format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportPptxFile() throws IOException {

    File file = File.createTempFile("subreport_", ".pptx");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.PPTX);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with rtf format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportRtfFile() throws IOException {

    File file = File.createTempFile("subreport_", ".rtf");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.RTF);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with csv format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportCsvFile() throws IOException {

    File file = File.createTempFile("subreport_", ".csv");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.CSV);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a file with txt format.
   *
   * @throws IOException if the temporal file can not be created.
   */
  @Test
  public void generateSubreportTxtFile() throws IOException {

    File file = File.createTempFile("subreport_", ".txt");
    this.reportManager.generateSubreport(this.masterReport, this.subreports, file, ReportingConstants.TEXT);
    assertThat(file.length()).isGreaterThan(0);
  }

  /**
   * Test that checks the creation of a concatenation of reports in a stream.
   *
   */
  @Test
  public void generateSubrerportStream() {

    this.stream = new ByteArrayOutputStream();
    this.reportManager.generateSubreport(this.masterReport, this.subreports, this.stream, ReportingConstants.PDF);
    assertThat(((ByteArrayOutputStream) this.stream).size()).isGreaterThan(0);
  }

  @SuppressWarnings("javadoc")
  @AfterEach
  public void end() throws IOException {

    if (this.stream != null)
      this.stream.close();
  }

  @SuppressWarnings("rawtypes")
  private List<HashMap> getCitiesAsMapList(String[] cities) {

    List<HashMap> citiesList = new ArrayList<>();
    for (String city : cities) {
      citiesList.add(createCityItem(city));
    }
    return citiesList;
  }

  private HashMap<String, String> createCityItem(String cityName) {

    HashMap<String, String> cityItem = new HashMap<>();
    cityItem.put("City", cityName);
    return cityItem;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static List<HashMap> getProductsAsMapList() {

    List productsList = new ArrayList();
    productsList.add(createProductItem(0, "Iron Iron", 3f, 8.10f));
    productsList.add(createProductItem(1, "Chair Shoe", 4f, 37.20f));
    productsList.add(createProductItem(4, "Ice Tea Shoe", 48f, 57.60f));
    productsList.add(createProductItem(5, "Clock Clock", 12f, 35.40f));
    productsList.add(createProductItem(6, "Ice Tea Chair", 23f, 14.70f));
    productsList.add(createProductItem(7, "Telephone Shoe", 5f, 12.60f));
    productsList.add(createProductItem(8, "Ice Tea Clock", 14f, 33.90f));
    productsList.add(createProductItem(10, "Telephone Ice Tea", 2f, 30.60f));
    productsList.add(createProductItem(11, "Telephone Iron", 17f, 13.20f));
    productsList.add(createProductItem(12, "Clock Ice Tea", 17f, 50.40f));
    productsList.add(createProductItem(14, "Telephone Iron", 15f, 18.60f));
    productsList.add(createProductItem(23, "Shoe Chair", 10f, 11.40f));
    productsList.add(createProductItem(24, "Chair Shoe", 16f, 10.80f));
    productsList.add(createProductItem(26, "Shoe Shoe", 39f, 75.60f));
    productsList.add(createProductItem(30, "Shoe Iron", 19f, 34.80f));
    productsList.add(createProductItem(31, "Ice Tea Telephone", 2f, 7.20f));
    productsList.add(createProductItem(33, "Iron Chair", 10f, 27.30f));
    productsList.add(createProductItem(35, "Telephone Shoe", 17f, 11.40f));
    productsList.add(createProductItem(36, "Ice Tea Iron", 24f, 4.80f));
    productsList.add(createProductItem(38, "Clock Ice Tea", 3f, 32.40f));
    productsList.add(createProductItem(41, "Clock Ice Tea", 10f, 30.90f));
    productsList.add(createProductItem(48, "Clock Clock", 4f, 31.50f));
    productsList.add(createProductItem(49, "Iron Iron", 23f, 3.30f));
    productsList.add(createProductItem(0, "Iron Iron", 3f, 8.10f));
    productsList.add(createProductItem(1, "Chair Shoe", 4f, 37.20f));
    productsList.add(createProductItem(4, "Ice Tea Shoe", 48f, 57.60f));
    productsList.add(createProductItem(5, "Clock Clock", 12f, 35.40f));
    productsList.add(createProductItem(6, "Ice Tea Chair", 23f, 14.70f));
    productsList.add(createProductItem(7, "Telephone Shoe", 5f, 12.60f));
    productsList.add(createProductItem(8, "Ice Tea Clock", 14f, 33.90f));
    productsList.add(createProductItem(10, "Telephone Ice Tea", 2f, 30.60f));
    productsList.add(createProductItem(11, "Telephone Iron", 17f, 13.20f));
    productsList.add(createProductItem(12, "Clock Ice Tea", 17f, 50.40f));
    productsList.add(createProductItem(14, "Telephone Iron", 15f, 18.60f));
    productsList.add(createProductItem(23, "Shoe Chair", 10f, 11.40f));
    productsList.add(createProductItem(24, "Chair Shoe", 16f, 10.80f));
    productsList.add(createProductItem(26, "Shoe Shoe", 39f, 75.60f));
    productsList.add(createProductItem(30, "Shoe Iron", 19f, 34.80f));
    productsList.add(createProductItem(31, "Ice Tea Telephone", 2f, 7.20f));
    productsList.add(createProductItem(33, "Iron Chair", 10f, 27.30f));
    productsList.add(createProductItem(35, "Telephone Shoe", 17f, 11.40f));
    productsList.add(createProductItem(36, "Ice Tea Iron", 24f, 4.80f));
    productsList.add(createProductItem(38, "Clock Ice Tea", 3f, 32.40f));
    productsList.add(createProductItem(41, "Clock Ice Tea", 10f, 30.90f));
    productsList.add(createProductItem(48, "Clock Clock", 4f, 31.50f));
    productsList.add(createProductItem(49, "Iron Iron", 23f, 3.30f));
    productsList.add(createProductItem(0, "Iron Iron", 3f, 8.10f));
    productsList.add(createProductItem(1, "Chair Shoe", 4f, 37.20f));
    productsList.add(createProductItem(4, "Ice Tea Shoe", 48f, 57.60f));
    productsList.add(createProductItem(5, "Clock Clock", 12f, 35.40f));
    productsList.add(createProductItem(6, "Ice Tea Chair", 23f, 14.70f));
    productsList.add(createProductItem(7, "Telephone Shoe", 5f, 12.60f));
    productsList.add(createProductItem(8, "Ice Tea Clock", 14f, 33.90f));
    productsList.add(createProductItem(10, "Telephone Ice Tea", 2f, 30.60f));
    productsList.add(createProductItem(11, "Telephone Iron", 17f, 13.20f));
    productsList.add(createProductItem(12, "Clock Ice Tea", 17f, 50.40f));
    productsList.add(createProductItem(14, "Telephone Iron", 15f, 18.60f));
    productsList.add(createProductItem(23, "Shoe Chair", 10f, 11.40f));
    productsList.add(createProductItem(24, "Chair Shoe", 16f, 10.80f));
    productsList.add(createProductItem(26, "Shoe Shoe", 39f, 75.60f));
    productsList.add(createProductItem(30, "Shoe Iron", 19f, 34.80f));
    productsList.add(createProductItem(31, "Ice Tea Telephone", 2f, 7.20f));
    productsList.add(createProductItem(33, "Iron Chair", 10f, 27.30f));
    productsList.add(createProductItem(35, "Telephone Shoe", 17f, 11.40f));
    productsList.add(createProductItem(36, "Ice Tea Iron", 24f, 4.80f));
    productsList.add(createProductItem(38, "Clock Ice Tea", 3f, 32.40f));
    productsList.add(createProductItem(41, "Clock Ice Tea", 10f, 30.90f));
    productsList.add(createProductItem(48, "Clock Clock", 4f, 31.50f));
    productsList.add(createProductItem(49, "Iron Iron", 23f, 3.30f));
    productsList.add(createProductItem(0, "Iron Iron", 3f, 8.10f));
    productsList.add(createProductItem(1, "Chair Shoe", 4f, 37.20f));
    productsList.add(createProductItem(4, "Ice Tea Shoe", 48f, 57.60f));
    productsList.add(createProductItem(5, "Clock Clock", 12f, 35.40f));
    productsList.add(createProductItem(6, "Ice Tea Chair", 23f, 14.70f));
    productsList.add(createProductItem(7, "Telephone Shoe", 5f, 12.60f));
    productsList.add(createProductItem(8, "Ice Tea Clock", 14f, 33.90f));
    productsList.add(createProductItem(10, "Telephone Ice Tea", 2f, 30.60f));
    productsList.add(createProductItem(11, "Telephone Iron", 17f, 13.20f));
    productsList.add(createProductItem(12, "Clock Ice Tea", 17f, 50.40f));
    productsList.add(createProductItem(14, "Telephone Iron", 15f, 18.60f));
    productsList.add(createProductItem(23, "Shoe Chair", 10f, 11.40f));
    productsList.add(createProductItem(24, "Chair Shoe", 16f, 10.80f));
    productsList.add(createProductItem(26, "Shoe Shoe", 39f, 75.60f));
    productsList.add(createProductItem(30, "Shoe Iron", 19f, 34.80f));
    productsList.add(createProductItem(31, "Ice Tea Telephone", 2f, 7.20f));
    productsList.add(createProductItem(33, "Iron Chair", 10f, 27.30f));
    productsList.add(createProductItem(35, "Telephone Shoe", 17f, 11.40f));
    productsList.add(createProductItem(36, "Ice Tea Iron", 24f, 4.80f));
    productsList.add(createProductItem(38, "Clock Ice Tea", 3f, 32.40f));
    productsList.add(createProductItem(41, "Clock Ice Tea", 10f, 30.90f));
    productsList.add(createProductItem(48, "Clock Clock", 4f, 31.50f));
    productsList.add(createProductItem(49, "Iron Iron", 23f, 3.30f));

    return productsList;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static Object createProductItem(int id, String name, float quantity, float price) {

    Map map = new HashMap();
    map.put("Id", id);
    map.put("Name", name);
    map.put("Quantity", quantity);
    map.put("Price", price);
    return map;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static List<HashMap> getAddressAsMapList() {

    List addressList = new ArrayList();
    addressList.add(createAddressItem(9, "James", "Schneider", "277 Seventh Av."));
    addressList.add(createAddressItem(22, "Bill", "Ott", "250 - 20th Ave."));
    return addressList;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static List<HashMap> getAddress2AsMapList() {

    List addressList = new ArrayList();
    addressList.add(createAddressItem(23, "Julia", "Heiniger", "358 College Av."));
    addressList.add(createAddressItem(22, "Bill", "Ott", "250 - 20th Ave."));
    addressList.add(createAddressItem(32, "Michael", "Ott", "339 College Av."));
    return addressList;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static Object createAddressItem(int id, String firstName, String lastName, String street) {

    Map map = new HashMap();
    map.put("Id", id);
    map.put("FirstName", firstName);
    map.put("LastName", lastName);
    map.put("Street", street);
    return map;
  }
}
