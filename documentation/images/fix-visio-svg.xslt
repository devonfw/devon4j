<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet>
<!-- 
public class VisioFixer {
  public static void main(String[] args) throws Exception {

    StreamSource stylesource = new StreamSource("fix-visio-svg.xslt");
    Transformer transformer = TransformerFactory.newInstance().newTransformer(stylesource);
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.transform(new StreamSource("T-Architecture-visio.svg"), new StreamResult("T-Architecture.svg"));
  }
}
 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:svg="http://www.w3.org/2000/svg"
xmlns="http://www.w3.org/2000/svg" exclude-result-prefixes="svg">
<xsl:output method="xml" indent="no"/>

<!-- Use description as title (for tooltips) rather than shape ID -->
<xsl:template match="svg:title">
  <xsl:variable name="description" select="../svg:desc" />
  <xsl:choose>
    <xsl:when test="string-length($description)&lt;1">
      <title><xsl:value-of select="."/></title>
    </xsl:when>
    <xsl:otherwise>
      <title><xsl:value-of select="$description"/></title>
    </xsl:otherwise>
  </xsl:choose>
  
</xsl:template>

<xsl:template match="@*|node()">
<xsl:copy>
<xsl:apply-templates select="@*|node()"/>
</xsl:copy>
</xsl:template>

</xsl:stylesheet>