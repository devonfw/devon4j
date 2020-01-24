<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet>
<!-- 
public class VisioFixer {
  public static void main(String[] args) throws Exception {

    StreamSource stylesource = new StreamSource("fix-visio-svg.xslt");
    Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.transform(new StreamSource("T-Architecture.svg"), new StreamResult("T-Architecture-fixed.svg"));
  }
}
 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:svg="http://www.w3.org/2000/svg"
xmlns="http://www.w3.org/2000/svg" exclude-result-prefixes="svg">
<xsl:output method="xml" indent="no"/>

<!-- Use description as title (for tooltips) rather than shape ID -->
<xsl:template match="svg:title">
  <title><xsl:value-of select="../svg:desc"/></title>
</xsl:template>

<xsl:template match="@*|node()">
<xsl:copy>
<xsl:apply-templates select="@*|node()"/>
</xsl:copy>
</xsl:template>

</xsl:stylesheet>