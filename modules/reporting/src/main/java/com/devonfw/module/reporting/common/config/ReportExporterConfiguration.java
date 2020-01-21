package com.devonfw.module.reporting.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that returns the {@link JRAbstractExporter exporter} according to the {@link ReportingConstants}
 *
 */
@Configuration
public class ReportExporterConfiguration {

  private final Map<String, JRAbstractExporter> exporterMap;

  /**
   * The constructor.
   */
  public ReportExporterConfiguration() {

    super();
    this.exporterMap = new HashMap<>();
  }

  /**
   * puts the {@link JRAbstractExporter} Instance in to the {@link Map} according to {@link ReportingConstants}
   *
   * @param jrAbstractExporters
   */
  @Inject
  public void setReportExporters(List<JRAbstractExporter> jrAbstractExporters) {

    for (JRAbstractExporter exporter : jrAbstractExporters) {
      this.exporterMap.put(exporter.getExporterKey(), exporter);
    }

  }

  @Bean
  public JRAbstractExporter getExcelExporter() {

    return new JRXlsExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.XLS;
      }
    };
  }

  /**
   * Returns the {@link JRAbstractExporter} for Xls version
   *
   * @return
   */
  @Bean
  public JRAbstractExporter getPdfExporter() {

    return new JRPdfExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.PDF;
      }
    };
  }

  @Bean
  public JRAbstractExporter getCsvExporter() {

    return new JRCsvExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.CSV;
      }
    };
  }

  @Bean
  public JRAbstractExporter getRtfExporter() {

    return new JRRtfExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.RTF;
      }
    };
  }

  @Bean
  public JRAbstractExporter getWordExporter() {

    return new JRRtfExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.WORD;
      }
    };
  }

  @Bean
  public JRAbstractExporter getTextExporter() {

    return new JRTextExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.TEXT;
      }
    };
  }

  @Bean
  public JRAbstractExporter getDocxExporter() {

    return new JRDocxExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.DOCX;
      }
    };
  }

  @Bean
  public JRAbstractExporter getXlsxExporter() {

    return new JRXlsxExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.XLSX;
      }
    };
  }

  @Bean
  public JRAbstractExporter getHtmlExporter() {

    return new HtmlExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.HTML;
      }
    };
  }

  @Bean
  public JRAbstractExporter getOdtExporter() {

    return new JROdtExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.ODT;
      }
    };
  }

  @Bean
  public JRAbstractExporter getOdsExporter() {

    return new JROdsExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.ODS;
      }
    };
  }

  @Bean
  public JRAbstractExporter getPptxExporter() {

    return new JRPptxExporter() {
      @Override
      public String getExporterKey() {

        return ReportingConstants.PPTX;
      }
    };
  }

  /**
   * Returns the {@link JRAbstractExporter} instance
   *
   * @param format key according the {@link ReportingConstants}
   * @return
   */
  public JRAbstractExporter getReportExporter(String format) {

    return this.exporterMap.get(format);
  }

}
